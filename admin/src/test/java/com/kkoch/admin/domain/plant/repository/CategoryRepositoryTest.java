package com.kkoch.admin.domain.plant.repository;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.domain.plant.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
class CategoryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CategoryRepository categoryRepository;


    @DisplayName("현재 카테고리의 모든 하위 카테코리 조회")
    @Test
    void getSubCategories() throws Exception {
        //given
        Category parentCatgory = createRootCategory("절화");
        Category catgory1 = createCategory("장미", parentCatgory);
        Category catgory2 = createCategory("튤립", parentCatgory);
        Category category1_1 = createCategory("레드", catgory1);
        Category category1_2 = createCategory("블루", catgory1);

        //when
        List<Category> results = categoryRepository.findAllById(parentCatgory.getId());
        List<Category> subResults = categoryRepository.findAllById(catgory1.getId());

        //then
        Assertions.assertThat(results).hasSize(2)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        catgory1.getName(), catgory2.getName()
                );

        Assertions.assertThat(subResults).hasSize(2)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        category1_1.getName(), category1_2.getName()
                );
    }

    private Category createRootCategory(String name) {
        Category category = Category.builder()
                .name(name)
                .level(1)
                .active(true)
                .build();

        return categoryRepository.save(category);
    }

    private Category createCategory(String name, Category parent) {
        Category category = Category.builder()
                .name(name)
                .level(1)
                .parent(parent)
                .active(true)
                .build();

        return categoryRepository.save(category);
    }

}