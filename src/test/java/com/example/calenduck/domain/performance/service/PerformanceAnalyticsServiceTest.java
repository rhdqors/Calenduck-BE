package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.bookmark.repository.BookmarkRepository;
import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.dto.response.RankingCountResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerformanceAnalyticsServiceTest {

    @Mock
    private PerformanceServiceBehavior performanceService;

    @Mock
    private BookmarkRepository bookmarkRepository;

    private PerformanceAnalyticsService analyticsService;

    private List<BasePerformancesResponseDto> samplePerformances() {
        return Arrays.asList(
                new BasePerformancesResponseDto("PF001", "p1.jpg", "뮤지컬 캣츠", "배우A", "뮤지컬", "세종문화회관", "19:30", "2024.01.01", "2024.03.01", "50000원"),
                new BasePerformancesResponseDto("PF002", "p2.jpg", "오페라의 유령", "배우B", "뮤지컬", "예술의전당", "19:30", "2024.01.01", "2024.03.01", "60000원"),
                new BasePerformancesResponseDto("PF003", "p3.jpg", "햄릿", "배우C", "연극", "세종문화회관", "20:00", "2024.02.01", "2024.04.01", "40000원"),
                new BasePerformancesResponseDto("PF004", "p4.jpg", "백조의 호수", "배우D", "무용", "예술의전당", "19:00", "2024.01.15", "2024.03.15", "70000원"),
                new BasePerformancesResponseDto("PF005", "p5.jpg", "라보엠", "배우E", "뮤지컬", "LG아트센터", "19:30", "2024.02.01", "2024.05.01", "55000원")
        );
    }

    @BeforeEach
    void setUp() {
        analyticsService = new PerformanceAnalyticsService(performanceService, bookmarkRepository);
    }

    @Nested
    @DisplayName("장르별 공연 수 집계")
    class GenreRanking {

        @Test
        @DisplayName("캐시된 공연을 장르별로 집계하여 내림차순 반환한다")
        void genreRanking_groupsByGenreDescending() throws Exception {
            when(performanceService.getAllPerformances(null, null)).thenReturn(samplePerformances());

            List<RankingCountResponse> result = analyticsService.popularityByGenreWithRegion();

            assertThat(result).hasSize(3);
            assertThat(result.get(0).getName()).isEqualTo("뮤지컬");
            assertThat(result.get(0).getCount()).isEqualTo(3);
            assertThat(result.get(1).getName()).isEqualTo("연극");
            assertThat(result.get(1).getCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("캐시가 비어있으면 빈 리스트를 반환한다")
        void genreRanking_emptyCache_returnsEmpty() throws Exception {
            when(performanceService.getAllPerformances(null, null)).thenReturn(Collections.emptyList());

            List<RankingCountResponse> result = analyticsService.popularityByGenreWithRegion();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("시설별 공연 수 집계")
    class FacilityRanking {

        @Test
        @DisplayName("캐시된 공연을 시설별로 집계하여 내림차순 반환한다")
        void facilityRanking_groupsByFacilityDescending() throws Exception {
            when(performanceService.getAllPerformances(null, null)).thenReturn(samplePerformances());

            List<RankingCountResponse> result = analyticsService.popularityByRegion();

            assertThat(result).hasSize(3);
            assertThat(result.get(0).getCount()).isEqualTo(2);
            assertThat(result.get(1).getCount()).isEqualTo(2);
            assertThat(result.get(2).getCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Top 10 인기 공연 (북마크 기준)")
    class TopTenRanking {

        @Test
        @DisplayName("북마크 수 기준 Top 10 공연을 반환한다")
        void topTen_returnsByBookmarkCount() throws Exception {
            List<Object[]> bookmarkCounts = Arrays.asList(
                    new Object[]{"PF001", 15L},
                    new Object[]{"PF003", 8L}
            );
            when(bookmarkRepository.findTopBookmarkedPerformances(any(Pageable.class))).thenReturn(bookmarkCounts);
            when(performanceService.getAllPerformances(null, null)).thenReturn(samplePerformances());

            List<RankingCountResponse> result = analyticsService.topTen();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo("PF001");
            assertThat(result.get(0).getName()).isEqualTo("뮤지컬 캣츠");
            assertThat(result.get(0).getCount()).isEqualTo(15);
            assertThat(result.get(1).getId()).isEqualTo("PF003");
            assertThat(result.get(1).getName()).isEqualTo("햄릿");
            assertThat(result.get(1).getCount()).isEqualTo(8);
        }

        @Test
        @DisplayName("북마크가 없으면 빈 리스트를 반환한다")
        void topTen_noBookmarks_returnsEmpty() throws Exception {
            when(bookmarkRepository.findTopBookmarkedPerformances(any(Pageable.class))).thenReturn(Collections.emptyList());

            List<RankingCountResponse> result = analyticsService.topTen();

            assertThat(result).isEmpty();
        }
    }
}
