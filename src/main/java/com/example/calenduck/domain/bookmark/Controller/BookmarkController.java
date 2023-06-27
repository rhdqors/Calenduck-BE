package com.example.calenduck.domain.bookmark.Controller;

import com.example.calenduck.domain.bookmark.Service.BookmarkService;
import com.example.calenduck.domain.bookmark.Service.BookmarkService;
import com.example.calenduck.domain.user.security.UserDetailsImpl;
import com.example.calenduck.global.exception.GlobalErrorCode;
import com.example.calenduck.global.exception.GlobalException;
import com.example.calenduck.global.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "찜목록 성공, 취소", description = "찜목록 성공, 취소")
    @PostMapping("/performances/{mt20id}/bookmark")
    public ResponseEntity<?> bookmark(@PathVariable("mt20id") String mt20id/*, @AuthenticationPrincipal UserDetailsImpl userDetails*/){
//        if (userDetails != null) {
//            message = bookmarkService.bookmark(mt20id, userDetails.getUser());
//        } else {
//            throw new GlobalException(GlobalErrorCode.INVALID_TOKEN);
//        }
        return ResponseMessage.SuccessResponse("완료", bookmarkService.bookmark(mt20id));
    }

    @Operation(summary = "찜목록 전체 조회", description = "찜목록 전체 조회")
    @GetMapping("/performances/bookmark")
    public ResponseEntity<?> getBookmarks(/*@AuthenticationPrincipal UserDetailsImpl userDetails*/) throws SQLException, IOException {
        return ResponseMessage.SuccessResponse("전체 조회 성공", bookmarkService.getBookmarks(/*userDetails.getUser()*/));
    }

}
