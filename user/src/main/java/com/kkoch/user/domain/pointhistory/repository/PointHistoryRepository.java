package com.kkoch.user.domain.pointhistory.repository;

import com.kkoch.user.domain.pointhistory.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PointHistory Data JPA Repository
 *
 * @author 임우택
 */
@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
