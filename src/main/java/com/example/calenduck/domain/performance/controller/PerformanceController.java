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

//    @Operation(summary = "상세 조회", description = "상세 조회")
//    @GetMapping("/{performance-id}")
//    public ResponseEntity<?> getDetailPerformance(@PathVariable("performance-id") String performanceId) throws SQLException, IOException {
//        return ResponseMessage.SuccessResponse("상세 조회 성공", performanceService.getDetailPerformance(performanceId));
//    }

//    @Operation(summary = "찜목록 성공, 취소", description = "찜목록 성공, 취소")
//    @PostMapping("/{mt20id}/bookmark")
//    public ResponseEntity<?> bookmark(@PathVariable("mt20id") String mt20id, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        String message = "";
//        if (userDetails != null) {
//            message = performanceService.bookmark(mt20id, userDetails.getUser());
////        message = performanceService.bookmark(mt20id);
//        } else {
//            throw new GlobalException(GlobalErrorCode.INVALID_TOKEN);
//        }
//        return ResponseMessage.SuccessResponse(message, "");
//    }

}
