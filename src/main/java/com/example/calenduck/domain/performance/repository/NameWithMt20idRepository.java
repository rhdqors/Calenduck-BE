package com.example.calenduck.domain.performance.repository;

import com.example.calenduck.domain.performance.entity.NameWithMt20id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface NameWithMt20idRepository extends JpaRepository<NameWithMt20id, Long> {

    @Transactional
    @Query("SELECT n.mt20id FROM NameWithMt20id n ORDER BY n.mt20id ASC")
    List<String> findAllMt20idsOrdered();
    boolean existsByMt20id(String mt20id);
}
