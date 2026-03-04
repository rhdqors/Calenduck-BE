package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.bookmark.repository.BookmarkRepository;
import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.dto.response.RankingCountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceAnalyticsService implements PerformanceAnalyticsBehavior {

    private final PerformanceServiceBehavior performanceService;
    private final BookmarkRepository bookmarkRepository;

    private static final int TOP_TEN_SIZE = 10;

    @Override
    public List<RankingCountResponse> popularityByGenreWithRegion() {
        List<BasePerformancesResponseDto> performances = getAllPerformancesSafely();
        return performances.stream()
                .collect(Collectors.groupingBy(BasePerformancesResponseDto::getGenrenm, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> new RankingCountResponse(null, entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<RankingCountResponse> topTen() {
        List<Object[]> bookmarkCounts = bookmarkRepository.findTopBookmarkedPerformances(PageRequest.of(0, TOP_TEN_SIZE));
        if (bookmarkCounts.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, String> performanceNames = getPerformanceNameMap();

        return bookmarkCounts.stream()
                .map(row -> {
                    String mt20id = (String) row[0];
                    long count = (Long) row[1];
                    String name = performanceNames.getOrDefault(mt20id, mt20id);
                    return new RankingCountResponse(mt20id, name, count);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<RankingCountResponse> popularityByRegion() {
        List<BasePerformancesResponseDto> performances = getAllPerformancesSafely();
        return performances.stream()
                .collect(Collectors.groupingBy(BasePerformancesResponseDto::getFcltynm, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> new RankingCountResponse(null, entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private List<BasePerformancesResponseDto> getAllPerformancesSafely() {
        try {
            return performanceService.getAllPerformances(null, null);
        } catch (Exception e) {
            log.error("공연 데이터 로드 실패", e);
            return Collections.emptyList();
        }
    }

    private Map<String, String> getPerformanceNameMap() {
        return getAllPerformancesSafely().stream()
                .collect(Collectors.toMap(
                        BasePerformancesResponseDto::getMt20id,
                        BasePerformancesResponseDto::getPrfnm,
                        (existing, replacement) -> existing
                ));
    }
}
