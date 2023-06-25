package com.example.calenduck.domain.performance.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class NameWithMt20id {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String mt20id;

    public NameWithMt20id(Long id, String name, String mt20id) {
        this.id = id;
        this.name = name;
        this.mt20id = mt20id;
    }
}