package com.example.calenduck.domain.performance.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@Component
@Slf4j
public class HttpRequest {

    @Value("${kopis.api.base-url}")
    private String baseUrl;

    @Value("${kopis.api.service-key}")
    private String serviceKey;

    @Value("${http.connect-timeout}")
    private int connectTimeout;

    @Value("${http.read-timeout}")
    private int readTimeout;

    public String requestExtraction(String mt20id) throws IOException {
        URL url = new URL(baseUrl + mt20id + "?service=" + serviceKey);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(connectTimeout);
        connection.setReadTimeout(readTimeout);

        int responseCode = connection.getResponseCode();
        log.info("KOPIS API 응답코드: {}, mt20id: {}", responseCode, mt20id);

        return readAndSaveRequest(connection);
    }

    private String readAndSaveRequest(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().collect(Collectors.joining());
        } finally {
            connection.disconnect();
        }
    }

}
