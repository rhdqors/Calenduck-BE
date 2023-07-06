package com.example.calenduck.domain.bookmark.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EditBookmarkRequestDto {

    private String content;
    private String alarm;

}
