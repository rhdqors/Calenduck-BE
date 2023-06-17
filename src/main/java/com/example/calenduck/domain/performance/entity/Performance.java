package com.example.calenduck.domain.performance.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 공연 고유 번호

    @Column
    private String poster;

    @Column
    private String prfnm;

    @Column
    private String prfcast;

    @Column
    private String genrenm;

    @Column
    private String fcltynm;

    @Column
    private String dtguidance;

    @Column
    private String prfpdfrom;

    @Column
    private String prfpdto;

    @Column
    private String pcseguidance;

}
