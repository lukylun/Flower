package com.kkoch.user.domain.member.repository;

import com.kkoch.user.api.controller.member.response.MemberInfoResponse;
import com.kkoch.user.api.controller.member.response.MemberResponseForAdmin;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.kkoch.user.domain.member.QMember.member;

/**
 * Member 조회용 Repository
 *
 * @author 임우택
 */
@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public MemberQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 회원 정보 조회
     *
     * @param memberKey 회원 고유키
     * @return 회원 정보 객체
     */
    public MemberInfoResponse findMyInfoByMemberKey(String memberKey) {
        return queryFactory
                .select(Projections.constructor(MemberInfoResponse.class,
                        member.email,
                        member.name,
                        member.tel,
                        member.businessNumber
                ))
                .from(member)
                .where(member.memberKey.eq(memberKey))
                .fetchFirst();
    }

    /**
     * 이메일 중복 조회
     *
     * @param email 검사할 이메일
     * @return 존재 하면 true
     */
    public Boolean existEmail(String email) {
        Integer content = queryFactory
                .selectOne()
                .from(member)
                .where(member.email.eq(email))
                .fetchFirst();

        return content != null;
    }

    public List<MemberResponseForAdmin> findAllUser() {
        return queryFactory
                .select(Projections.constructor(MemberResponseForAdmin.class,
                        member.id,
                        member.email,
                        member.name,
                        member.tel,
                        member.businessNumber,
                        member.point,
                        member.active
                ))
                .from(member)
                .fetch();
    }
}

