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

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class XmlToMap implements XmlToMapBehavior {

    private final BookmarkService bookmarkService;

    @Autowired
    public XmlToMap(@Lazy BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

//    @Transactional
//    @Override
//    public List<String> queryForSaveMt20id() {
//        List<String> duplicateMt20ids = nameWithMt20idRepository.findAllMt20idsOrdered();
//        return saveUniqueMt20ids(duplicateMt20ids);
//    }
//
//    @Transactional
//    @Override
//    public List<Elements> getElements() throws InterruptedException, ExecutionException {
//        try{
//            List<String> duplicateMt20ids = nameWithMt20idRepository.findAllMt20idsOrdered();
//            List<String> uniqueMt20ids = saveUniqueMt20ids(duplicateMt20ids);
//
//            return batchManager.startBatches(uniqueMt20ids);
//        } catch(InterruptedException e) {
//            Thread.currentThread().interrupt();
//            log.error("스레드 중단됨", e);
//            throw e;
//        } catch(ExecutionException e) {
//            log.error("실헹 에러 발생", e);
//            throw e;
//        }
//    }

    // 찜목록 mt20id 상세정보 가져오기
    // TODO 찜 목록은 bookmarkService에서 처리해야할듯?
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

//    private List<String> saveUniqueMt20ids(List<String> duplicateMt20ids) {
//        List<String> uniqueMt20ids = new ArrayList<>();
//        Set<String> set = new HashSet<>();
//
//        for (String duplicateMt20id : duplicateMt20ids) {
//            if (!set.contains(duplicateMt20id)) {
//                set.add(duplicateMt20id);
//                uniqueMt20ids.add(duplicateMt20id);
//            }
//        }
//        return uniqueMt20ids;
//    }
//
//    private List<Elements> startBatches(List<String> mt20ids) throws ExecutionException, InterruptedException {
//        ExecutorService executorService = Executors.newFixedThreadPool(50);
//        int batchSize = 40;
//
//        List<List<String>> batches = createBatches(mt20ids, batchSize);
//        List<CompletableFuture<List<Elements>>> futures = processBatchesAsync(batches, executorService);
//        waitForCompletion(futures);
//        List<Elements> allElements = retrieveResults(futures);
//
//        executorService.shutdown();
//        return allElements;
//    }
//
//    private List<List<String>> createBatches(List<String> mt20ids, int batchSize) {
//        List<List<String>> batches = new ArrayList<>();
//        for (int i = 0; i < mt20ids.size(); i += batchSize) {
//            int endIndex = Math.min(i + batchSize, mt20ids.size());
//            List<String> batch = mt20ids.subList(i, endIndex);
//            batches.add(batch);
//        }
//        return batches;
//    }
//
//    private List<CompletableFuture<List<Elements>>> processBatchesAsync(List<List<String>> batches, ExecutorService executorService) {
//        return batches.stream()
//                .map(batch -> CompletableFuture.supplyAsync(() -> processBatch(batch), executorService))
//                .collect(Collectors.toList());
//    }
//
//    private List<Elements> processBatch(List<String> batches) {
//        List<Elements> batchElements = new ArrayList<>();
//
//        for (String performanceId : batches) {
//            log.info("performanceId == " + performanceId);
//            StringBuilder response = new StringBuilder();
//            try {
//                // API 요청 및 데이터 추출
//                URL url = new URL("http://kopis.or.kr/openApi/restful/pblprfr/" + performanceId + "?service=60a3d3573c5e4d8bb052a4abebff27b6");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                int responseCode = connection.getResponseCode();
//                log.info("responseCode = " + responseCode);
//
//                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                }
//
//                Document doc = Jsoup.parse(response.toString());
//                Elements elements = doc.select("db > *");
//                batchElements.add(elements);
//            } catch (IOException e) {
//                log.error("Error occurred while fetching data for performanceId: " + performanceId, e);
//            }
//        }
//        return batchElements;
//    }
//
//    // 모든 CompletableFuture 작업이 완료될 때까지 대기
//    private void waitForCompletion(List<CompletableFuture<List<Elements>>> futures) {
//        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//        allFutures.join();
//    }
//
//    // 각 CompletableFuture의 결과를 최종 목록에 추가
//    private List<Elements> retrieveResults(List<CompletableFuture<List<Elements>>> futures) throws ExecutionException, InterruptedException {
//        List<Elements> allElements = new ArrayList<>();
//
//        for (CompletableFuture<List<Elements>> future : futures) {
//            List<Elements> batchElements = future.get();
//            allElements.addAll(batchElements);
//        }
//        return allElements;
//    }

}