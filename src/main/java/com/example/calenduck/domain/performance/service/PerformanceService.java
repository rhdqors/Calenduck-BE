package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.io.BufferedReader;
import java.util.concurrent.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final XmlToMap xmlToMap;

    // 전체 조회 & 메인
//    @Transactional
//    public List<BasePerformancesResponseDto> getAllPerformances(int pageSize) throws SQLException, IOException {
//        List<String> performanceIds = xmlToMap.getPerformanceIdList();
//        List<BasePerformancesResponseDto> responseDtos = new ArrayList<>();
//        String poster = "";
//        String prfnm = "";
//        String prfcast = "";
//
//        // --- url + id 조합으로 공연별 상세정보 조회 ---
//        for (String performanceId : performanceIds) {
//            StringBuilder response = new StringBuilder();
//            log.info("performanceId = " + performanceId);
//            URL url = new URL("http://kopis.or.kr/openApi/restful/pblprfr/" + performanceId + "?service=60a3d3573c5e4d8bb052a4abebff27b6");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//
//            int responseCode = connection.getResponseCode();
//            log.info("Response Code: " + responseCode);
//
//            // Read the response
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            reader.close();
//
//            // response = 공연id의 상세정보 xml 보기
//            log.info("Response XML:\n" + response.toString());
//
//            // --- xml -> 문서 -> 문자열변수 저장 ---
//            Document doc = Jsoup.parse(response.toString());
//            log.info("doc = " + doc.toString());
//
//            // 데이터 <db> 요소 안의 모든 데이터를 선택
//            Elements elements = doc.select("db > *");
//            log.info("elements = " + elements.toString());
//
//            poster = elements.select("poster").text();
//            prfnm = elements.select("prfnm").text();
//            prfcast = elements.select("prfcast").text();
//
//            // Performance performance = performanceRepository.findByMt20id(performanceId).orElseThrow( () -> new GlobalException(GlobalErrorCode.NOT_FOUND_PERFORMANCE));
//            // responseDtos.add(new BasePerformancesResponseDto(performance, poster, prfnm, prfcast));
//            // responseDtos.add(new BasePerformancesResponseDto(new Performance(), poster, prfnm, prfcast));
//            responseDtos.add(new BasePerformancesResponseDto(poster, prfnm, prfcast));
//        }
//        log.info("poster = " + poster + "/ " + "prfnm = " + prfnm + "/ " + "prfcast = " + prfcast);
//
//        // Apply pagination
//        int startIndex = 0;
//        int endIndex = Math.min(pageSize, responseDtos.size());
//        if (startIndex < endIndex) {
//            responseDtos = responseDtos.subList(startIndex, endIndex);
//        }
//
//        return responseDtos;
//    }

    // 멀티스레드?
    @Transactional
    public List<BasePerformancesResponseDto> getAllPerformances(int pageSize) throws SQLException, IOException {
        List<String> performanceIds = xmlToMap.getPerformanceIdList();
        List<BasePerformancesResponseDto> responseDtos = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<Void>> tasks = new ArrayList<>();

        // Calculate the number of pages based on the pageSize
        int totalPages = (int) Math.ceil((double) performanceIds.size() / pageSize);

        for (int page = 0; page < totalPages; page++) {
            int startIndex = page * pageSize;
            int endIndex = Math.min(startIndex + pageSize, performanceIds.size());

            List<String> pagePerformanceIds = performanceIds.subList(startIndex, endIndex);

            tasks.add(() -> {
                for (String performanceId : pagePerformanceIds) {
                    StringBuilder response = new StringBuilder();
                    log.info("performanceId = " + performanceId);

                    try {
                        URL url = new URL("http://kopis.or.kr/openApi/restful/pblprfr/" + performanceId + "?service=60a3d3573c5e4d8bb052a4abebff27b6");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");

                        int responseCode = connection.getResponseCode();
                        log.info("Response Code: " + responseCode);

                        // Read the response
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // response = View detailed information xml of performance ID
                        log.info("Response XML:\n" + response.toString());

                        Document doc = Jsoup.parse(response.toString());
                        log.info("doc = " + doc.toString());

                        // select all data inside the data <db> element
                        Elements elements = doc.select("db > *");
                        log.info("elements = " + elements.toString());

                        String poster = elements.select("poster").text();
                        String prfnm = elements.select("prfnm").text();
                        String prfcast = elements.select("prfcast").text();

                        synchronized (responseDtos) {
                            responseDtos.add(new BasePerformancesResponseDto(poster, prfnm, prfcast));
                        }

                        log.info("Processed performanceId: " + performanceId);
                    } catch (IOException e) {
                        log.error("Error processing performanceId: " + performanceId, e);
                    }
                }
                return null;
            });
        }

        try {
            List<Future<Void>> futures = executor.invokeAll(tasks);
            for (Future<Void> future : futures) {
                // Wait for all tasks to complete
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error executing tasks", e);
            // Handle the error appropriately
        } finally {
            executor.shutdown();
        }
        return responseDtos;
    }



    // 상세 페이지
//    public DetailPerformanceResponseDto getDetailPerformance(Long performanceId) {
//
//        return null;
//    }

}
