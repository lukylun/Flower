package com.kkoch.admin;

import com.kkoch.admin.domain.plant.Category;
import com.kkoch.admin.domain.plant.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

//@Profile("local")
//@Component
@RequiredArgsConstructor
public class InitData {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        initCategory();
    }

    private void initCategory() {
        Category code = createCategory("절화", null);
        categoryRepository.save(code);

        Category type1 = createCategory("장미(스탠다드)", code);
        Category type2 = createCategory("소재", code);
        categoryRepository.saveAll(List.of(type1, type2));

        Category name1 = createCategory("하젤", type1);
        Category name2 = createCategory("도미니카", type1);
        Category name3 = createCategory("빅토리아", type1);
        Category name4 = createCategory("클라린스", type1);

        Category name5 = createCategory("보리사초", type2);
        Category name6 = createCategory("설유화", type2);
        Category name7 = createCategory("잎안개", type2);

        categoryRepository.saveAll(List.of(name1, name2, name3, name4, name5, name6, name7));
    }

    private Category createCategory(String name, Category category) {
        return Category.builder()
            .name(name)
            .level(category == null ? 1 : category.getLevel() + 1)
            .active(true)
            .parent(category)
            .build();
    }
}
