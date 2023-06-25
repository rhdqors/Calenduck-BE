package com.example.calenduck.domain.performance.repository;

import com.example.calenduck.domain.performance.entity.NameWithMt20id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NameWithMt20idRepository extends JpaRepository<NameWithMt20id, Long> {

    List<NameWithMt20id> findAllByMt20id(String mt20id);
//    @Query("SELECT n FROM NameWithMt20id n WHERE n.mt20id = :mt20id")
//    Optional<NameWithMt20id> findByMt20id(@Param("mt20id") String mt20id);

}
