package com.example.calenduck.global.scheduler;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.service.PerformanceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchedulerTest {

    @Mock
    private PerformanceService performanceService;

    @InjectMocks
    private Scheduler scheduler;

    @Test
    @DisplayName("refreshPerformancesCache 성공 시 정상 갱신된다")
    void updatePerformancesCache_onSuccess_completesNormally() {
        // Given
        BasePerformancesResponseDto dto = new BasePerformancesResponseDto(
                "PF001", "poster.jpg", "공연1", "배우1", "뮤지컬",
                "극장1", "19:30", "2024.01.01", "2024.03.01", "50000원");
        when(performanceService.refreshPerformancesCache()).thenReturn(Arrays.asList(dto));

        // When
        scheduler.updatePerformancesCache();

        // Then
        verify(performanceService).refreshPerformancesCache();
    }

    @Test
    @DisplayName("refreshPerformancesCache가 빈 리스트를 반환해도 예외 없이 처리된다")
    void updatePerformancesCache_onEmptyResult_handlesGracefully() {
        // Given
        when(performanceService.refreshPerformancesCache()).thenReturn(Collections.emptyList());

        // When
        scheduler.updatePerformancesCache();

        // Then
        verify(performanceService).refreshPerformancesCache();
    }

    @Test
    @DisplayName("refreshPerformancesCache 예외 시 기존 캐시를 유지하며 예외 없이 처리된다")
    void updatePerformancesCache_onException_handlesGracefully() {
        // Given
        when(performanceService.refreshPerformancesCache()).thenThrow(new RuntimeException("API 장애"));

        // When
        scheduler.updatePerformancesCache();

        // Then
        verify(performanceService).refreshPerformancesCache();
    }
}
