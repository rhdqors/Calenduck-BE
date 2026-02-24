package com.example.calenduck.domain.bookmark.service;

import com.example.calenduck.domain.bookmark.entity.Bookmark;
import com.example.calenduck.domain.bookmark.dto.request.EditBookmarkRequestDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EditBookmarkMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookmarkFromDto(EditBookmarkRequestDto editBookmarkRequestDto, @MappingTarget Bookmark bookmark);
}
