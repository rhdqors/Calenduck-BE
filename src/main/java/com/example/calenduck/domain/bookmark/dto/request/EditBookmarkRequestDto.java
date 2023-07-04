package com.example.calenduck.domain.bookmark.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditBookmarkRequestDto {

    private String content;
    private String alarm;

}
