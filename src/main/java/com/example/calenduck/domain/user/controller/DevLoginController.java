package com.example.calenduck.domain.user.controller;

import com.example.calenduck.domain.user.dto.request.KakaoUserInfoDto;
import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.domain.user.entity.UserRoleEnum;
import com.example.calenduck.domain.user.repository.UserRepository;
import com.example.calenduck.global.jwt.JwtUtil;
import com.example.calenduck.global.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Profile("db-local")
@RestController
@RequiredArgsConstructor
public class DevLoginController {

    private static final Long DEV_KAKAO_ID = 99999L;
    private static final String DEV_NICKNAME = "dev-user";
    private static final String DEV_EMAIL = "dev@test.com";

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/dev/login")
    public ResponseEntity<?> devLogin(HttpServletResponse response) {
        User user = userRepository.findByKakaoId(DEV_KAKAO_ID)
                .orElseGet(() -> userRepository.save(
                        new User(
                                new KakaoUserInfoDto(DEV_KAKAO_ID, DEV_NICKNAME, DEV_EMAIL, null, null),
                                UserRoleEnum.USER
                        )
                ));

        String token = jwtUtil.createToken(user.getNickname(), user.getKakaoEmail(), user.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return ResponseMessage.SuccessResponse("Dev 로그인 성공", "");
    }
}
