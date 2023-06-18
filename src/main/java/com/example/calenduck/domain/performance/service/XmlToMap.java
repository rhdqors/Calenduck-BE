package com.example.calenduck.domain.performance.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class XmlToMap {
    // 1. mt20 id + kopis 인증키로 공연 상세정보 xml로 불러오기 -> 파싱으로 json 등으로 변환해야 함
    // 외부 request 요청
    // todo 현재 고정 ip로 값 불러옴 -> 반복문 등을 통해 id 돌려야할 듯
    public String detailPerformanceToXml() {
        StringBuilder response = new StringBuilder();
        try {
            // Create URL object with the API endpoint
            // 여러가지 보려면 PF134308의 mt20 id 반복문으로 돌려야 할듯 ?
            URL url = new URL("http://kopis.or.kr/openApi/restful/pblprfr/PF134308?service=60a3d3573c5e4d8bb052a4abebff27b6");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();
            log.info("Response Code: " + responseCode);

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // response = 공연id의 상세정보
            log.info("response = " + response);
            log.info("Response XML:\n" + response.toString());

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    // 2. xml 파싱 및 상세정보값 map에 키, 벨류 저장 -> 키값 입력해서 원하는 값 뽑아쓸 수 있음
    public Map<String, String> xmlToMap() {
        Map<String, String> keyValueMap = new HashMap<>();
        String xmlResponse = detailPerformanceToXml();

        // (html형식??)xml 문자열 xmlResponse -> document(문서) 객체로 파싱
        Document doc = Jsoup.parse(xmlResponse);
        log.info("doc = " + doc.toString());

        // 데이터 <db> 요소 안의 모든 데이터를 선택
        Elements elements = doc.select("db > *");
        log.info("elements = " + elements.toString());

        for (Element element : elements) {
            // 키값 - <괄호 안의 내용>
            String key = element.tagName();
            // 벨류값 <>괄호 사이의 값<>
            String value = element.text();
            // 키, 벨류 맵
            keyValueMap.put(key, value);
        }
        // 정보 하나만 조회시 .get 이용
        log.info("poster = " + keyValueMap.get("poster"));
        log.info(keyValueMap.toString());
        return keyValueMap;
    }


}
