package com.example.calenduck.domain.performance.entity;

import com.example.calenduck.global.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class NameWithMt20id {

    @Column
    private String name;

    @Id
    @Column
    private String mt20id;

    public NameWithMt20id(String name, String mt20id) {
        this.name = name;
        this.mt20id = mt20id;
    }
}