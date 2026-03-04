package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.http.BatchManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceService implements PerformanceServiceBehavior {

    private final PerformanceSearchBehavior performanceSearchService;
    private final BatchManager batchManager;
    private final CacheManager cacheManager;

    private static final String CACHE_NAME = "elementsCache";
    private static final String CACHE_KEY = "getAllPerformances";

    @Override
    public List<BasePerformancesResponseDto> getAllPerformances(String prfnm, String prfcast) throws ExecutionException, InterruptedException {
        List<BasePerformancesResponseDto> allPerformances = getOrLoadAllPerformances();

        if (prfnm == null && prfcast == null) {
            return allPerformances;
        }

        String lowerPrfnm = prfnm != null ? prfnm.toLowerCase() : null;
        String lowerPrfcast = prfcast != null ? prfcast.toLowerCase() : null;
        updateSearchWord(prfnm, prfcast);

        return allPerformances.stream()
                .filter(dto -> isMatch(lowerPrfnm, lowerPrfcast, dto))
                .collect(Collectors.toList());
    }

    // 스케줄러용 캐시 갱신 메서드
    @Override
    @CachePut(value = "elementsCache",
            key = "'getAllPerformances'",
            unless = "#result.isEmpty()")
    public List<BasePerformancesResponseDto> refreshPerformancesCache() {
        try {
            List<Elements> elements = batchManager.getElements();
            return convertAllToDto(elements);
        } catch (Exception e) {
            log.error("캐시 갱신 중 KOPIS API 호출 실패", e);
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    private List<BasePerformancesResponseDto> getOrLoadAllPerformances() throws ExecutionException, InterruptedException {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            Cache.ValueWrapper cached = cache.get(CACHE_KEY);
            if (cached != null) {
                return (List<BasePerformancesResponseDto>) cached.get();
            }
        }

        try {
            List<Elements> elements = batchManager.getElements();
            List<BasePerformancesResponseDto> result = convertAllToDto(elements);
            if (!result.isEmpty() && cache != null) {
                cache.put(CACHE_KEY, result);
            }
            return result;
        } catch (ExecutionException e) {
            log.error("실행 에러 발생", e);
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("스레드 중단됨", e);
            throw e;
        }
    }

    private void updateSearchWord(String prfnm, String prfcast) {
        if (prfnm != null) performanceSearchService.updatePopularSearchWord(prfnm);
        if (prfcast != null) performanceSearchService.updatePopularSearchWord(prfcast);
    }

    private BasePerformancesResponseDto createPerformanceDto(Elements element) {
        return new BasePerformancesResponseDto(
                element.select("mt20id").text(),
                element.select("poster").text(),
                element.select("prfnm").text(),
                element.select("prfcast").text(),
                element.select("genrenm").text(),
                element.select("fcltynm").text(),
                element.select("dtguidance").text(),
                element.select("prfpdfrom").text(),
                element.select("prfpdto").text(),
                element.select("pcseguidance").text()
        );
    }

    private boolean isMatch(String lowerPrfnm, String lowerPrfcast, BasePerformancesResponseDto dto) {
        if (lowerPrfnm == null && lowerPrfcast == null) {
            return true;
        }

        boolean nameMatch = lowerPrfnm != null && dto.getPrfnm().toLowerCase().contains(lowerPrfnm);
        boolean castMatch = lowerPrfcast != null && dto.getPrfcast().toLowerCase().contains(lowerPrfcast);
        return nameMatch || castMatch;
    }

    private List<BasePerformancesResponseDto> convertAllToDto(List<Elements> elements) {
        List<BasePerformancesResponseDto> performances = new ArrayList<>();
        for (Elements element : elements) {
            performances.add(createPerformanceDto(element));
        }
        return performances;
    }

}
