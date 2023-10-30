package com.kkoch.admin.domain.plant.repository;

import com.kkoch.admin.domain.plant.Category;
import com.kkoch.admin.domain.plant.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.kkoch.admin.domain.plant.QCategory.category;

@Repository
public class CategoryQueryRepository {

    private final JPAQueryFactory queryFactory;

    public CategoryQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<String> getTypes(String codeName) {
        QCategory code = new QCategory("code");
        QCategory type = new QCategory("type");
        return queryFactory
            .select(type.name)
            .from(type)
            .join(type.parent, code)
            .where(
                type.active.isTrue(),
                code.name.eq(codeName)
            )
            .orderBy(type.createdDate.asc())
            .fetch();
    }

    public List<String> getNames(String codeName, String typeName) {
        QCategory code = new QCategory("code");
        QCategory type = new QCategory("type");
        QCategory name = new QCategory("name");

        return queryFactory
            .select(name.name)
            .from(name)
            .join(name.parent, type)
            .join(type.parent, code)
            .where(
                name.active.isTrue(),
                code.name.eq(codeName),
                type.name.eq(typeName)
            )
            .orderBy(name.createdDate.asc())
            .fetch();
    }

    public List<Category> getCategories(String parentName) {
        QCategory code = new QCategory("code");
        QCategory type = new QCategory("type");
        return queryFactory
            .select(type)
            .from(type)
            .join(type.parent, code).fetchJoin()
            .where(
                type.active.isTrue(),
                type.parent.name.eq(parentName)
            )
            .fetch();
    }

    public List<Category> getCategoriesByParentId(Long parentId) {
        return queryFactory.selectFrom(category)
                .where(category.parent.id.eq(parentId)
                        .and(category.active.isTrue()))
                .fetch();
    }

}
