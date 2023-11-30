package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import com.example.calenduck.domain.bookmark.Service.BookmarkService;
import com.example.calenduck.domain.performance.repository.NameWithMt20idRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Component
public class XmlToMap implements XmlToMapBehavior {

    private final BookmarkService bookmarkService;
    private final NameWithMt20idRepository nameWithMt20idRepository;

    @Autowired
    public XmlToMap(@Lazy BookmarkService bookmarkService, NameWithMt20idRepository nameWithMt20idRepository) {
        this.bookmarkService = bookmarkService;
        this.nameWithMt20idRepository = nameWithMt20idRepository;
    }

    @Transactional
    @Override
    public List<String> queryForSaveMt20id() {
        List<String> duplicateMt20ids = nameWithMt20idRepository.findAllMt20idsOrdered();
        return saveUniqueMt20ids(duplicateMt20ids);
    }

    @Transactional
    @Override
    public List<Elements> getElements() throws InterruptedException, ExecutionException {
        try{
            long startTime = System.currentTimeMillis();
            List<String> mt20ids = queryForSaveMt20id();
            List<Elements> elementsList = new ArrayList<>();

            // 스레드 풀 설정, 동시성 제어(한번에 40개)
            ExecutorService executorService = Executors.newFixedThreadPool(50);

            int batchSize = 40;
            List<List<String>> batches = createBatches(mt20ids, batchSize);
            List<CompletableFuture<List<Elements>>> futures = processBatchesAsync(batches, executorService);
            waitForCompletion(futures);
            retrieveResults(futures, elementsList);

            executorService.shutdown();

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.info("getElements execution time: " + executionTime + "ms");

            return elementsList;
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("스레드 중단됨", e);
            throw e;
        } catch(ExecutionException e) {
            log.error("실헹 에러 발생", e);
            throw e;
        }
    }

    // 찜목록 mt20id 상세정보 가져오기
    @Transactional
    @Override
    public List<Elements> getBookmarkElements(String mt20id) throws IOException {
        try {
            List<String> mt20ids = new ArrayList<>();
            List<Bookmark> bookmarks = bookmarkService.findBookmarks(mt20id);
            for (Bookmark bookmark : bookmarks) {
                mt20ids.add(bookmark.getMt20id());
            }
            List<Elements> elements = new ArrayList<>();

            // --- url + id 조합으로 공연별 상세정보 조회 ---
            for (String performanceId : mt20ids) {
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
        } catch(IOException e) {
            log.error("I/O error 발생" + e);
            throw e;
        }
    }

    private List<String> saveUniqueMt20ids(List<String> duplicateMt20ids) {
        List<String> uniqueMt20ids = new ArrayList<>();
        Set<String> set = new HashSet<>();

        for (String duplicateMt20id : duplicateMt20ids) {
            if (!set.contains(duplicateMt20id)) {
                set.add(duplicateMt20id);
                uniqueMt20ids.add(duplicateMt20id);
            }
        }
        return uniqueMt20ids;
    }

    private List<List<String>> createBatches(List<String> performanceIds, int batchSize) {
        List<List<String>> batches = new ArrayList<>();
        for (int i = 0; i < performanceIds.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, performanceIds.size());
            List<String> batch = performanceIds.subList(i, endIndex);
            batches.add(batch);
        }
        return batches;
    }

    private List<CompletableFuture<List<Elements>>> processBatchesAsync(List<List<String>> batches, ExecutorService executorService) {
        return batches.stream()
                .map(batch -> CompletableFuture.supplyAsync(() -> processBatch(batch), executorService))
                .collect(Collectors.toList());
    }

    private List<Elements> processBatch(List<String> batch) {
        List<Elements> batchElements = new ArrayList<>();
        for (String performanceId : batch) {
            log.info("performanceId == " + performanceId);
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

}