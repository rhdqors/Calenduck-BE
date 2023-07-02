package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.entity.NameWithMt20id;
import com.example.calenduck.domain.performance.repository.NameWithMt20idRepository;
import com.example.calenduck.global.exception.GlobalErrorCode;
import com.example.calenduck.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceService {

    private final NameWithMt20idRepository nameWithMt20idRepository;
    private final XmlToMap xmlToMap;

    // 전체 조회 & 메인 - 페이징 X
    @Transactional
    // key 값은 현재 메서드를 나타내는 sple(Spring Expression Language)언어
    // 다시 현재 메서드가 호출되면 데이터를 다시 조회하는 것이 아닌 캐시된 데이터를 불러옴
    @Cacheable(value = "elementsCache", key = "#root.methodName")
    public List<BasePerformancesResponseDto> getAllPerformances() throws SQLException, IOException, ExecutionException, InterruptedException {
        List<BasePerformancesResponseDto> performances = new ArrayList<>();
        List<Elements> elements = xmlToMap.getElements();

        for (Elements element : elements) {
            // Retrieve the required data from the element
            String mt20id = element.select("mt20id").text();
            String poster = element.select("poster").text();
            String prfnm = element.select("prfnm").text();
            String prfcast = element.select("prfcast").text();
            String genrenm = element.select("genrenm").text();
            String fcltynm = element.select("fcltynm").text();
            String dtguidance = element.select("dtguidance").text();
            String stdate = element.select("prfpdfrom").text();
            String eddate = element.select("prfpdto").text();
            String pcseguidance = element.select("pcseguidance").text();

            BasePerformancesResponseDto basePerformancesResponseDto = new BasePerformancesResponseDto(
                    mt20id,
                    poster,
                    prfnm,
                    prfcast,
                    genrenm,
                    fcltynm,
                    dtguidance,
                    stdate,
                    eddate,
                    pcseguidance
            );
            performances.add(basePerformancesResponseDto);
        }
        return performances;
    }




//    @Transactional
//    public Page<BasePerformancesResponseDto> getAllPerformances(int pageIndex, int pageCount) throws SQLException, IOException {
//        List<BasePerformancesResponseDto> performances = new ArrayList<>();
//        List<Elements> elements = xmlToMap.getElements(pageIndex, pageCount);
//        log.info("----------elements = " + elements.toString());
//        for (Elements element : elements) {
//            String mt20id = element.select("mt20id").text();
//            String poster = element.select("poster").text();
//            String prfnm = element.select("prfnm").text();
//            String prfcast = element.select("prfcast").text();
//            String genrenm = element.select("genrenm").text();
//            String fcltynm = element.select("fcltynm").text();
//            String dtguidance = element.select("dtguidance").text();
//            String stdate = element.select("prfpdfrom").text();
//            String eddate = element.select("prfpdto").text();
//            String pcseguidance = element.select("pcseguidance").text();
//
//            BasePerformancesResponseDto basePerformancesResponseDto = new BasePerformancesResponseDto(
//                    mt20id,
//                    poster,
//                    prfnm,
//                    prfcast,
//                    genrenm,
//                    fcltynm,
//                    dtguidance,
//                    stdate,
//                    eddate,
//                    pcseguidance
//            );
//            performances.add(basePerformancesResponseDto);
//        }
//
//        Long totalCount = xmlToMap.getTotalCount();
//        Pageable pageable = PageRequest.of(pageIndex, pageCount);
//        Page<BasePerformancesResponseDto> page = new PageImpl<>(performances, pageable, totalCount);
//        return page;
////        return performances;
//    }


    // 상세 페이지 -> 메인에서 데이터 다 넘김
    // 받아오는 performanceId = mt20id
//    public DetailPerformanceResponseDto getDetailPerformance(String performanceId) throws SQLException, IOException {
//        List<Elements> elements = xmlToMap.getElements();
//        DetailPerformanceResponseDto detailPerformanceResponseDto = null;
//        log.info("------------performanceId = " + performanceId);
//
//        for (Elements element : elements) {
//            String mt20id = element.select("mt20id").text();
//            log.info("----------element = " + element);
//            log.info("----------mt20id = " + mt20id);
//
//            if (mt20id.equals(performanceId)) {
//                String poster = element.select("poster").text();
//                String prfnm = element.select("prfnm").text();
//                String prfcast = element.select("prfcast").text();
//                String genrenm = element.select("genrenm").text();
//                String fcltynm = element.select("fcltynm").text();
//                String dtguidance = element.select("dtguidance").text();
//                String stdate = element.select("prfpdfrom").text();
//                String eddate = element.select("prfpdto").text();
//                String pcseguidance = element.select("pcseguidance").text();
//
//                detailPerformanceResponseDto = new DetailPerformanceResponseDto(poster, prfnm, prfcast, genrenm, fcltynm, dtguidance, stdate, eddate, pcseguidance);
//                break;
//            }
//        }
//        return detailPerformanceResponseDto;
//    }

    public List<NameWithMt20id> getNameWithMt20id(String mt20id) {
        return nameWithMt20idRepository.findAllByMt20id(mt20id);
    }

    public NameWithMt20id getMt20id(String mt20id) {
        return nameWithMt20idRepository.findByMt20id(mt20id).orElseThrow(() -> new GlobalException(GlobalErrorCode.NOT_FOUND_PERFORMANCE));
    }
}