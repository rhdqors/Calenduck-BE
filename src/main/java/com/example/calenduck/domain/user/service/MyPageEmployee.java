package com.example.calenduck.domain.user.service;

import com.example.calenduck.domain.user.entity.User;

import java.util.List;

public interface MyPageEmployee {
    List<String> getAlarms(User user); // 유저가 저장한 공연 자동 알림
}
