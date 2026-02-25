package com.example.calenduck.domain.user.service;

import com.example.calenduck.domain.user.dto.request.KakaoUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KakaoOAuthClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String clientId;
    private final String redirectUrl;

    public KakaoOAuthClient(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${kakao.client-id}") String clientId,
            @Value("${kakao.redirect_url}") String redirectUrl) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.clientId = clientId;
        this.redirectUrl = redirectUrl;
    }

    public String getToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_url", redirectUrl);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } catch (JsonProcessingException e) {
            log.error("getToken JSON processing error", e);
            throw e;
        }
    }

    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        try {
            String responseBody = response.getBody();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            Long id = jsonNode.get("id").asLong();
            String nickname = jsonNode.get("properties")
                    .get("nickname").asText();
            String email = jsonNode.get("kakao_account").has("email") ?
                    jsonNode.get("kakao_account").get("email").asText() : null;
            String gender = jsonNode.get("kakao_account").has("gender") ?
                    jsonNode.get("kakao_account").get("gender").asText() : null;
            String age = jsonNode.get("kakao_account").has("age") ?
                    jsonNode.get("kakao_account").get("age").asText() : null;

            log.info("카카오 로그인 처리: kakaoId={}", id);
            return new KakaoUserInfoDto(id, nickname, email, gender, age);
        } catch (JsonProcessingException e) {
            log.error("getKakaoUserInfo JSON processing error", e);
            throw e;
        }
    }
}
