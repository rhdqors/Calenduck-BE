package com.example.calenduck.domain.user.service;

import com.example.calenduck.domain.user.dto.request.KakaoUserInfoDto;
import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.domain.user.entity.UserRoleEnum;
import com.example.calenduck.domain.user.repository.UserRepository;
import com.example.calenduck.global.exception.GlobalErrorCode;
import com.example.calenduck.global.exception.GlobalException;
import com.example.calenduck.global.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserBehavior {

    private final UserRepository userRepository;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public String kakaoLogin(String code) {
        try {
            String accessToken = kakaoOAuthClient.getToken(code);
            KakaoUserInfoDto kakaoUserInfo = kakaoOAuthClient.getKakaoUserInfo(accessToken);
            signupIfNeeded(kakaoUserInfo);

            User user = userRepository.findByKakaoId(kakaoUserInfo.getId())
                    .orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));

            return jwtUtil.createToken(user.getNickname(), user.getKakaoEmail(), user.getRole());
        } catch (JsonProcessingException e) {
            log.error("kakaoLogin JSON processing error", e);
            throw new GlobalException(GlobalErrorCode.JSON_PROCESSING_ERROR);
        } catch (Exception e) {
            log.error("예상치 못한 오류가 발생했습니다.", e);
            throw new GlobalException(GlobalErrorCode.UNEXPECTED_ERROR);
        }
    }

    private void signupIfNeeded(KakaoUserInfoDto kakaoUserInfodto) {
        Long kakaoId = kakaoUserInfodto.getId();
        if (userRepository.existsByKakaoId(kakaoId)) return;

        userRepository.save(new User(kakaoUserInfodto, UserRoleEnum.USER));
    }
}
