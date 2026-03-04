package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.RankingCountResponse;

import java.util.List;

public interface PerformanceAnalyticsBehavior {
    List<RankingCountResponse> popularityByGenreWithRegion();
    List<RankingCountResponse> topTen();
    List<RankingCountResponse> popularityByRegion();
}
