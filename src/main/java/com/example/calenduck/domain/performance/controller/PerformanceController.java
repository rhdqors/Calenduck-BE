package com.example.calenduck.domain.performance.controller;

import com.example.calenduck.domain.performance.service.PerformanceService;
import com.example.calenduck.global.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

//    @Operation(summary = "전체 조회 & 메인", description = "전체 조회 & 메인")
//    @GetMapping()
//    public ResponseEntity<?> getAllPerformances() throws SQLException, IOException {
//        return ResponseMessage.SuccessResponse("전체 조회 성공", performanceService.getAllPerformances());
//    }
    @Operation(summary = "전체 조회 & 메인", description = "전체 조회 & 메인")
    @GetMapping()
    public ResponseEntity<?> getAllPerformances(@RequestParam(defaultValue = "10") int pageSize) throws SQLException, IOException {
        return ResponseMessage.SuccessResponse("전체 조회 성공", performanceService.getAllPerformances(pageSize));
    }
//    @Operation(summary = "전체 조회 & 메인", description = "전체 조회 & 메인")
//    @GetMapping()
//    public ResponseEntity<?> getAllPerformances(@RequestParam(defaultValue = "10") int pageSize) throws SQLException, IOException {
//        List<BasePerformancesResponseDto> performances = performanceService.getAllPerformances(pageSize);
//        return ResponseMessage.SuccessResponse("전체 조회 성공", performances);
//    }

//    @Operation(summary = "상세 조회", description = "전체 조회")
//    @GetMapping("{/performance-id}")
//    public ResponseEntity<?> getDetailPerformance(@PathVariable Long performanceId) {
//        return ResponseMessage.SuccessResponse("상세 조회 성공", performanceService.getDetailPerformance(performanceId));
//    }

}
