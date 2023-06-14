package com.example.calenduck.domain.user.controller;

import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.domain.user.service.KakaoService;
import com.example.calenduck.domain.user.service.UserService;
import com.example.calenduck.global.jwt.JwtUtil;
import com.example.calenduck.global.message.ResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @GetMapping("/users/kakao/login")
    public synchronized ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        User kakaoUser = kakaoService.kakaoLogin(code, response);
        String createToken =  jwtUtil.createToken(kakaoUser.getNickName(), kakaoUser.getKakaoEmail(), kakaoUser.getRole());

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

        return ResponseMessage.SuccessResponse("로그인 성공!", "");
    }

}
