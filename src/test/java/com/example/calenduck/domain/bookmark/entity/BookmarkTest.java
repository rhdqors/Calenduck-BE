package com.example.calenduck.domain.bookmark.entity;

import com.example.calenduck.domain.bookmark.dto.request.EditBookmarkRequestDto;
import com.example.calenduck.domain.user.dto.request.KakaoUserInfoDto;
import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.domain.user.entity.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookmarkTest {

    private User createTestUser() {
        KakaoUserInfoDto kakaoInfo = new KakaoUserInfoDto(
                12345L, "testUser", "test@email.com", "male", "20~29"
        );
        return new User(kakaoInfo, UserRoleEnum.USER);
    }

    @Test
    @DisplayName("Bookmark 생성 시 mt20id, user, reservationDate가 설정된다")
    void create_withValidParams_setsFields() {
        // Given
        User user = createTestUser();

        // When
        Bookmark bookmark = new Bookmark("PF12345", user, "20240101");

        // Then
        assertThat(bookmark.getMt20id()).isEqualTo("PF12345");
        assertThat(bookmark.getUser()).isEqualTo(user);
        assertThat(bookmark.getReservationDate()).isEqualTo("20240101");
    }

    @Test
    @DisplayName("updateBookmark 호출 시 content와 alarm이 업데이트된다")
    void updateBookmark_withDto_updatesContentAndAlarm() {
        // Given
        User user = createTestUser();
        Bookmark bookmark = new Bookmark("PF12345", user, "20240101");
        EditBookmarkRequestDto dto = new EditBookmarkRequestDto();
        dto.setContent("테스트 메모");
        dto.setAlarm("1일전");

        // When
        bookmark.updateBookmark(dto);

        // Then
        assertThat(bookmark.getContent()).isEqualTo("테스트 메모");
        assertThat(bookmark.getAlarm()).isEqualTo("1일전");
    }

    @Test
    @DisplayName("Bookmark는 deletedAt 필드를 가진다")
    void bookmark_hasDeletedAtField() {
        // Given
        User user = createTestUser();
        Bookmark bookmark = new Bookmark("PF12345", user, "20240101");

        // When & Then
        assertThat(bookmark.getDeletedAt()).isNull();
    }

    @Test
    @DisplayName("Bookmark는 createdAt, modifiedAt 감사 필드를 가진다")
    void bookmark_hasAuditFields() {
        // Given
        User user = createTestUser();
        Bookmark bookmark = new Bookmark("PF12345", user, "20240101");

        // When & Then — JPA 컨텍스트 없이는 null이지만, 필드 존재를 확인
        assertThat(bookmark.getCreatedAt()).isNull();
        assertThat(bookmark.getModifiedAt()).isNull();
    }

    @Test
    @DisplayName("기본 생성자로 생성한 Bookmark의 모든 필드는 null이다")
    void create_withNoArgs_allFieldsNull() {
        // When
        Bookmark bookmark = new Bookmark();

        // Then
        assertThat(bookmark.getId()).isNull();
        assertThat(bookmark.getMt20id()).isNull();
        assertThat(bookmark.getContent()).isNull();
        assertThat(bookmark.getAlarm()).isNull();
        assertThat(bookmark.getUser()).isNull();
        assertThat(bookmark.getReservationDate()).isNull();
        assertThat(bookmark.getDeletedAt()).isNull();
    }
}
