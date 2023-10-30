package com.kkoch.admin.domain.plant.repository;

import com.kkoch.admin.domain.plant.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "select category from Category category where category.parent.id = :id and category.active =true ")
    List<Category> findAllById(@Param("id") Long parentId);
}