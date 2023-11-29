package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PerformanceServiceBehavior {
    List<BasePerformancesResponseDto> getAllPerformances(String prfnm, String prfcast) throws ExecutionException, InterruptedException; // 전체 공연 조회
}
