package com.kkoch.user.domain.alarm.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.kkoch.user.domain.alarm.QAlarm.alarm;
import static com.kkoch.user.domain.member.QMember.*;

/**
 * Alarm bulk command Repository
 *
 * @author 임우택
 */
@Repository
public class AlarmCommandRepository {

    private final JPAQueryFactory queryFactory;

    public AlarmCommandRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 알림 열람 상태 벌크 업데이트
     *
     * @param memberKey 회원 고유키
     * @return 열람된 알림의 갯수
     */
    public int updateOpen(String memberKey) {
        List<Long> alarmIds = findAlarmIdByMemberKey(memberKey);

        return (int) queryFactory
            .update(alarm)
            .where(alarm.id.in(alarmIds))
            .set(alarm.open, true)
            .execute();
    }

    private List<Long> findAlarmIdByMemberKey(String memberKey) {
        return queryFactory
            .select(alarm.id)
            .from(alarm)
            .join(alarm.member, member)
            .where(
                alarm.open.isFalse(),
                member.memberKey.eq(memberKey)
            )
            .fetch();
    }

}
