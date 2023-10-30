package com.kkoch.admin.domain.plant.repository;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.plant.response.PlantResponse;
import com.kkoch.admin.domain.plant.Category;
import com.kkoch.admin.domain.plant.Plant;
import com.kkoch.admin.api.service.plant.dto.PlantSearchCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class PlantQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private PlantQueryRepository plantQueryRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Plant createPlant(Category code, Category type, Category name) {
        Plant plant = Plant.builder()
                .code(code)
                .type(type)
                .name(name)
                .active(true)
                .build();

        return plantRepository.save(plant);
    }

    private Category createCategory(String name) {
        Category category = Category.builder()
                .name(name)
                .level(1)
                .active(true)
                .build();

        return categoryRepository.save(category);
    }

}