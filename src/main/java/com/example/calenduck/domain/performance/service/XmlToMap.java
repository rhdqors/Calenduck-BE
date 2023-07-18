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

    // 전체 mt20id 상세정보 불러오기
    @Transactional
    public List<Elements> getElements() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        List<String> performanceIds = getMt20idResultSet();
        List<Elements> elementsList = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(40);

        int batchSize = 50; // Set the desired batch size
        List<List<String>> batches = new ArrayList<>();
        for (int i = 0; i < performanceIds.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, performanceIds.size());
            List<String> batch = performanceIds.subList(i, endIndex);
            batches.add(batch);
        }

        List<CompletableFuture<List<Elements>>> futures = batches.stream()
                .map(batch -> CompletableFuture.supplyAsync(() -> {
                    List<Elements> batchElements = new ArrayList<>();
                    for (String performanceId : batch) {
                        log.info("performanceId = " + performanceId);
                        StringBuilder response = new StringBuilder();

                        try {
                            URL url = new URL("http://kopis.or.kr/openApi/restful/pblprfr/" + performanceId + "?service=60a3d3573c5e4d8bb052a4abebff27b6");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            int responseCode = connection.getResponseCode();

                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                            }

                            Document doc = Jsoup.parse(response.toString());
                            Elements elements = doc.select("db > *");
                            batchElements.add(elements);
                        } catch (IOException e) {
                            // Handle any exceptions
                            log.error("Error occurred while fetching data for performanceId: " + performanceId, e);
                        }
                    }
                    return batchElements;
                }, executorService))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        allFutures.join();

        for (CompletableFuture<List<Elements>> future : futures) {
            List<Elements> batchElements = future.get();
            elementsList.addAll(batchElements);
        }

        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        log.info("getElements execution time: " + executionTime + "ms");

        return elementsList;
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


}