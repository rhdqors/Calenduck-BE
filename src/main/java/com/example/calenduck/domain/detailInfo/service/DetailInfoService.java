package com.example.calenduck.domain.detailInfo.service;

import com.example.calenduck.domain.detailInfo.entity.DetailInfo;
import com.example.calenduck.domain.detailInfo.repository.DetailInfoRepository;
import com.example.calenduck.global.exception.GlobalErrorCode;
import com.example.calenduck.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetailInfoService {

    private final DetailInfoRepository detailInfoRepository;

    public DetailInfo findDetailInfo(String mt20id) {
        return detailInfoRepository.findByMt20id(mt20id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.NOT_FOUND_DETAILINFO));
    }
}
