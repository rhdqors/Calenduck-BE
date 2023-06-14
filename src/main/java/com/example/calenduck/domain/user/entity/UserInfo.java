package com.example.calenduck.domain.user.entity;

import com.example.calenduck.domain.user.dto.request.KakaoUserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String userAddress;

    @Column(nullable = true)
    private String userDetailAddress;

    // xxx : 카카오로그인 phone 번호 못 받아서 true로 바꿈니다 ㅠ
    @Column(nullable = true)
    private String userPhone;

    public UserInfo(KakaoUserInfoDto kakaoUserInfoDto) {

    }

}
