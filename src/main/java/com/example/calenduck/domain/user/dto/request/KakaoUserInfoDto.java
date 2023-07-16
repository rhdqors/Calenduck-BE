package com.example.calenduck.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {

    private Long id;
    private String nickname;
    private String email;
    private String gender;
    private String age;

    public KakaoUserInfoDto(Long id, String nickname, String email, String gender, String age) {
        this.id = id;
        this.nickname = nickname;
        if(email != null) this.email = email;
        this.gender = gender;
        this.age = age;
    }

}