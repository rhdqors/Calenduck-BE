package com.example.calenduck.domain.bookmark.Entity;

import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.global.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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

    public Bookmark(String mt20id) {
        this.mt20id = mt20id;
    }
}
