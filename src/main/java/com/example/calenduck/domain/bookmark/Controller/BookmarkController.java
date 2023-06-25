package com.example.calenduck.domain.bookmark.Controller;

import com.example.calenduck.domain.bookmark.Service.BookmarkService;
import com.example.calenduck.domain.user.security.UserDetailsImpl;
import com.example.calenduck.global.exception.GlobalErrorCode;
import com.example.calenduck.global.exception.GlobalException;
import com.example.calenduck.global.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "찜목록 성공, 취소", description = "찜목록 성공, 취소")
    @PostMapping("/performances/{mt20id}/bookmark")
    public ResponseEntity<?> bookmark(@PathVariable("mt20id") String mt20id/*, @AuthenticationPrincipal UserDetailsImpl userDetails*/){
        String message = "";
//        if (userDetails != null) {
//            message = bookmarkService.bookmark(mt20id, userDetails.getUser());
        message = bookmarkService.bookmark(mt20id);
//        } else {
//            throw new GlobalException(GlobalErrorCode.INVALID_TOKEN);
//        }
        return ResponseMessage.SuccessResponse(message, "");
    }
}
