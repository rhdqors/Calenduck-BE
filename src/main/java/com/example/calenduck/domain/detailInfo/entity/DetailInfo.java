package com.example.calenduck.domain.detailInfo.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class DetailInfo {

    @Id
    private String mt20id;

    @Column
    private String mt10id;

    @Column
    private String prfnm;

    @Column
    private String fcltynm;

    @Column
    private String prfcast;

    @Column
    private String prfcrew;

    @Column
    private String prfruntime;

    @Column
    private String prfage;

    @Column
    private String entrpsnm;

    @Column
    private String pcseguidance;

    @Column
    private String genrenm;

}
