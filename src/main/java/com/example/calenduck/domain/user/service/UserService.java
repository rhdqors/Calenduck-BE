package com.example.calenduck.domain.user.service;

import com.example.calenduck.domain.user.dto.request.KakaoUserInfoDto;
import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.domain.user.entity.UserRoleEnum;
import com.example.calenduck.domain.user.repository.UserRepository;
import com.example.calenduck.global.exception.GlobalErrorCode;
import com.example.calenduck.global.exception.GlobalException;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserBehavior {
    private final UserRepository userRepository;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect_url}")
    private String redirectUrl;

    @Transactional
    @Override
    public User kakaoLogin(String code, HttpServletResponse response) {
        try{
            // 1. "인가 코드"로 "액세스 토큰" 요청
            // 인가코드 -> 로그인 후 서비스제공자(카카오)로부터 받는 임시 코드
            // 인가코드는 일회성 그리고 짧은 시간내에 사용되어야함
            log.info("code : " + code);
            String accessToken = getToken(code);
            log.info("accessToken : " + accessToken);

            // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
            // 액세스 토큰 서비스 제공자(카카오) api 호출할 떄 사용하는 인증 수단
            // 액세스 토큰으로 추가 정보를 요청할 수 있고 이용자의 동의를 얻은 기능 실행 가능(친구목록, 메시지 전송, 프로필가져오기 등??)
            // 액세스 토큰 만료시 리프레시토큰으로 새로 발급
            KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
            log.info("kakaoUserInfo : " + kakaoUserInfo);
            // 3. 회원가입
            signupIfNeeded(kakaoUserInfo);

            // 4. JWT 토큰 반환
            // String createToken =  jwtUtil.createToken(kakaoUserInfo.getNickname(), kakaoUserInfo.getEmail(), UserRoleEnum.USER);
            // response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);
            // return createToken;package com.example.calenduck.domain.user.service;

            User user = userRepository.findByKakaoId(kakaoUserInfo.getId())
                    .orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));
            return user;
        } catch (JsonProcessingException e) {
            log.error("kakaoLogin JSON processing error", e);
            throw new GlobalException(GlobalErrorCode.JSON_PROCESSING_ERROR);
        } catch (Exception e) {
            log.error("예상치 못한 오류가 발생했습니다.", e);
            throw new GlobalException(GlobalErrorCode.UNEXPECTED_ERROR);
        }
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    private String getToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);

        body.add("redirect_url", redirectUrl);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } catch (JsonProcessingException e) {
            log.error("getToken JSON processing error", e);
            throw e;
        }
    }

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        log.info("response : " + response);

        try {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            Long id = jsonNode.get("id").asLong();
            String nickname = jsonNode.get("properties")
                    .get("nickname").asText();
            String email = jsonNode.get("kakao_account").has("email") ? jsonNode.get("kakao_account").get("email").asText() : null;
            String gender = jsonNode.get("kakao_account").has("gender") ? jsonNode.get("kakao_account").get("gender").asText() : null;
            String age = jsonNode.get("kakao_account").has("age") ? jsonNode.get("kakao_account").get("age").asText() : null;

            log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email + ", " +  gender + ", " + age);
            return new KakaoUserInfoDto(id, nickname, email, gender, age);
        } catch (JsonProcessingException e) {
            log.error("getKakaoUserInfo JSON processing error", e);
            throw e;
        }
    }

    // 3. 회원가입
    private void signupIfNeeded(KakaoUserInfoDto kakaoUserInfodto) {
        Long kakaoId = kakaoUserInfodto.getId();
        String nickName = kakaoUserInfodto.getNickname();
        String email = kakaoUserInfodto.getEmail() != null ? kakaoUserInfodto.getEmail() : null;

        log.info("카카오 사용자 정보: " + nickName);
        log.info("카카오 사용자 정보: " + email);
        log.info("카카오 사용자 정보: " + kakaoId);

        if (userRepository.existsByKakaoId(kakaoId)) {
            return;
        }
        userRepository.save(new User(kakaoUserInfodto, UserRoleEnum.USER));
//        userRepository.saveUser(nickName, kakaoId, email, UserRoleEnum.USER);
    }

}