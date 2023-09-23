package com.example.calenduck.domain.bookmark.Service;

import com.example.calenduck.domain.bookmark.dto.request.EditBookmarkRequestDto;
import com.example.calenduck.domain.bookmark.dto.response.BookmarkResponseDto;
import com.example.calenduck.domain.bookmark.dto.response.MyBookmarkResponseDto;
import com.example.calenduck.domain.user.entity.User;

import java.io.IOException;
import java.util.List;

public interface BookmarkBehavior {
    BookmarkResponseDto bookmark(String mt20id, String year, String month, String day, User user); // 북마크 성공/취소
    List<MyBookmarkResponseDto> getBookmarks(User user) throws IOException; // 북마크 전체 조회
    void editBookmark(String mt20id, String year, String month, String day, User user, EditBookmarkRequestDto editBookmarkRequestDto); // 북마크 상세 수정

}
