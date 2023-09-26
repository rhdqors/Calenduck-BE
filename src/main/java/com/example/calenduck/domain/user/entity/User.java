package com.example.calenduck.domain.user.entity;

import com.example.calenduck.domain.user.dto.request.KakaoUserInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 유저 고유 번호

    @Column
    private String nickname; // 카카오 사용자 이름 (필수 동의)

    @Column(unique = true)
    private Long kakaoId; // 카카오 고유 id

    @Column(unique = true)
    private String kakaoEmail; // 카카오 이메일 (선택 동의)

    @Column
    private String gender;

    @Column
    private String age;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(KakaoUserInfoDto kakaoUserInfoDto, UserRoleEnum role) {
        this.kakaoId = kakaoUserInfoDto.getId();
        this.nickname = kakaoUserInfoDto.getNickname();
        this.kakaoEmail = kakaoUserInfoDto.getEmail();
        this.gender = kakaoUserInfoDto.getGender();
        this.age = kakaoUserInfoDto.getAge();
        this.role = role;
    }

}
