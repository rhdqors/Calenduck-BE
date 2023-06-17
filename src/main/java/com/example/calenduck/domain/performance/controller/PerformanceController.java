package com.example.calenduck.domain.performance.controller;

import com.example.calenduck.domain.performance.service.PerformanceService;
import com.example.calenduck.global.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    @Operation(summary = "전체 조회 & 메인", description = "전체 조회 & 메인")
    @GetMapping()
    public ResponseEntity<?> getAllPerformances() {
        return ResponseMessage.SuccessResponse("전체 조회 성공", performanceService.getAllPerformances());
    }



}
