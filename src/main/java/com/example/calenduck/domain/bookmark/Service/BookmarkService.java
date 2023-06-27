package com.example.calenduck.domain.bookmark.Service;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import com.example.calenduck.domain.bookmark.Repository.BookmarkRepository;
import com.example.calenduck.domain.bookmark.dto.response.BookmarkResponseDto;
import com.example.calenduck.domain.bookmark.dto.response.MyBookmarkResponseDto;
import com.example.calenduck.domain.performance.entity.NameWithMt20id;
import com.example.calenduck.domain.performance.repository.NameWithMt20idRepository;
import com.example.calenduck.domain.performance.service.PerformanceService;
import com.example.calenduck.domain.performance.service.XmlToMap;
import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.global.exception.GlobalErrorCode;
import com.example.calenduck.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final NameWithMt20idRepository nameWithMt20idRepository;
    private final PerformanceService performanceService;
    private final XmlToMap xmlToMap;

    // 북마크 성공/취소
    @Transactional
    public BookmarkResponseDto bookmark(String mt20id/*, User user*/) {
        // 공연 있나 확인
        if (!nameWithMt20idRepository.existsByMt20id(mt20id)) {
            throw new GlobalException(GlobalErrorCode.NOT_FOUND_PERFORMANCE);
        }

//        if(bookmarkRepository.existsByUserAndMt20id(user, mt20id)) {
//            bookmarkRepository.deleteByUserAndMt20id(user, mt20id);
//            return "찜목록 취소";
//        } else {
//            bookmarkRepository.saveAndFlush(new Bookmark(mt20id, user));
//            return "찜목록 성공";
//        }
        if (bookmarkRepository.existsByMt20id(mt20id)) {
            bookmarkRepository.deleteByMt20id(mt20id);
            LocalDateTime deletedAt = LocalDateTime.now();
            return new BookmarkResponseDto("찜목록 취소", deletedAt);
        } else {
            bookmarkRepository.saveAndFlush(new Bookmark(mt20id));
            LocalDateTime createdAt = LocalDateTime.now();
            return new BookmarkResponseDto("찜목록 성공", createdAt);
        }
    }

    // 찜목록 전체 조회
    @Transactional
    public List<MyBookmarkResponseDto> getBookmarks(/*User user*/) throws SQLException, IOException {
//        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
        List<Elements> elements = xmlToMap.getElements();
        List<MyBookmarkResponseDto> myBookmarks = new ArrayList<>();
        log.info("----------elements = " + elements.toString());
        for (Elements element : elements) {
            String mt20id = element.select("mt20id").text();
            String poster = element.select("poster").text();
            String prfnm = element.select("prfnm").text();
            String prfcast = element.select("prfcast").text();
            String genrenm = element.select("genrenm").text();
            String fcltynm = element.select("fcltynm").text();
            String dtguidance = element.select("dtguidance").text();
            String stdate = element.select("prfpdfrom").text();
            String eddate = element.select("prfpdto").text();
            String pcseguidance = element.select("pcseguidance").text();

//            log.info("--------------------mt20id = " + mt20id + ", " + poster + ", " + prfnm + ", " + prfcast + ", " + genrenm + ", " +
//                    fcltynm + ", " + dtguidance + ", " + stdate + ", " + eddate + ", " + pcseguidance);

            List<Bookmark> bookmarks = bookmarkRepository.findAllByMt20id(mt20id);
            MyBookmarkResponseDto bookmarkDto = new MyBookmarkResponseDto();
//            log.info(bookmarks.);
            for (Bookmark bookmark : bookmarks) {
                log.info("----------asdfhasdkfhsadjfhasjdkf424237645wuearyatcsd = " + bookmark.getMt20id().equals(mt20id));
                if (bookmark.getMt20id().equals(mt20id)) {
                    bookmarkDto = new MyBookmarkResponseDto(
                            mt20id,
                            poster,
                            prfnm,
                            prfcast,
                            genrenm,
                            fcltynm,
                            dtguidance,
                            stdate,
                            eddate,
                            pcseguidance
                    );
                }
            }
            myBookmarks.add(bookmarkDto);
        }

//        bookmarkRepository.saveAll(bookmarks); // Save the updated bookmarks

        return myBookmarks;
    }



//    @Transactional
//    public List<MyBookmarkResponseDto> getBookmarks(/*User user*/) throws SQLException, IOException {
////        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
//        List<Elements> elements = xmlToMap.getElements();
//        List<MyBookmarkResponseDto> myBookmarks = new ArrayList<>();
//
//        for (Elements element : elements) {
//            String mt20id = element.select("mt20id").text();
//            String poster = element.select("poster").text();
//            String prfnm = element.select("prfnm").text();
//            String prfcast = element.select("prfcast").text();
//            String genrenm = element.select("genrenm").text();
//            String fcltynm = element.select("fcltynm").text();
//            String dtguidance = element.select("dtguidance").text();
//            String stdate = element.select("prfpdfrom").text();
//            String eddate = element.select("prfpdto").text();
//            String pcseguidance = element.select("pcseguidance").text();
//
//            log.info("--------------------mt20id = " + mt20id + ", " + poster + ", " + prfnm + ", " + prfcast + ", " + genrenm + ", " +
//                    fcltynm + ", " + dtguidance + ", " + stdate + ", " + eddate + ", " + pcseguidance);
//
//            List<Bookmark> bookmarks = bookmarkRepository.findAllByMt20id(mt20id);
//            MyBookmarkResponseDto bookmarkDto = new MyBookmarkResponseDto();
//
//            for (Bookmark bookmark : bookmarks) {
//                if (bookmark.getMt20id().equals(mt20id)) {
//                    bookmarkDto = new MyBookmarkResponseDto(
//                            mt20id,
//                            poster,
//                            prfnm,
//                            prfcast,
//                            genrenm,
//                            fcltynm,
//                            dtguidance,
//                            stdate,
//                            eddate,
//                            pcseguidance
//                    );
//                }
//            }
//            myBookmarks.add(bookmarkDto);
//        }
//
////        bookmarkRepository.saveAll(bookmarks); // Save the updated bookmarks
//
//        return myBookmarks;
//    }

}
