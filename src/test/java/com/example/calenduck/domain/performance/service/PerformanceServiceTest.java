package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.http.BatchManager;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceTest {

    @Mock
    private PerformanceSearchBehavior performanceSearchService;

    @Mock
    private BatchManager batchManager;

    @InjectMocks
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

    @Test
    @DisplayName("BatchManager 성공 시 전체 공연 목록을 반환한다")
    void getAllPerformances_withElements_returnsPerformances() throws Exception {
        // Given
        Elements elements1 = createTestElements("PF001", "뮤지컬 캣츠", "배우A");
        Elements elements2 = createTestElements("PF002", "오페라의 유령", "배우B");
        when(batchManager.getElements()).thenReturn(Arrays.asList(elements1, elements2));

        // When
        List<BasePerformancesResponseDto> result = performanceService.getAllPerformances(null, null);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("BatchManager가 빈 리스트를 반환하면 빈 리스트를 반환한다")
    void getAllPerformances_withEmptyElements_returnsEmptyList() throws Exception {
        // Given
        when(batchManager.getElements()).thenReturn(Collections.emptyList());

        // When
        List<BasePerformancesResponseDto> result = performanceService.getAllPerformances(null, null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("refreshPerformancesCache 성공 시 결과를 반환한다")
    void refreshPerformancesCache_onSuccess_returnsPerformances() throws Exception {
        // Given
        Elements elements = createTestElements("PF001", "뮤지컬 캣츠", "배우A");
        when(batchManager.getElements()).thenReturn(Arrays.asList(elements));

        // When
        List<BasePerformancesResponseDto> result = performanceService.refreshPerformancesCache();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMt20id()).isEqualTo("PF001");
    }

    @Test
    @DisplayName("refreshPerformancesCache 실패 시 빈 리스트를 반환한다")
    void refreshPerformancesCache_onFailure_returnsEmptyList() throws Exception {
        // Given
        when(batchManager.getElements()).thenThrow(new RuntimeException("KOPIS API 장애"));

        // When
        List<BasePerformancesResponseDto> result = performanceService.refreshPerformancesCache();

        // Then
        assertThat(result).isEmpty();
    }
}
