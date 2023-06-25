package com.example.calenduck.domain.bookmark.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class myBookmarkResponseDto {

    private Long id;
    private String prfnm; // 공연명
    private String prfcast; // 공연출연진

    public myBookmarkResponseDto(Long id, String prfnm, String prfcast) {
        this.id = id;
        this.prfnm = prfnm;
        this.prfcast = prfcast;
    }
}
