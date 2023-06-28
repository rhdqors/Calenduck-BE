package com.example.calenduck.domain.performance.repository;

import com.example.calenduck.domain.performance.entity.NameWithMt20id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NameWithMt20idRepository extends JpaRepository<NameWithMt20id, Long> {

    List<NameWithMt20id> findAllByMt20id(String mt20id);
    Optional<NameWithMt20id> findByMt20id(String mt20id);

    boolean existsByMt20id(String mt20id);

}
