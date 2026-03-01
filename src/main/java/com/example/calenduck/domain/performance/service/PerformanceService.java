package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.http.BatchManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceService implements PerformanceServiceBehavior {

    private final PerformanceSearchBehavior performanceSearchService;
    private final BatchManager batchManager;

    // 전체 조회 & 메인 & 검색
    @Override
    @Transactional
    @Cacheable(value = "elementsCache",
            condition = "#prfnm == null and #prfcast == null",
            key = "#root.methodName",
            unless = "#result.isEmpty()")
    public List<BasePerformancesResponseDto> getAllPerformances(String prfnm, String prfcast) throws ExecutionException, InterruptedException {
        try {
            List<Elements> elements = batchManager.getElements();
            String lowerPrfnm = searchPerformanceNullCheck(prfnm);
            String lowerPrfcast = searchCastNullCheck(prfcast);
            List<BasePerformancesResponseDto> performances = savePerformanceInformation(elements, lowerPrfnm, lowerPrfcast);

            updateSearchWord(prfnm, prfcast);
            return performances;
        } catch (ExecutionException e) {
            log.error("실행 에러 발생", e);
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("스레드 중단됨", e);
            throw e;
        }
    }

    // 스케줄러용 캐시 갱신 메서드
    @Override
    @CachePut(value = "elementsCache",
            key = "'getAllPerformances'",
            unless = "#result.isEmpty()")
    public List<BasePerformancesResponseDto> refreshPerformancesCache() {
        try {
            List<Elements> elements = batchManager.getElements();
            return savePerformanceInformation(elements, null, null);
        } catch (Exception e) {
            log.error("캐시 갱신 중 KOPIS API 호출 실패", e);
            return Collections.emptyList();
        }
    }

    private void updateSearchWord(String prfnm, String prfcast) {
        if (prfnm != null) performanceSearchService.updatePopularSearchWord(prfnm);
        if (prfcast != null) performanceSearchService.updatePopularSearchWord(prfcast);
    }

    private String searchPerformanceNullCheck(String prfnm) {
        return prfnm != null ? prfnm.toLowerCase() : null;
    }

    private String searchCastNullCheck(String prfcast) {
        return prfcast != null ? prfcast.toLowerCase() : null;
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
        return (lowerPrfnm == null || dto.getPrfnm().toLowerCase().contains(lowerPrfnm))
                && (lowerPrfcast == null || dto.getPrfcast().toLowerCase().contains(lowerPrfcast));
    }

    private List<BasePerformancesResponseDto> savePerformanceInformation(List<Elements> elements, String lowerPrfnm, String lowerPrfcast) {
        List<BasePerformancesResponseDto> performances = new ArrayList<>();

        for (Elements element : elements) {
            BasePerformancesResponseDto dto = createPerformanceDto(element);
            if (isMatch(lowerPrfnm, lowerPrfcast, dto)) {
                performances.add(dto);
            }
        }
        return performances;
    }

}
