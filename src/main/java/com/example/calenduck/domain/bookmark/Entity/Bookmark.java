package com.example.calenduck.domain.bookmark.Entity;

import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.global.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE bookmark SET deleted_at = CONVERT_TZ(now(), 'UTC', 'Asia/Seoul') WHERE id = ?")
public class Bookmark extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mt20id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Bookmark(Long id, User user) {
        this.id = id;
        this.user = user;
    }

    public Bookmark(String mt20id, User user) {
        this.mt20id = mt20id;
        this.user = user;
    }
}
