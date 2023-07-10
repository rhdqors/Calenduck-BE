package com.example.calenduck.domain.graph.controller;

import com.example.calenduck.domain.graph.service.GraphService;
import com.example.calenduck.global.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GraphController {

    private final GraphService graphService;

    @Operation(summary = "장르별 인기도 - 지역별", description = "장르별 인기도 - 지역별")
    @GetMapping("/{performance-id}")
    public ResponseEntity<?> PopularityByRegion() {
        return ResponseMessage.SuccessResponse("장르별 인기도 - 지역별", graphService.PopularityByRegion());
    }

}
