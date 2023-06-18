package com.example.calenduck.domain.performance.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BasePerformancesResponseDto {

    private String poster; // 포스터
    private String prfnm; // 공연명
    private String prfcast; // 공연출연진

    public BasePerformancesResponseDto(String poster, String prfnm, String prfcast) {
        this.poster = poster;
        this.prfnm = prfnm;
        this.prfcast = prfcast;
    }
}
