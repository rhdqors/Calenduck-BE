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
    private Long id; // 유저 고유 번호

    @Column(/*nullable = false,*/ unique = true)
    private String nickName; // 카카오 사용자 이름 (필수 동의)

    @Column(unique = true)
    private Long kakaoId; // 카카오 고유 id

    @Column(unique = true)
    private String kakaoEmail; // 카카오 이메일 (선택 동의)

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    // 카카오 로그인

    public User(String nickName, Long kakaoId, String kakaoEmail, UserRoleEnum role) {
        this.nickName = nickName;
        this.kakaoId = kakaoId;
        this.kakaoEmail = kakaoEmail;
        this.role = role;
    }

    public User(KakaoUserInfoDto kakaoUserInfoDto, UserRoleEnum role) {
        this.kakaoId = kakaoUserInfoDto.getId();
        this.nickName = kakaoUserInfoDto.getNickname();
        this.kakaoEmail = kakaoUserInfoDto.getEmail();
        this.role = role;
    }

}
