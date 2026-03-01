package com.example.calenduck.domain.bookmark.service;

import com.example.calenduck.domain.bookmark.dto.response.MyBookmarkResponseDto;
import com.example.calenduck.domain.bookmark.entity.Bookmark;
import com.example.calenduck.domain.bookmark.repository.BookmarkRepository;
import com.example.calenduck.domain.performance.http.DataConversion;
import com.example.calenduck.domain.performance.http.HttpRequest;
import com.example.calenduck.domain.performance.repository.NameWithMt20idRepository;
import com.example.calenduck.domain.user.dto.request.KakaoUserInfoDto;
import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.domain.user.entity.UserRoleEnum;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private NameWithMt20idRepository nameWithMt20idRepository;

    @Mock
    private HttpRequest httpRequest;

    @Mock
    private DataConversion dataConversion;

    private ThreadPoolTaskExecutor taskExecutor;
    private BookmarkService bookmarkService;

    private User testUser;

    @BeforeEach
    void setUp() {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setThreadNamePrefix("bookmark-test-");
        taskExecutor.initialize();

        bookmarkService = new BookmarkService(
                bookmarkRepository, nameWithMt20idRepository,
                httpRequest, dataConversion, taskExecutor
        );

        KakaoUserInfoDto kakaoInfo = new KakaoUserInfoDto(12345L, "testUser", "test@email.com", "male", "20~29");
        testUser = new User(kakaoInfo, UserRoleEnum.USER);
    }

    @Test
    @DisplayName("주입된 ThreadPoolTaskExecutor로 북마크 상세 병렬 조회가 정상 동작한다")
    void getBookmarks_withValidBookmarks_returnsDetails() throws Exception {
        // Given
        Bookmark bookmark1 = new Bookmark("PF001", testUser, "20240101");
        Bookmark bookmark2 = new Bookmark("PF002", testUser, "20240202");
        when(bookmarkRepository.findAllByUser(testUser)).thenReturn(Arrays.asList(bookmark1, bookmark2));

        String xml1 = "<db><mt20id>PF001</mt20id><poster>p1.jpg</poster><prfnm>공연1</prfnm>"
                + "<prfcast>배우1</prfcast><genrenm>뮤지컬</genrenm><fcltynm>극장1</fcltynm>"
                + "<dtguidance>19:30</dtguidance><prfpdfrom>2024.01.01</prfpdfrom>"
                + "<prfpdto>2024.03.01</prfpdto><pcseguidance>50000원</pcseguidance></db>";
        String xml2 = "<db><mt20id>PF002</mt20id><poster>p2.jpg</poster><prfnm>공연2</prfnm>"
                + "<prfcast>배우2</prfcast><genrenm>연극</genrenm><fcltynm>극장2</fcltynm>"
                + "<dtguidance>20:00</dtguidance><prfpdfrom>2024.02.01</prfpdfrom>"
                + "<prfpdto>2024.04.01</prfpdto><pcseguidance>40000원</pcseguidance></db>";

        Elements elements1 = Jsoup.parse(xml1).select("db > *");
        Elements elements2 = Jsoup.parse(xml2).select("db > *");

        when(httpRequest.requestExtraction("PF001")).thenReturn(xml1);
        when(httpRequest.requestExtraction("PF002")).thenReturn(xml2);
        when(dataConversion.convertXml(xml1)).thenReturn(elements1);
        when(dataConversion.convertXml(xml2)).thenReturn(elements2);

        // When
        List<MyBookmarkResponseDto> result = bookmarkService.getBookmarks(testUser);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("개별 API 실패 시 null 필터링되어 결과에서 제외된다")
    void getBookmarks_withPartialFailure_filtersNulls() throws Exception {
        // Given
        Bookmark bookmark1 = new Bookmark("PF001", testUser, "20240101");
        Bookmark bookmark2 = new Bookmark("PF002", testUser, "20240202");
        when(bookmarkRepository.findAllByUser(testUser)).thenReturn(Arrays.asList(bookmark1, bookmark2));

        String xml1 = "<db><mt20id>PF001</mt20id><poster>p1.jpg</poster><prfnm>공연1</prfnm>"
                + "<prfcast>배우1</prfcast><genrenm>뮤지컬</genrenm><fcltynm>극장1</fcltynm>"
                + "<dtguidance>19:30</dtguidance><prfpdfrom>2024.01.01</prfpdfrom>"
                + "<prfpdto>2024.03.01</prfpdto><pcseguidance>50000원</pcseguidance></db>";
        Elements elements1 = Jsoup.parse(xml1).select("db > *");

        when(httpRequest.requestExtraction("PF001")).thenReturn(xml1);
        when(httpRequest.requestExtraction("PF002")).thenThrow(new IOException("Connection refused"));
        when(dataConversion.convertXml(xml1)).thenReturn(elements1);

        // When
        List<MyBookmarkResponseDto> result = bookmarkService.getBookmarks(testUser);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMt20id()).isEqualTo("PF001");
    }

    @Test
    @DisplayName("빈 북마크 리스트일 때 빈 결과를 반환한다")
    void getBookmarks_withEmptyBookmarks_returnsEmptyList() throws Exception {
        // Given
        when(bookmarkRepository.findAllByUser(testUser)).thenReturn(Collections.emptyList());

        // When
        List<MyBookmarkResponseDto> result = bookmarkService.getBookmarks(testUser);

        // Then
        assertThat(result).isEmpty();
    }
}
