package com.kkoch.user.domain.alarm.repository;

import com.kkoch.user.domain.alarm.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Alarm Data JPA Repository
 *
 * @author 임우택
 */
@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
