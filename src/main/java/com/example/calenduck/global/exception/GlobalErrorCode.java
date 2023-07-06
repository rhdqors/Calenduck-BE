package com.example.calenduck.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {
    // User
    // 400 BAD_REQUEST - 잘못된 요청
    NOT_VALID_EMAIL(BAD_REQUEST, "유효하지 않은 이메일 입니다."),
    DUPLICATE_NICK_NAME(BAD_REQUEST, "닉네임이 동일합니다."),
    // 401 Unauthorized - 권한 없음
    INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다"),
    // 404 Not Found - 찾을 수 없음
    EMAIL_NOT_FOUND(NOT_FOUND, "존재하지 않는 이메일 입니다."),
    USER_NOT_FOUND(NOT_FOUND, "등록된 사용자가 없습니다"),
    // 409
    DUPLICATE_USER(CONFLICT, "중복된 사용자 입니다"),

    // Performance
    NOT_FOUND_PERFORMANCE(NOT_FOUND, "등록된 공연이 없습니다"),

    // Bookmark
    // 400 BAD_REQUEST - 잘못된 요청
    NOT_VALID_DATE(BAD_REQUEST, "유효한 날짜를 선택하세요."),
    // 404 Not Found - 찾을 수 없음
    BOOKMARK_NOT_FOUND(NOT_FOUND, "찜목록이 존재하지 않습니다."),
    ALARM_NOT_FOUND(NOT_FOUND, "알람이 존재하지 않습니다."),

    // DetailInfo
    // 404 Not Found - 찾을 수 없음
    NOT_FOUND_DETAILINFO(NOT_FOUND, "등록된 DETAILINFO가 없습니다");


    private final HttpStatus httpStatus;
    private final String message;
}
