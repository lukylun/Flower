package com.kkoch.admin.api.service.category.dto;

import com.kkoch.admin.domain.plant.Category;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddCategoryDto {

    private String name;

    private Long parentId;


    @Builder
    private AddCategoryDto(String name, Long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public Category toEntity() {
        return Category.builder()
                .name(this.name)
                .build();
    }
}
