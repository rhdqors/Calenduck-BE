package com.example.calenduck.domain.performance.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface PerformanceAnalyticsBehavior {
    JsonNode popularityByGenreWithRegion(); // 지역별 장르 인기도
    JsonNode topTen(); // 인기 공연 탑텐
    JsonNode popularityByRegion(); // 지역별 인기 공연
}
