package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceService implements PerformanceServiceBehavior {

    private final XmlToMap xmlToMap;
    private final PerformanceSearchBehavior performanceSearchService;

    // 전체 조회 & 메인 & 검색
    @Override
    @Transactional
    // key 값은 현재 메서드를 나타내는 sple(Spring Expression Language)언어
    // 다시 현재 메서드가 호출되면 데이터를 다시 조회하는 것이 아닌 캐시된 데이터를 불러옴
    @Cacheable(value = "elementsCache", condition = "#prfnm == null and #prfcast == null", key = "#root.methodName")
    public List<BasePerformancesResponseDto> getAllPerformances(String prfnm, String prfcast) throws ExecutionException, InterruptedException {
        try {
            List<Elements> elements = xmlToMap.getElements();
            String lowerPrfnm = searchPerformanceNullCheck(prfnm);
            String lowerPrfcast = searchCastNullCheck(prfcast);
            List<BasePerformancesResponseDto> performances = savePerformanceInformation(elements, lowerPrfnm, lowerPrfcast);

            updateSearchWord(prfnm, prfcast);
            return performances;
        } catch (ExecutionException e) {
            log.error("실헹 에러 발생", e);
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태를 다시 설정
            log.error("스레드 중단됨", e);
            throw e;
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