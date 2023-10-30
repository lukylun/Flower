package com.kkoch.admin.api.controller.category.response;

import com.kkoch.admin.domain.plant.Category;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CategoryForMemberResponse {

    private String type;
    private List<String> names;

    @Builder
    private CategoryForMemberResponse(String type, List<String> names) {
        this.type = type;
        this.names = names;
    }

    public static CategoryForMemberResponse of(Category category) {
        List<String> names = category.getChildren().stream()
            .map(Category::getName)
            .collect(Collectors.toList());

        return CategoryForMemberResponse.builder()
            .type(category.getName())
            .names(names)
            .build();
    }
}
