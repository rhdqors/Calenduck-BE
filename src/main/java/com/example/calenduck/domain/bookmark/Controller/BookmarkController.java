package com.example.calenduck.domain.bookmark.Controller;

import com.example.calenduck.domain.bookmark.Service.BookmarkService;
import com.example.calenduck.domain.bookmark.dto.request.EditBookmarkRequestDto;
import com.example.calenduck.domain.user.security.UserDetailsImpl;
import com.example.calenduck.global.jwt.JwtUtil;
import com.example.calenduck.global.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "찜목록 성공, 취소", description = "찜목록 성공, 취소")
    @PostMapping("/performances/{mt20id}/bookmark/{year}/{month}/{day}")
    public ResponseEntity<?> bookmark(HttpServletRequest request, @PathVariable("mt20id") String mt20id,
                                      @PathVariable("year") String year,
                                      @PathVariable("month") String month,
                                      @PathVariable("day") String day, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("완료", bookmarkService.bookmark(mt20id, year, month, day, userDetails.getUser()));
    }

    @Operation(summary = "찜목록 전체 조회", description = "찜목록 전체 조회")
    @GetMapping("/performances/bookmark")
    public ResponseEntity<?> getBookmarks(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) throws SQLException, IOException {
        log.info(jwtUtil.resolveToken(request));
        return ResponseMessage.SuccessResponse("전체 조회 성공", bookmarkService.getBookmarks(userDetails.getUser()));
    }

    @Operation(summary = "찜목록 상세 수정", description = "찜목록 상세 수정")
    @PatchMapping("/performances/{mt20id}/bookmark/{year}/{month}/{day}")
    public ResponseEntity<?> editBookmark(HttpServletRequest request, @PathVariable("mt20id") String mt20id,
                                          @PathVariable String year,
                                          @PathVariable String month,
                                          @PathVariable String day, @AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody EditBookmarkRequestDto editBookmarkRequestDto){
        bookmarkService.editBookmark(mt20id, year, month, day, userDetails.getUser(), editBookmarkRequestDto);
        log.info(jwtUtil.resolveToken(request));
        return ResponseMessage.SuccessResponse("완료", "");
    }

}
