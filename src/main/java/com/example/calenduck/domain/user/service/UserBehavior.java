package com.example.calenduck.domain.user.service;

import com.example.calenduck.domain.user.entity.User;

import javax.servlet.http.HttpServletResponse;

public interface UserBehavior {
    User kakaoLogin(String code, HttpServletResponse response); // 로그인
}