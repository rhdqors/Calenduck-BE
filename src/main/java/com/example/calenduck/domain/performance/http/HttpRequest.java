package com.example.calenduck.domain.performance.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpRequest {

    @Value("${request.first-url}")
    private String firstUrl;

    @Value("${request.last-url}")
    private String lastUrl;

    @Transactional
    public String requestExtraction(String mt20id) throws IOException {
        URL url = new URL(firstUrl + mt20id + lastUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        log.info("responseCode = " + responseCode);

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
