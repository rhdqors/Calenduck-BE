package com.example.calenduck.domain.performance.entity;

import com.example.calenduck.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 공연 고유 번호

    @Column
    private String mt20id; // 공연id 상세정보 request로 꺼내 쓰면 될듯

    @ManyToOne
    @JoinColumn(name = "kakaouser_id"/*,nullable = false*/)
    private User user;

    public Performance(String mt20id, User user) {
        this.mt20id = mt20id;
        this.user = user;
    }
    public Performance(String mt20id) {
        this.mt20id = mt20id;
    }
}
