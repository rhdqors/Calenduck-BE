package com.example.calenduck.domain.user.repository;

import com.example.calenduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(Long kakaoid);
    boolean existsByKakaoId(Long kakaoid);
    Optional<User> findByNickname(String nickname);

//    @Query(value = "INSERT INTO users (nickname, kakao_id, email, role) VALUES (?1, ?2, ?3, 'USER')", nativeQuery = true)
//    void saveUser(String nickName, Long kakaoid, String email, UserRoleEnum role);

}
