package com.example.calenduck.domain.bookmark.Entity;

import com.example.calenduck.domain.performance.entity.Performance;
import com.example.calenduck.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String mt20id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Bookmark(String mt20id, User user) {
        this.mt20id = mt20id;
        this.user = user;
    }
    public Bookmark(String mt20id) {
        this.mt20id = mt20id;
    }

}
