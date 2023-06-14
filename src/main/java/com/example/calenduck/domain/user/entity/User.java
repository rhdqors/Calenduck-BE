package com.example.calenduck.domain.user.entity;

import com.example.calenduck.domain.user.dto.request.KakaoUserInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = true, unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private String kakaoEmail;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToOne
    @JoinColumn(name = "userinfo_id", nullable = false)
    private UserInfo userinfo;

    // 카카오 로그인
    public User(KakaoUserInfoDto kakaoUserInfoDto, UserInfo userInfo, UserRoleEnum role) {
        this.kakaoId = kakaoUserInfoDto.getId();
        this.nickName = kakaoUserInfoDto.getNickname();
        this.kakaoEmail = kakaoUserInfoDto.getEmail();
        this.userinfo = userInfo;
        this.role = role;
    }
    public User(Long kakaoId, String nickname, String email, UserInfo userInfo, UserRoleEnum role){
        this.kakaoId = kakaoId;
        this.nickName = nickname;
        if(email != null) this.kakaoEmail = email;
        this.userinfo = userInfo;
        this.role = role;
    }
}
