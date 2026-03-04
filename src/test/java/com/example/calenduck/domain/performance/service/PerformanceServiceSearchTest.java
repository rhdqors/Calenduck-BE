package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.http.BatchManager;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceSearchTest {

    @Mock
    private PerformanceSearchBehavior performanceSearchService;

    @Mock
    private BatchManager batchManager;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    private PerformanceService performanceService;

    private Elements createTestElements(String mt20id, String prfnm, String prfcast) {
        String xml = String.format(
                "<db><mt20id>%s</mt20id><poster>poster.jpg</poster><prfnm>%s</prfnm>"
                        + "<prfcast>%s</prfcast><genrenm>뮤지컬</genrenm><fcltynm>극장</fcltynm>"
                        + "<dtguidance>19:30</dtguidance><prfpdfrom>2024.01.01</prfpdfrom>"
                        + "<prfpdto>2024.03.01</prfpdto><pcseguidance>50000원</pcseguidance></db>",
                mt20id, prfnm, prfcast);
        return Jsoup.parse(xml).select("db > *");
    }

    private List<BasePerformancesResponseDto> cachedPerformances() {
        return Arrays.asList(
                new BasePerformancesResponseDto("PF001", "poster1.jpg", "뮤지컬 캣츠", "배우A, 배우C", "뮤지컬", "극장1", "19:30", "2024.01.01", "2024.03.01", "50000원"),
                new BasePerformancesResponseDto("PF002", "poster2.jpg", "오페라의 유령", "배우B", "뮤지컬", "극장2", "19:30", "2024.01.01", "2024.03.01", "60000원"),
                new BasePerformancesResponseDto("PF003", "poster3.jpg", "링크드 콘서트", "배우D", "콘서트", "극장3", "20:00", "2024.02.01", "2024.04.01", "70000원")
        );
    }

    @BeforeEach
    void setUp() {
        performanceService = new PerformanceService(performanceSearchService, batchManager, cacheManager);
    }

    @Nested
    @DisplayName("검색 필터링 로직")
    class SearchFiltering {

        @BeforeEach
        void setUpCache() {
            when(cacheManager.getCache("elementsCache")).thenReturn(cache);
            when(cache.get("getAllPerformances")).thenReturn(() -> cachedPerformances());
        }

        @Test
        @DisplayName("공연명으로 검색 시 공연명에 검색어가 포함된 공연을 반환한다")
        void searchByName_returnsMatchingByName() throws Exception {
            List<BasePerformancesResponseDto> result = performanceService.getAllPerformances("캣츠", null);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPrfnm()).contains("캣츠");
        }

        @Test
        @DisplayName("출연진으로 검색 시 출연진에 검색어가 포함된 공연을 반환한다")
        void searchByCast_returnsMatchingByCast() throws Exception {
            List<BasePerformancesResponseDto> result = performanceService.getAllPerformances(null, "배우B");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPrfcast()).contains("배우B");
        }

        @Test
        @DisplayName("공연명과 출연진 동시 검색 시 OR 조건으로 매칭한다")
        void searchByBoth_returnsMatchingEither() throws Exception {
            // "캣츠"는 PF001 공연명에, "배우B"는 PF002 출연진에 매칭
            List<BasePerformancesResponseDto> result = performanceService.getAllPerformances("캣츠", "배우B");

            assertThat(result).hasSize(2);
            assertThat(result).extracting(BasePerformancesResponseDto::getMt20id)
                    .containsExactlyInAnyOrder("PF001", "PF002");
        }

        @Test
        @DisplayName("검색어와 일치하는 공연이 없으면 빈 리스트를 반환한다")
        void searchNoMatch_returnsEmptyList() throws Exception {
            List<BasePerformancesResponseDto> result = performanceService.getAllPerformances("존재하지않는공연", null);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("검색은 대소문자를 구분하지 않는다")
        void searchIsCaseInsensitive() throws Exception {
            List<BasePerformancesResponseDto> result = performanceService.getAllPerformances("링크드", null);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getMt20id()).isEqualTo("PF003");
        }

        @Test
        @DisplayName("검색 시 검색어를 인기검색어에 반영한다")
        void search_updatesPopularSearchWord() throws Exception {
            performanceService.getAllPerformances("캣츠", null);

            verify(performanceSearchService).updatePopularSearchWord("캣츠");
        }

        @Test
        @DisplayName("인기검색어 업데이트 실패 시에도 검색 결과는 정상 반환한다")
        void search_withRedisFailure_stillReturnsResults() throws Exception {
            doThrow(new RuntimeException("Redis 연결 실패"))
                    .when(performanceSearchService).updatePopularSearchWord(any());

            List<BasePerformancesResponseDto> result = performanceService.getAllPerformances("캣츠", null);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPrfnm()).contains("캣츠");
        }
    }

    @Nested
    @DisplayName("검색 시 캐시 활용")
    class SearchCacheUsage {

        @Test
        @DisplayName("캐시에 데이터가 있으면 BatchManager를 호출하지 않는다")
        void search_withCachedData_doesNotCallBatchManager() throws Exception {
            when(cacheManager.getCache("elementsCache")).thenReturn(cache);
            when(cache.get("getAllPerformances")).thenReturn(() -> cachedPerformances());

            performanceService.getAllPerformances("캣츠", null);

            verify(batchManager, never()).getElements();
        }

        @Test
        @DisplayName("캐시가 비어있으면 BatchManager를 호출하여 데이터를 로드한다")
        void search_withEmptyCache_callsBatchManager() throws Exception {
            when(cacheManager.getCache("elementsCache")).thenReturn(cache);
            when(cache.get("getAllPerformances")).thenReturn(null);

            Elements el1 = createTestElements("PF001", "뮤지컬 캣츠", "배우A");
            when(batchManager.getElements()).thenReturn(Arrays.asList(el1));

            List<BasePerformancesResponseDto> result = performanceService.getAllPerformances(null, null);

            verify(batchManager).getElements();
            assertThat(result).hasSize(1);
        }
    }
}
