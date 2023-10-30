package com.kkoch.admin.api.controller.category.request;

import com.kkoch.admin.api.service.category.dto.SetCategoryDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class SetCategoryRequest {

    @NotEmpty
    private String changeName;

    @Builder
    private SetCategoryRequest(String changeName) {
        this.changeName = changeName;
    }

    public SetCategoryDto toSetCategoryDto() {
        return SetCategoryDto.builder()
                .changeName(this.changeName)
                .build();
    }
}
