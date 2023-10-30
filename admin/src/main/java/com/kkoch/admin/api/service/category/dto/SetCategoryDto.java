package com.kkoch.admin.api.service.category.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SetCategoryDto {

    private String changeName;

    @Builder
    private SetCategoryDto(String changeName) {
        this.changeName = changeName;
    }
}
