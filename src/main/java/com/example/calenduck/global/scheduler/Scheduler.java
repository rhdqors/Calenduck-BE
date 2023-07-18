package com.example.calenduck.global.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.cache.annotation.CacheEvict;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    // 매일 자정 메인페이지(전체 조회) 캐시 업데이트
    @Scheduled(cron = "0 0 0 * * *")
    @CacheEvict(value = "elementsCache", allEntries = true)
    public void evictAllPerformancesCache() {
    }

}
