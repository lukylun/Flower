package com.kkoch.admin.domain.plant.repository;

import com.kkoch.admin.domain.plant.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
}
