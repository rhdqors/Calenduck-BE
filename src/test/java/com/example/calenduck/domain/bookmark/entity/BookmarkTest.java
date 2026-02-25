package com.example.calenduck.domain.bookmark.entity;

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
    @DisplayName("updateContent 호출 시 content와 alarm이 업데이트된다")
    void updateContent_withParams_updatesFields() {
        // Given
        User user = createTestUser();
        Bookmark bookmark = new Bookmark("PF12345", user, "20240101");

        // When
        bookmark.updateContent("테스트 메모", "1일전");

        // Then
        assertThat(bookmark.getContent()).isEqualTo("테스트 메모");
        assertThat(bookmark.getAlarm()).isEqualTo("1일전");
    }

    @Test
    @DisplayName("softDelete 호출 시 deletedAt이 설정된다")
    void softDelete_setsDeletedAt() {
        // Given
        User user = createTestUser();
        Bookmark bookmark = new Bookmark("PF12345", user, "20240101");

        // When
        bookmark.softDelete();

        // Then
        assertThat(bookmark.getDeletedAt()).isNotNull();
        assertThat(bookmark.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("restore 호출 시 deletedAt이 null로 초기화된다")
    void restore_clearsDeletedAt() {
        // Given
        User user = createTestUser();
        Bookmark bookmark = new Bookmark("PF12345", user, "20240101");
        bookmark.softDelete();

        // When
        bookmark.restore();

        // Then
        assertThat(bookmark.getDeletedAt()).isNull();
        assertThat(bookmark.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("삭제되지 않은 Bookmark의 isDeleted는 false다")
    void isDeleted_whenNotDeleted_returnsFalse() {
        // Given
        User user = createTestUser();
        Bookmark bookmark = new Bookmark("PF12345", user, "20240101");

        // When & Then
        assertThat(bookmark.isDeleted()).isFalse();
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
}
