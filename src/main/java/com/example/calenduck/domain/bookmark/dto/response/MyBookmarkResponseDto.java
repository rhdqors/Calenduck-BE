package com.example.calenduck.domain.bookmark.dto.response;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyBookmarkResponseDto {

    private String mt20id; // 공연id
    private String poster; // 포스터
    private String prfnm; // 공연명
    private String prfcast; // 공연출연진
    private String genrenm; // 공연 장르명
    private String fcltynm; // 공연시설명
    private String dtguidance; // 공연시간
    private String stdate; // 공연시작일자
    private String eddate; // 공연종료일자
    private String pcseguidance; // 티켓 가격
    private String reservationDate; // 찜목록에 저장한 예약 날짜

    public MyBookmarkResponseDto(String mt20id, String poster, String prfnm, String prfcast, String genrenm, String fcltynm, String dtguidance, String stdate, String eddate, String pcseguidance, String reservationDate) {
        this.mt20id = mt20id;
        this.poster = poster;
        this.prfnm = prfnm;
        this.prfcast = prfcast;
        this.genrenm = genrenm;
        this.fcltynm = fcltynm;
        this.dtguidance = dtguidance;
        this.stdate = stdate;
        this.eddate = eddate;
        this.pcseguidance = pcseguidance;
        this.reservationDate = reservationDate;
    }
}
