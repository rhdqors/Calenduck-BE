package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.SearchRankResponseDto;

import java.util.List;

public interface PerformanceSearchBehavior {
    void updatePopularSearchWord(String searchTerm); // 인기검색어에 저장
    List<SearchRankResponseDto> searchRankList(); // 인기검색어 5위까지 조회
}
