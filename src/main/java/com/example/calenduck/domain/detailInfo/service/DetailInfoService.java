package com.example.calenduck.domain.detailInfo.service;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import com.example.calenduck.domain.bookmark.Service.BookmarkService;
import com.example.calenduck.domain.detailInfo.entity.DetailInfo;
import com.example.calenduck.domain.detailInfo.repository.DetailInfoRepository;
import com.example.calenduck.global.exception.GlobalErrorCode;
import com.example.calenduck.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetailInfoService {

    private final DetailInfoRepository detailInfoRepository;
    private final BookmarkService bookmarkService;

    public DetailInfo findDetailInfo(String mt20id) {
        List<Bookmark> bookmarks = bookmarkService.findBookmarksToId(mt20id);
        if (bookmarks.isEmpty()) {
            throw new GlobalException(GlobalErrorCode.BOOKMARK_NOT_FOUND);
        }
        Bookmark bookmark = bookmarks.get(0); // Assuming you want to retrieve the first bookmark

        return detailInfoRepository.findByMt20id(bookmark.getMt20id())
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.BOOKMARK_NOT_FOUND));
    }


}
