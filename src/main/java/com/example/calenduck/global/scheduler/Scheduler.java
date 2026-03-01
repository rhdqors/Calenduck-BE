package com.example.calenduck.global.scheduler;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final PerformanceService performanceService;

    // 매일 자정 메인페이지(전체 조회) 캐시 업데이트
    @Scheduled(cron = "0 0 0 * * *")
    public void updatePerformancesCache() {
        try {
            List<BasePerformancesResponseDto> result = performanceService.refreshPerformancesCache();
            if (result.isEmpty()) {
                log.warn("캐시 갱신 결과 비어있음 — KOPIS API 문제 가능성");
            } else {
                log.info("캐시 갱신 성공: {}건", result.size());
            }
        } catch (Exception e) {
            log.error("캐시 갱신 실패, 기존 캐시 유지: {}", e.getMessage(), e);
        }
    }

}
