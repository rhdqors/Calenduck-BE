package com.example.calenduck.domain.performance.controller;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.service.PerformanceService;
import com.example.calenduck.domain.user.security.UserDetailsImpl;
import com.example.calenduck.global.exception.GlobalErrorCode;
import com.example.calenduck.global.exception.GlobalException;
import com.example.calenduck.global.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    @Operation(summary = "전체 조회 & 메인", description = "전체 조회 & 메인")
    @GetMapping()
    public ResponseEntity<?> getAllPerformances(
            @RequestParam(required = false) String prfnm,
            @RequestParam(required = false) String prfcast
    ) throws SQLException, IOException, ExecutionException, InterruptedException {
        List<BasePerformancesResponseDto> performances = performanceService.getAllPerformances(prfnm, prfcast);
        return ResponseMessage.SuccessResponse("전체 조회 완료", performances);
    }

    @Operation(summary = "장르별 인기도 - 지역별", description = "장르별 인기도 - 지역별")
    @GetMapping("/popularity/genres/region")
    public ResponseEntity<?> PopularityByGenreWithRegion() {
        return ResponseMessage.SuccessResponse("장르별 인기도 - 지역별", performanceService.PopularityByGenreWithRegion());
    }

//    @Operation(summary = "장르별 인기도 - 랭킹점수 가산", description = "장르별 인기도 - 랭킹점수 가산")
//    @GetMapping("/popularity/genre/rank")
//    public ResponseEntity<?> PopularityByRegion() {
//        return ResponseMessage.SuccessResponse("장르별 인기도 - 랭킹점수 가산", performanceService.PopularityByRegion());
//    }

    @Operation(summary = "Top 10", description = "Top 10")
    @GetMapping("/topten")
    public ResponseEntity<?> topTen() {
        return ResponseMessage.SuccessResponse("Top 10", performanceService.topTen());
    }

    @Operation(summary = "지역별 인기 공연", description = "지역별 인기 공연")
    @GetMapping("/popularity/region")
    public ResponseEntity<?> PopularityByRegion() {
        return ResponseMessage.SuccessResponse("지역별 인기 공연", performanceService.PopularityByRegion());
    }






}
