package com.kkoch.admin.api.service.plant;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.plant.response.PlantResponse;
import com.kkoch.admin.domain.plant.Category;
import com.kkoch.admin.domain.plant.Plant;
import com.kkoch.admin.domain.plant.repository.CategoryRepository;
import com.kkoch.admin.domain.plant.repository.PlantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class PlantQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PlantRepository plantRepository;

    @Autowired
    private PlantQueryService plantQueryService;

    @DisplayName("관계자는 식물을 조회할 수 있다.")
    @Test
    void getPlants() throws Exception {
        //given
        Category code = createCategory("절화", null);
        Category type = createCategory("장미(스탠다드)", code);
        Category name1 = createCategory("하젤", type);
        Category name2 = createCategory("클레라", type);

        Plant plant1 = createPlant(code, type, name1);
        Plant plant2 = createPlant(code, type, name2);
        //when
        List<PlantResponse> responses = plantQueryService.getPlants();
        //then
        assertThat(responses).hasSize(2);

    }

    private Category createCategory(String name, Category parent) {
        Category category = Category.builder()
                .name(name)
                .active(true)
                .parent(parent)
                .build();
        return categoryRepository.save(category);
    }

    private Plant createPlant(Category code, Category name, Category type) {
        Plant plant = Plant.builder()
                .active(true)
                .code(code)
                .name(name)
                .type(type)
                .build();
        return plantRepository.save(plant);
    }

}