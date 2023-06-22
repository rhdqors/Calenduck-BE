package com.example.calenduck.domain.performance.repository;

import com.example.calenduck.domain.performance.entity.Performance;
import com.example.calenduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

//    Optional<Performance> findByMt20id(String performanceId);
    boolean existsByUserAndMt20id(User user, String mt20id);
    void deleteByUserAndMt20id(User user, String mt20id);

    boolean existsByMt20id(String mt20id);
    void deleteByMt20id(String mt20id);

}
