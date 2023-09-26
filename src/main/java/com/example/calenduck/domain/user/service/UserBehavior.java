package com.example.calenduck.domain.user.service;

import com.example.calenduck.domain.user.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

// 외부에서 호출이 예상되는 메서드 집합
public interface UserBehavior {
    User kakaoLogin(String code, HttpServletResponse response); // 로그인
    List<String> getAlarms(User user); // 유저가 저장한 공연 자동 알림
}