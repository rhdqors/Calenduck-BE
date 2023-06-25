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
    private String nickname; // 카카오 사용자 이름 (필수 동의)

    @Column(unique = true)
    private Long kakaoid; // 카카오 고유 id

    @Column(unique = true)
    private String kakaoemail; // 카카오 이메일 (선택 동의)

    @Column(unique = true)
    private String gender;

    @Column(unique = true)
    private String age;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(KakaoUserInfoDto kakaoUserInfoDto, UserRoleEnum role) {
        this.kakaoid = kakaoUserInfoDto.getId();
        this.nickname = kakaoUserInfoDto.getNickname();
        this.kakaoemail = kakaoUserInfoDto.getEmail();
        this.gender = kakaoUserInfoDto.getGender();
        this.age = kakaoUserInfoDto.getAge();
        this.role = role;
    }

}
