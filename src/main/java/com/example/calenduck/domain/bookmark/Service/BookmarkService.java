package com.example.calenduck.domain.bookmark.Service;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import com.example.calenduck.domain.bookmark.Repository.BookmarkRepository;
import com.example.calenduck.domain.bookmark.dto.request.EditBookmarkRequestDto;
import com.example.calenduck.domain.bookmark.dto.response.BookmarkResponseDto;
import com.example.calenduck.domain.bookmark.dto.response.MyBookmarkResponseDto;
import com.example.calenduck.domain.performance.repository.NameWithMt20idRepository;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final NameWithMt20idRepository nameWithMt20idRepository;
    private final XmlToMap xmlToMap;
    private final EditBookmarkMapper editBookmarkMapper;

    // 북마크 성공/취소
    @Transactional
    public BookmarkResponseDto bookmark(String mt20id, String year, String month, String day, User user) {
        // 공연 확인
        if (!nameWithMt20idRepository.existsByMt20id(mt20id)) {
            throw new GlobalException(GlobalErrorCode.NOT_FOUND_PERFORMANCE);
        }
        if (year == null || month == null || day == null) {
            throw new GlobalException(GlobalErrorCode.NOT_VALID_DATE);
        }

        int yearValue = Integer.parseInt(year);
        int monthValue = Integer.parseInt(month);
        int dayValue = Integer.parseInt(day);

        String reservationDate = LocalDate.of(yearValue, monthValue, dayValue)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Bookmark bookmark = bookmarkRepository.findByUserAndMt20idAndReservationDate(user, mt20id, reservationDate);
        if (bookmark != null) {
            if (bookmark.getDeletedAt() == null) {
                bookmark.setDeletedAt(LocalDateTime.now());
                bookmark.setModifiedAt(LocalDateTime.now());
            } else {
                bookmark.setDeletedAt(null);
            }
        } else {
            bookmark = new Bookmark(mt20id, user, reservationDate);
            bookmark.setModifiedAt(LocalDateTime.now());
        }

        bookmarkRepository.saveAndFlush(bookmark);

        if (bookmark.getDeletedAt() != null) {
            LocalDateTime deletedAt = bookmark.getDeletedAt();
            return new BookmarkResponseDto("찜목록 취소", deletedAt);
        } else {
            LocalDateTime createdAt = bookmark.getCreatedAt();
            return new BookmarkResponseDto("찜목록 성공", createdAt, reservationDate);
        }
    }

    // 찜목록 전체 조회
    @Transactional
    public List<MyBookmarkResponseDto> getBookmarks(User user) throws SQLException, IOException {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
        log.info("bookmark.size ===== " + bookmarks.size());
        for (int i=0; i<bookmarks.size(); i++) {
            log.info("bookmark.getMt20id() == " + bookmarks.get(i).getMt20id() + " " + (i+1) + "번째 값임");
        }
        List<MyBookmarkResponseDto> myBookmarks = new ArrayList<>();

        for (Bookmark bookmark : bookmarks) {
        List<Elements> elements = xmlToMap.getBookmarkElements(bookmark.getMt20id());

            for (Elements element : elements) {
                log.info("element ====== " + element);
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

                if (bookmark.getMt20id().equals(mt20id)) {
                    MyBookmarkResponseDto bookmarkDto = new MyBookmarkResponseDto(
                            mt20id,
                            poster,
                            prfnm,
                            prfcast,
                            genrenm,
                            fcltynm,
                            dtguidance,
                            stdate,
                            eddate,
                            pcseguidance,
                            bookmark.getReservationDate(),
                            null,
                            null
                    );
                    myBookmarks.add(bookmarkDto);
                }
            }
        }
        return myBookmarks;
    }

    // 찜목록 상세 수정
    @Transactional
    public void editBookmark(String mt20id, String year, String month, String day, User user, EditBookmarkRequestDto editBookmarkRequestDto) {

        int yearValue = Integer.parseInt(year);
        int monthValue = Integer.parseInt(month);
        int dayValue = Integer.parseInt(day);

        String reservationDate = LocalDate.of(yearValue, monthValue, dayValue)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String[] alarm = editBookmarkRequestDto.getAlarm().split(",");
        LocalDate date = LocalDate.parse(reservationDate, DateTimeFormatter.BASIC_ISO_DATE);
        log.info("date == " + date);

        StringBuilder resultBuilder = new StringBuilder();

        for (String a : alarm) {
            log.info("alarm == " + a);
            LocalDate calculatedDate = date;

            if (a.equals("1일전")) {
                calculatedDate = calculatedDate.minusDays(1);
            } else if (a.equals("3일전")) {
                calculatedDate = calculatedDate.minusDays(3);
            } else if (a.equals("7일전")) {
                calculatedDate = calculatedDate.minusDays(7);
            }

            String formattedDate = calculatedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            log.info("formattedDate == " + formattedDate);

            if (resultBuilder.length() > 0) {
                resultBuilder.append(",");
            }
            resultBuilder.append(formattedDate);
        }

        String result = resultBuilder.toString();
        log.info("Result: " + result);
        editBookmarkRequestDto.setAlarm(result);
        log.info("editBookmarkRequestDto.setAlarm(result) == " + editBookmarkRequestDto.getAlarm());

        Bookmark bookmark = findByUserAndMt20idAndReservationDate(user, mt20id, reservationDate);
        if(bookmark == null) {
            throw new GlobalException(GlobalErrorCode.BOOKMARK_NOT_FOUND);
        }
        if(bookmark.getMt20id().equals(mt20id) && bookmark.getReservationDate().equals(reservationDate) && bookmark.getDeletedAt() != null) {
            throw new GlobalException(GlobalErrorCode.BOOKMARK_NOT_FOUND);
        }

        if (reservationDate.equals(bookmark.getReservationDate())) {
            log.info("bookmark.getReservationDate() = " + bookmark.getReservationDate());
            log.info("if 들어옴----");
            editBookmarkMapper.updateBookmarkFromDto(editBookmarkRequestDto, bookmark);
            bookmarkRepository.save(bookmark);
        } else {
            throw new GlobalException(GlobalErrorCode.NOT_VALID_DATE);
        }
    }

    public List<Bookmark> findBookmarks(User user) {
        return bookmarkRepository.findAllByUser(user);
    }


    public List<Bookmark> findBookmarksToId(String mt20id) {
        return bookmarkRepository.findAllByMt20id(mt20id);
    }


    public List<Bookmark> findBookmarks(String mt20id) {
        return bookmarkRepository.findAllByMt20id(mt20id);
    }

    public Bookmark findByUserAndMt20idAndReservationDate(User user, String mt20id, String reservationDate) {
        return bookmarkRepository.findByUserAndMt20idAndReservationDate(user, mt20id, reservationDate);
    }

}
