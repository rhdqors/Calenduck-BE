package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.BasePerformancesResponseDto;
import com.example.calenduck.domain.performance.dto.response.SearchRankResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceService implements PerformanceBehavior {

    private final XmlToMap xmlToMap;
    private final RedisTemplate<String, String> redisTemplate;
//    Redis에 데이터 저장 및 업데이트.
//    Redis에서 데이터 검색 및 가져오기.
//    Redis의 리스트, 집합, 해시 등과 같은 다양한 데이터 구조 사용.
//    Redis 트랜잭션 관리.
//    Pub/Sub 메시징 시스템을 사용하여 메시지 발행 및 구독.
    @Value("${server.url}")
    private String serverUrl;

    // 전체 조회 & 메인 & 검색
    @Override
    @Transactional
    // key 값은 현재 메서드를 나타내는 sple(Spring Expression Language)언어
    // 다시 현재 메서드가 호출되면 데이터를 다시 조회하는 것이 아닌 캐시된 데이터를 불러옴
    @Cacheable(value = "elementsCache", condition = "#prfnm == null and #prfcast == null", key = "#root.methodName")
    public List<BasePerformancesResponseDto> getAllPerformances(String prfnm, String prfcast) throws ExecutionException, InterruptedException {
        try {
            List<BasePerformancesResponseDto> performances = new ArrayList<>();
            List<Elements> elements = xmlToMap.getElements();

            String lowercasePrfnm = prfnm != null ? prfnm.toLowerCase() : null;
            String lowercasePrfcast = prfcast != null ? prfcast.toLowerCase() : null;

            for (Elements element : elements) {
                String mt20id = element.select("mt20id").text();
                String poster = element.select("poster").text();
                String prfnmElement = element.select("prfnm").text();
                String prfcastElement = element.select("prfcast").text();
                String genrenm = element.select("genrenm").text();
                String fcltynm = element.select("fcltynm").text();
                String dtguidance = element.select("dtguidance").text();
                String stdate = element.select("prfpdfrom").text();
                String eddate = element.select("prfpdto").text();
                String pcseguidance = element.select("pcseguidance").text();

                boolean matchPrfnm = lowercasePrfnm == null || (prfnmElement != null && prfnmElement.toLowerCase().contains(lowercasePrfnm));
                boolean matchPrfcast = lowercasePrfcast == null || (prfcastElement != null && prfcastElement.toLowerCase().contains(lowercasePrfcast));

                if (matchPrfnm && matchPrfcast) {
                    BasePerformancesResponseDto basePerformancesResponseDto = new BasePerformancesResponseDto(
                            mt20id,
                            poster,
                            prfnmElement,
                            prfcastElement,
                            genrenm,
                            fcltynm,
                            dtguidance,
                            stdate,
                            eddate,
                            pcseguidance
                    );
                    performances.add(basePerformancesResponseDto);
                }
            }

            // 인기 검색어
            if (prfnm != null) {
                updatePopularSearchTerm(prfnm);
            }
            if (prfcast != null) {
                updatePopularSearchTerm(prfcast);
            }

            return performances;
        } catch (ExecutionException e) {
            log.error("실헹 에러 발생", e);
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태를 다시 설정
            log.error("스레드 중단됨", e);
            throw e;
        }
    }
    // 인기 검색어 저장 - 파라미터(검색어)가 있다면 ? 키, 벨류값 넣고 1증가
    @Override
    public void updatePopularSearchTerm(String searchTerm) {
        redisTemplate.opsForZSet().incrementScore("rank", searchTerm, 1);
    }

    // 인기검색어 조회 리스트 1위~5위까지
    @Override
    public List<SearchRankResponseDto> searchRankList() {
        String key = "rank";
        // ZSetOperations 객체 생성
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        // score순으로 5개 보여줌
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 4);
        return typedTuples.stream().map(SearchRankResponseDto::convertToResponseRankingDto).collect(Collectors.toList());
    }

    // 인기도 - 지역별 장르
    @Override
    public JsonNode PopularityByGenreWithRegion() {
        String url = serverUrl + "/api/queries/3/results.json?api_key=DaPCdTfknrRmiIR6UVBG3eVgvwv9LhgB4WuGkCfC";
        JsonNode rowsNode = null;

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                String jsonResponse = response.toString();

                // Parse JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                JsonNode dataNode = jsonNode.path("query_result").path("data");
                rowsNode = dataNode.path("rows");

                for (JsonNode rowNode : rowsNode) {
                    log.info("rowNode == " + rowNode);
                    String area = rowNode.path("area_nm").asText();
                    String genre = rowNode.path("genre_nm").asText();
                    double value = rowNode.path("\uc608\ub9e4\uc728").asDouble();

                    log.info("area: " + area + ", genre: " + genre + ", value: " + value);
                }
                log.info("rowsNode == " + Arrays.toString(new JsonNode[]{rowsNode}));
            } else {
                log.error("HTTP 실패 코드: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            log.error("http 에러", e);
        }
        return rowsNode;
    }

    // 탑텐
    @Override
    public JsonNode topTen() {
        String url = serverUrl + "/api/queries/5/results.json?api_key=B15NuqHs3MxTpejr9OS9AwXyPxTL85naIxsQkKgT";
        JsonNode rowsNode = null;

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                String jsonResponse = response.toString();

                // Parse JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                JsonNode dataNode = jsonNode.path("query_result").path("data");
                rowsNode = dataNode.path("rows");

                System.out.println("rowsNode: " + rowsNode);
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            System.out.println("HTTP Error");
            e.printStackTrace();
        }
        return rowsNode;
    }

    // 지역별 인기 공연
    @Override
    public JsonNode PopularityByRegion() {
        String url = serverUrl + "/api/queries/1/results.json?api_key=X7GrpYLJCgnCAAP410I63YfeLifwKKaViKAxz7SE";
        JsonNode rowsNode = null;

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                String jsonResponse = response.toString();

                // Parse JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                JsonNode dataNode = jsonNode.path("query_result").path("data");
                rowsNode = dataNode.path("rows");

                log.info("rowsNode == " + Arrays.toString(new JsonNode[]{rowsNode}));
            } else {
                log.error("HTTP 실패 코드: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            log.error("http 에러", e);
        }
        return rowsNode;
    }

}