package com.example.calenduck.domain.performance.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BasePerformancesResponseDto {

    private String mt20id; // 공연id
    private String poster; // 포스터
    private String prfnm; // 공연명
    private String prfcast; // 공연출연진

    public BasePerformancesResponseDto(String mt20id, String poster, String prfnm, String prfcast) {
        this.mt20id = mt20id;
        this.poster = poster;
        this.prfnm = prfnm;
        this.prfcast = prfcast;
    }
}