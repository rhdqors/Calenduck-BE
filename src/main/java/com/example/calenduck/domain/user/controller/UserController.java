package com.example.calenduck.domain.user.controller;

import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.domain.user.security.UserDetailsImpl;
import com.example.calenduck.domain.user.service.UserMyPage;
import com.example.calenduck.domain.user.service.UserService;
import com.example.calenduck.global.jwt.JwtUtil;
import com.example.calenduck.global.message.ResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserMyPage userMyPage;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인")
    @GetMapping("/kakao/login")
    public synchronized ResponseEntity<?> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        User user = userService.kakaoLogin(code, response);
        String createToken =  jwtUtil.createToken(user.getNickname(), user.getKakaoEmail(), user.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

        return ResponseMessage.SuccessResponse("로그인 성공", "");
    }

    @Operation(summary = "알람 전체 조회", description = "알람 전체 조회")
    @GetMapping("/alarm")
    public ResponseEntity<?> getAla인rms(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ExecutionException, InterruptedException {
        return ResponseMessage.SuccessResponse("알람 조회 성공", userMyPage.getAlarms(userDetails.getUser()));
    }


}

