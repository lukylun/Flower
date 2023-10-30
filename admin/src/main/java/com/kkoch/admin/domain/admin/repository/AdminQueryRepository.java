package com.kkoch.admin.domain.admin.repository;

import com.kkoch.admin.api.controller.admin.LoginAdmin;
import com.kkoch.admin.api.controller.admin.response.AdminResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.kkoch.admin.domain.admin.QAdmin.admin;

@Repository
public class AdminQueryRepository {

    private final JPAQueryFactory queryFactory;

    public AdminQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<AdminResponse> getAdmins() {
        return queryFactory
            .select(Projections.constructor(AdminResponse.class,
                admin.id,
                admin.loginId,
                admin.loginPw,
                admin.name,
                admin.tel,
                admin.position,
                admin.createdDate,
                admin.active)
            )
            .from(admin)
            .orderBy(admin.name.asc())
            .fetch();
    }

    public Optional<LoginAdmin> getLoginAdmin(String loginId, String loginPw) {
        LoginAdmin content = queryFactory
            .select(Projections.constructor(LoginAdmin.class,
                admin.id,
                admin.position)
            )
            .from(admin)
            .where(
                admin.loginId.eq(loginId),
                admin.loginPw.eq(loginPw)
            )
            .fetchFirst();

        return Optional.ofNullable(content);
    }
}
