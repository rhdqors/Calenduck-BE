package com.example.calenduck.domain.user.repository;

import com.example.calenduck.domain.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
