package com.example.calenduck.domain.bookmark.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@NoArgsConstructor
public class BookmarkResponseDto {

    private String message;
    private String date;

    public BookmarkResponseDto(String message, LocalDateTime date) {
        this.message = message;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}







