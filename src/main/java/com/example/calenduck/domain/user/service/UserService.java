package com.example.calenduck.domain.user.service;

import com.example.calenduck.domain.user.dto.request.KakaoUserInfoDto;
import com.example.calenduck.domain.user.entity.UserInfo;
import com.example.calenduck.domain.user.repository.UserInfoRepository;
import com.example.calenduck.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public UserInfo saveKakaoUserInfo(KakaoUserInfoDto kakaoUserInfoDto){
        return userInfoRepository.save(new UserInfo(kakaoUserInfoDto));
    }

}
