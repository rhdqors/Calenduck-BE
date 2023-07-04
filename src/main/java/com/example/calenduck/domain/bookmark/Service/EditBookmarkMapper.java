package com.example.calenduck.domain.bookmark.Service;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import com.example.calenduck.domain.bookmark.dto.request.EditBookmarkRequestDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EditBookmarkMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookmarkFromDto(EditBookmarkRequestDto editBookmarkRequestDto, @MappingTarget Bookmark bookmark);
}
