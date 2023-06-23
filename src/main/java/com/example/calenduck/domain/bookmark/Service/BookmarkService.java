package com.example.calenduck.domain.bookmark.Service;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import com.example.calenduck.domain.bookmark.Repository.BookmarkRepository;
import com.example.calenduck.domain.performance.entity.Performance;
import com.example.calenduck.domain.performance.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PerformanceService performanceService;

    @Transactional
    public String bookmark(String mt20id/*, User user*/) {

//         공연이 있나 확인해야하는지?
        Performance performance = performanceService.getPerfornance(mt20id);

        String result = "찜목록 성공";
//        if(bookmarkRepository.existsByUserAndMt20id(user, mt20id)) {
//            bookmarkRepository.deleteByUserAndMt20id(user, mt20id);
//            result = "북마크 취소";
//        } else {
//            bookmarkRepository.saveAndFlush(new Bookmark(mt20id, user));
//        }
        if(bookmarkRepository.existsByMt20id(mt20id)) {
            bookmarkRepository.deleteByMt20id(mt20id);
            result = "찜목록 취소";
        } else {
            bookmarkRepository.saveAndFlush(new Bookmark(mt20id));
        }
        return result;
    }

}
