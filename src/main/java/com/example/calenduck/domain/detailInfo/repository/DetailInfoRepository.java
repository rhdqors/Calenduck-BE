package com.example.calenduck.domain.detailInfo.repository;

import com.example.calenduck.domain.detailInfo.entity.DetailInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailInfoRepository extends JpaRepository<DetailInfo, Long> {

    Optional<DetailInfo> findByMt20id(String mt20id);
}
