package com.example.calenduck.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoRequestDto {
    private Long id;
    private String email;
    private String nickname;
    // userPhone도 받아야함

    public UserInfoRequestDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        if(email != null) this.email = email;
    }
}
