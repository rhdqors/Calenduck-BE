//package com.example.calenduck.domain.user.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @InjectMocks
//    private UserService userService;
//
//    @Nested
//    @DisplayName("성공 케이스")
//    class SuccessCase {
//        @Test
//        @DisplayName("카카오 로그인")
//        void kakaoLogin() {
//
//        }
//
//        @Test
//        @DisplayName("인가 코드로 액세스 토큰 요청")
//        void getTokenTest() throws JsonProcessingException {
//            // Given
//            RestTemplate mockRestTemplate = mock(RestTemplate.class);
//
//            String code = "someCode";
//
//            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//            body.add("grant_type", "authorization_code");
//            body.add("client_id", "b0eb227d20bd3e34f8503571dbf24772");
//            body.add("redirect_uri", "http://localhost:3000/auth");
//            body.add("code", code);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
//
//            ResponseEntity<String> mockResponseEntity = new ResponseEntity<>(
//                    "{\"access_token\":\"someAccessToken\"}", HttpStatus.OK);
//
//            given(mockRestTemplate.exchange(
//                    "https://kauth.kakao.com/oauth/token",
//                    HttpMethod.POST,
//                    requestEntity,
//                    String.class)).willReturn(mockResponseEntity);
//
//            // When
//            String actualToken = userService.getToken(code);
//
//            // Then
//            assertEquals("someAccessToken", actualToken);
//        }
//
//
//        @Test
//        @DisplayName("액세스 토큰으로 카카오 사용자 정보 가져오기")
//        void getAlarms() {
//            HttpHeaders headers = new HttpHeaders();
//            String accessToken = "accessToken";
//            headers.add("Authorization", "Bearer " + accessToken);
//            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//            HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//            RestTemplate rt = new RestTemplate();
//            ResponseEntity<String> response = rt.exchange(
//                    "https://kapi.kakao.com/v2/user/me",
//                    HttpMethod.POST,
//                    kakaoUserInfoRequest,
//                    String.class
//            );
//
//        }
//
//    }// success
//}// class