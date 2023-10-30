package com.kkoch.admin.api.service.category;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.category.response.CategoryResponse;
import com.kkoch.admin.domain.plant.Category;
import com.kkoch.admin.domain.plant.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@Transactional
class CategoryQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private CategoryQueryService categoryQueryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("카테고리를 선택시 하위 카테고리를 가져온다.")
    @Test
    void getCategories() throws Exception {
        //given
        Category parentCatgory = createRootCategory("절화");
        Category catgory1 = createCategory("장미", parentCatgory);
        Category catgory2 = createCategory("튤립", parentCatgory);
        //when

        List<CategoryResponse> results = categoryQueryService.getCategoriesByParentId(parentCatgory.getId());

        //then
        assertThat(results).hasSize(2)
                .extracting("name", "categoryId", "level")
                .containsExactlyInAnyOrder(
                        tuple(catgory1.getName(), catgory1.getId(), catgory1.getLevel()),
                        tuple(catgory2.getName(), catgory2.getId(), catgory2.getLevel())
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
