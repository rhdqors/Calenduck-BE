package com.example.calenduck.domain.bookmark.entity;

import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_mt20id", columnList = "mt20id"))
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mt20id;

    @Column
    private String content;

    @Column
    private String alarm;

    @Column(nullable = false)
    private String reservationDate;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Bookmark(String mt20id, User user, String reservationDate) {
        this.mt20id = mt20id;
        this.user = user;
        this.reservationDate = reservationDate;
    }

    public void updateContent(String content, String alarm) {
        this.content = content;
        this.alarm = alarm;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deletedAt = null;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
