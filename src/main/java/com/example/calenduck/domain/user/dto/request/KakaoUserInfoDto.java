package com.example.calenduck.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {

    private Long id;
    private String email;
    private String nickname;
    private String gender;
    private String age;

    public KakaoUserInfoDto(Long id, String email, String nickname, String gender, String age) {
        this.id = id;
        if(email != null) this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
    }

}