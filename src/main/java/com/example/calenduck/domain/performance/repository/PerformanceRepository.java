package com.example.calenduck.domain.performance.repository;

import com.example.calenduck.domain.performance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {


}
