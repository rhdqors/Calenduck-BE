package com.example.calenduck.domain.bookmark.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditBookmarkRequestDto {

    private String content;
    private String alarm;

}
