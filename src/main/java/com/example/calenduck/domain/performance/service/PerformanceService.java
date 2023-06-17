package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final XmlToJson xmlToJson;

    // 전체 조회 & 메인
    // 포스터, 공연명, 출연진
    // todo xmlToJson 클래스의 2개 메소드 list로 변환 필요ㅜ
    public List<BasePerformancesResponseDto> getAllPerformances() {
//         maps = xmlToJson.xmlToMap();
//        List<Map<String, String>> maps = xmlToJson.xmlToMap();
        List<BasePerformancesResponseDto> responseDtos = new ArrayList<>();
//        for (BasePerformancesResponseDto responseDto : responseDtos) {
//            maps.get("poster");
//            responseDto.
//        }

        return responseDtos;
    }

}
