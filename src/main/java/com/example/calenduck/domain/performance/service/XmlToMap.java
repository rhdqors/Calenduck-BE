package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import com.example.calenduck.domain.bookmark.Service.BookmarkService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class XmlToMap {

    private final EntityManager entityManager;
    private final BookmarkService bookmarkService;

    @Autowired
    public XmlToMap(EntityManager entityManager, @Lazy BookmarkService bookmarkService) {
        this.entityManager = entityManager;
        this.bookmarkService = bookmarkService;
    }

    // 2 + 3
    public List<String> getMt20idResultSet() {
        long startTime = System.currentTimeMillis();
        List<String> performanceIds = new ArrayList<>();
        Set<String> uniqueMt20ids = new HashSet<>();

        String jpql = "SELECT n.mt20id FROM NameWithMt20id n ORDER BY n.mt20id ASC";
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class);

        List<String> resultList = query.getResultList();
        log.info("-----------------------" + String.valueOf(resultList.size()));
        log.info("resultList = " + resultList);
        for (String performanceId : resultList) {
//                log.info("performanceId == " + performanceId);
            if (!uniqueMt20ids.contains(performanceId)) {
                uniqueMt20ids.add(performanceId);
                performanceIds.add(performanceId);
            }
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("getMt20idResultSet execution time: " + executionTime + "ms");

        return performanceIds;
    }

    @Transactional
    public List<Elements> getElements() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        List<String> performanceIds = getMt20idResultSet();
        List<Elements> elementsList = new ArrayList<>();

        // 스레드 풀 설정, 동시성 제어(한번에 40개)
        ExecutorService executorService = Executors.newFixedThreadPool(40);

        int batchSize = 50;
        List<List<String>> batches = createBatches(performanceIds, batchSize);
        List<CompletableFuture<List<Elements>>> futures = processBatchesAsync(batches, executorService);
        waitForCompletion(futures);
        retrieveResults(futures, elementsList);

        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("getElements execution time: " + executionTime + "ms");

        return elementsList;
    }

    // performanceId 배치 나눔
    private List<List<String>> createBatches(List<String> performanceIds, int batchSize) {
        List<List<String>> batches = new ArrayList<>();
        for (int i = 0; i < performanceIds.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, performanceIds.size());
            List<String> batch = performanceIds.subList(i, endIndex);
            batches.add(batch);
        }
        return batches;
    }

    // 배치 비동기 처리
    private List<CompletableFuture<List<Elements>>> processBatchesAsync(List<List<String>> batches, ExecutorService executorService) {
        return batches.stream()
                .map(batch -> CompletableFuture.supplyAsync(() -> processBatch(batch), executorService))
                .collect(Collectors.toList());
    }

    // 각 배치 처리
    private List<Elements> processBatch(List<String> batch) {
        List<Elements> batchElements = new ArrayList<>();
        for (String performanceId : batch) {
            StringBuilder response = new StringBuilder();
            try {
                // API 요청 및 데이터 추출
                URL url = new URL("http://kopis.or.kr/openApi/restful/pblprfr/" + performanceId + "?service=60a3d3573c5e4d8bb052a4abebff27b6");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                log.info("responseCode = " + responseCode);

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
//                log.info("response = " + response);

                Document doc = Jsoup.parse(response.toString());
//                log.info("doc = " + doc);
                Elements elements = doc.select("db > *");
//                log.info("elements = " + elements);
                batchElements.add(elements);
            } catch (IOException e) {
                log.error("Error occurred while fetching data for performanceId: " + performanceId, e);
            }
        }
        return batchElements;
    }

    // 모든 CompletableFuture 작업이 완료될 때까지 대기
    private void waitForCompletion(List<CompletableFuture<List<Elements>>> futures) {
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.join();
    }

    // 각 CompletableFuture의 결과를 최종 목록에 추가
    private void retrieveResults(List<CompletableFuture<List<Elements>>> futures, List<Elements> elementsList) throws ExecutionException, InterruptedException {
        for (CompletableFuture<List<Elements>> future : futures) {
            List<Elements> batchElements = future.get();
            elementsList.addAll(batchElements);
        }
    }

    // 찜목록 mt20id 상세정보 가져오기
    public List<Elements> getBookmarkElements(String mt20id) throws SQLException, IOException {
        List<String> performanceIds = new ArrayList<>();
        List<Bookmark> bookmarks = bookmarkService.findBookmarks(mt20id);
        for (Bookmark bookmark : bookmarks) {
            performanceIds.add(bookmark.getMt20id());
        }
        List<Elements> elements = new ArrayList<>();

        // --- url + id 조합으로 공연별 상세정보 조회 ---
        for (String performanceId : performanceIds) {
            log.info("performanceId = " + performanceId);

            StringBuilder response = new StringBuilder();
            URL url = new URL("http://kopis.or.kr/openApi/restful/pblprfr/" + performanceId + "?service=60a3d3573c5e4d8bb052a4abebff27b6");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            log.info("Response Code: " + responseCode);

            // Read the response
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            Document doc = Jsoup.parse(response.toString());
            elements.add(doc.select("db > *"));
        }
        return elements;
    }


    //    // 전체 mt20id 상세정보 불러오기
//    @Transactional
//    public List<Elements> getElements() throws InterruptedException, ExecutionException {
//        long startTime = System.currentTimeMillis();
//        List<String> performanceIds = getMt20idResultSet();
//        List<Elements> elementsList = new ArrayList<>();
//
//        // ThreadPool (동시성 설정 - 한번에 40개 실행)
//        ExecutorService executorService = Executors.newFixedThreadPool(40);
//
//        // 지정 크기 Batch로(50) 나누기 & 1배치 - performanceId 50개
//        int batchSize = 50;
//        List<List<String>> batches = new ArrayList<>();
//        for (int i = 0; i < performanceIds.size(); i += batchSize) {
//            int endIndex = Math.min(i + batchSize, performanceIds.size());
//            List<String> batch = performanceIds.subList(i, endIndex);
//            batches.add(batch);
//        }
//
//        List<CompletableFuture<List<Elements>>> futures = batches.stream()
//                // 각 배치별 비동기 처리 위한 CompletableFuture 생성.
//                .map(batch -> CompletableFuture.supplyAsync(() -> {
//                    List<Elements> batchElements = new ArrayList<>();
//                    // 배치 내부 id반복.
//                    for (String performanceId : batch) {
////                        log.info("performanceId = " + performanceId);
//                        StringBuilder response = new StringBuilder();
//                        // try catch api request & 데이터 추출
//                        try {
//                            URL url = new URL("http://kopis.or.kr/openApi/restful/pblprfr/" + performanceId + "?service=60a3d3573c5e4d8bb052a4abebff27b6");
//                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                            connection.setRequestMethod("GET");
//                            int responseCode = connection.getResponseCode();
//
//                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                                String line;
//                                while ((line = reader.readLine()) != null) {
//                                    response.append(line);
//                                }
//                            }
//                            log.info("response = " + response);
//
//                            Document doc = Jsoup.parse(response.toString());
//                            log.info("doc = " + doc);
//                            Elements elements = doc.select("db > *");
//                            log.info("elements = " + elements);
//                            batchElements.add(elements);
//                        } catch (IOException e) {
//                            // Handle any exceptions
//                            log.error("Error occurred while fetching data for performanceId: " + performanceId, e);
//                        }
//                    }
//                    return batchElements;
//                }, executorService))
//                .collect(Collectors.toList());
//
//        // 모든 CompletableFuture 작업이 완료될 때까지 대기.
//        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//        allFutures.join();
//
//        // 각 CompletableFuture의 결과를 검색하여 최종 목록에 추가.
//        for (CompletableFuture<List<Elements>> future : futures) {
//            List<Elements> batchElements = future.get();
//            elementsList.addAll(batchElements);
//        }
//        // ExecutorService 종료 및 리소스 해제.
//        executorService.shutdown();
//
//        long endTime = System.currentTimeMillis();
//        long executionTime = endTime - startTime;
//
//        log.info("getElements execution time: " + executionTime + "ms");
//
//        return elementsList;
//    }


}