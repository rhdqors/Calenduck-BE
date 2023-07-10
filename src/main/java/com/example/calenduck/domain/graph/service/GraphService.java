package com.example.calenduck.domain.graph.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

@Service
@Slf4j
public class GraphService {

    public JsonNode PopularityByRegion() {
        String url = "http://3.35.194.120/api/queries/3/results.json?api_key=DaPCdTfknrRmiIR6UVBG3eVgvwv9LhgB4WuGkCfC";
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
}
