package com.kkoch.admin.domain.plant.repository;

import com.kkoch.admin.domain.plant.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
}
