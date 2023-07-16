package com.example.calenduck.domain.performance.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailPerformanceResponseDto {

    private String poster; // 포스터
    private String prfnm; // 공연명
    private String prfcast; // 공연출연진
    private String genrenm; // 공연 장르명
    private String fcltynm; // 공연시설명
    private String dtguidance; // 공연시간
    private String stdate; // 공연시작일자
    private String eddate; // 공연종료일자
    private String pcseguidance; // 티켓 가격

    public DetailPerformanceResponseDto(String poster, String prfnm, String prfcast, String genrenm, String fcltynm, String dtguidance, String stdate, String eddate, String pcseguidance) {
        this.poster = poster;
        this.prfnm = prfnm;
        this.prfcast = prfcast;
        this.genrenm = genrenm;
        this.fcltynm = fcltynm;
        this.dtguidance = dtguidance;
        this.stdate = stdate;
        this.eddate = eddate;
        this.pcseguidance = pcseguidance;
    }
}
