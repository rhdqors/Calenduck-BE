package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.dto.response.DetailPerformanceResponseDto;
import com.example.calenduck.domain.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final XmlToMap xmlToMap;

    // 전체 조회 & 메인 - 페이징 X
    @Transactional
    public List<BasePerformancesResponseDto> getAllPerformances() throws SQLException, IOException {
        List<BasePerformancesResponseDto> performances = new ArrayList<>();
        List<Elements> elements = xmlToMap.getElements();
        log.info("----------elements = " + elements.toString());
        for (Elements element : elements) {
            log.info("--------------------element = " + element);
            String mt20id = element.select("mt20id").text();
            String poster = element.select("poster").text();
            String prfnm = element.select("prfnm").text();
            String prfcast = element.select("prfcast").text();

            BasePerformancesResponseDto basePerformancesResponseDto = new BasePerformancesResponseDto(mt20id, poster, prfnm, prfcast);
            performances.add(basePerformancesResponseDto);
        }
        return performances;
    }

    // 상세 페이지
    // 받아오는 performanceId = mt20id
    public DetailPerformanceResponseDto getDetailPerformance(String performanceId) throws SQLException, IOException {
        List<Elements> elements = xmlToMap.getElements();
        DetailPerformanceResponseDto detailPerformanceResponseDto = null;
        log.info("------------performanceId = " + performanceId);

        for (Elements element : elements) {
            String mt20id = element.select("mt20id").text();
            log.info("----------element = " + element);
            log.info("----------mt20id = " + mt20id);

            if (mt20id.equals(performanceId)) {
                String poster = element.select("poster").text();
                String prfnm = element.select("prfnm").text();
                String prfcast = element.select("prfcast").text();
                String genrenm = element.select("genrenm").text();
                String fcltynm = element.select("fcltynm").text();
                String dtguidance = element.select("dtguidance").text();
                String stdate = element.select("prfpdfrom").text();
                String eddate = element.select("prfpdto").text();
                String pcseguidance = element.select("pcseguidance").text();

                detailPerformanceResponseDto = new DetailPerformanceResponseDto(poster, prfnm, prfcast, genrenm, fcltynm, dtguidance, stdate, eddate, pcseguidance);
                break;
            }
        }
        return detailPerformanceResponseDto;
    }

}
