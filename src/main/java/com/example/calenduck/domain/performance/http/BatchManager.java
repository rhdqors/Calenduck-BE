package com.example.calenduck.domain.performance.http;

import com.example.calenduck.domain.performance.repository.NameWithMt20idRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchManager {
    private final HttpRequest httpRequest;
    private final DataConversion dataConversion;
    private final NameWithMt20idRepository nameWithMt20idRepository;

    @Transactional
    public List<Elements> getElements() throws InterruptedException, ExecutionException {
        try{
            List<String> duplicateMt20ids = nameWithMt20idRepository.findAllMt20idsOrdered();
            List<String> uniqueMt20ids = saveUniqueMt20ids(duplicateMt20ids);

            return startBatches(uniqueMt20ids);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("스레드 중단됨", e);
            throw e;
        } catch(ExecutionException e) {
            log.error("실헹 에러 발생", e);
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

    public List<Elements> startBatches(List<String> mt20ids) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        int batchSize = 40;

        List<List<String>> batches = createBatches(mt20ids, batchSize);
        List<CompletableFuture<List<Elements>>> futures = processBatchesAsync(batches, executorService);
        waitForCompletion(futures);
        List<Elements> allElements = retrieveResults(futures);

        executorService.shutdown();
        return allElements;
    }

    private List<List<String>> createBatches(List<String> mt20ids, int batchSize) {
        List<List<String>> batches = new ArrayList<>();
        for (int i = 0; i < mt20ids.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, mt20ids.size());
            List<String> batch = mt20ids.subList(i, endIndex);
            batches.add(batch);
        }
        return batches;
    }

    private List<CompletableFuture<List<Elements>>> processBatchesAsync(List<List<String>> batches, ExecutorService executorService) {
        return batches.stream()
                .map(batch -> CompletableFuture.supplyAsync(() -> processBatch(batch), executorService))
                .collect(Collectors.toList());
    }

    private List<Elements> processBatch(List<String> batches) {
        List<Elements> batchElements = new ArrayList<>();

        for (String mt20id : batches) {
            try {
                String response = httpRequest.requestExtraction(mt20id);
                Elements elements = dataConversion.convertXml(response);

                batchElements.add(elements);
            } catch (IOException e) {
                log.error("mt20id(공연id)에 대한 데이터를 가져오며 에러 발생: " + mt20id, e);
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
    private List<Elements> retrieveResults(List<CompletableFuture<List<Elements>>> futures) throws ExecutionException, InterruptedException {
        List<Elements> allElements = new ArrayList<>();

        for (CompletableFuture<List<Elements>> future : futures) {
            List<Elements> batchElements = future.get();
            allElements.addAll(batchElements);
        }
        return allElements;
    }

}
