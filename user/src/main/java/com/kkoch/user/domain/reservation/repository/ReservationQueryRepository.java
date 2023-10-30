package com.kkoch.user.domain.reservation.repository;

import com.kkoch.user.api.controller.reservation.response.ReservationForAuctionResponse;
import com.kkoch.user.domain.reservation.Reservation;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.kkoch.user.domain.member.QMember.*;
import static com.kkoch.user.domain.reservation.QReservation.*;

@Repository
public class ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ReservationQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Reservation> findReservationByMemberKey(String memberKey, Pageable pageable) {
        List<Long> ids = queryFactory
            .select(reservation.id)
            .from(reservation)
            .join(reservation.member, member)
            .where(
                reservation.member.memberKey.eq(memberKey)
            )
            .orderBy(reservation.createdDate.desc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        return queryFactory
            .select(reservation)
            .from(reservation)
            .where(reservation.id.in(ids))
            .orderBy(reservation.createdDate.desc())
            .fetch();
    }

    public long getTotalCount(String memberKey) {
        return queryFactory
            .select(reservation.id)
            .from(reservation)
            .join(reservation.member, member)
            .where(
                reservation.member.memberKey.eq(memberKey)
            )
            .fetch()
            .size();
    }

    public ReservationForAuctionResponse findByPlantId(Long plantId) {
        return queryFactory
            .select(Projections.constructor(ReservationForAuctionResponse.class,
                reservation.member.memberKey,
                reservation.id,
                reservation.price
            ))
            .from(reservation)
            .where(reservation.plantId.eq(plantId))
            .orderBy(reservation.price.desc(), reservation.createdDate.asc())
            .fetchFirst();
    }
}
