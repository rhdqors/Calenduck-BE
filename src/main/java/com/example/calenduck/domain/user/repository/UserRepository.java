package com.example.calenduck.domain.user.repository;

import com.example.calenduck.domain.user.entity.KakaoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<KakaoUser, Long> {

    Optional<KakaoUser> findByKakaoId(Long kakaoId);

}
