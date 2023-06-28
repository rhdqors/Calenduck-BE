package com.example.calenduck.domain.bookmark.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class BookmarkResponseDto {

    private String message;
    private String date;
    private String reservationDate;

    // 찜목록 취소
    public BookmarkResponseDto(String message, LocalDateTime date) {
        this.message = message;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // 찜목록 성공
    public BookmarkResponseDto(String message, LocalDateTime date, String reservationDate) {
        this.message = message;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.reservationDate = reservationDate;
    }
}







