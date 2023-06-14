package com.example.calenduck.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {

    USER_NOT_FOUND(NOT_FOUND, "등록된 사용자가 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
