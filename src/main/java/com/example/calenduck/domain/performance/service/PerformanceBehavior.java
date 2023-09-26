package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.dto.response.SearchRankResponseDto;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PerformanceBehavior {
    List<BasePerformancesResponseDto> getAllPerformances(String prfnm, String prfcast) throws ExecutionException, InterruptedException; // 전체 공연 조회
    void updatePopularSearchTerm(String searchTerm); // 인기검색어에 저장
    List<SearchRankResponseDto> searchRankList(); // 인기검색어 5위까지 조회
    JsonNode PopularityByGenreWithRegion(); // 지역별 장르 인기도
    JsonNode topTen(); // 인기 공연 탑텐
    JsonNode PopularityByRegion(); // 지역별 인기 공연

}
