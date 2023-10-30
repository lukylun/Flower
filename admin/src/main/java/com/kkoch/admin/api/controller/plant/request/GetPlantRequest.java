package com.kkoch.admin.api.controller.plant.request;

import com.kkoch.admin.api.service.plant.dto.PlantSearchCond;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GetPlantRequest {

    private String code;

    private String type;

    private String name;

    @Builder
    public GetPlantRequest(String code, String type, String name) {
        this.code = code;
        this.type = type;
        this.name = name;
    }


    public PlantSearchCond toSearchCond() {
        return PlantSearchCond.builder()
                .code(this.code)
                .type(this.type)
                .name(this.name)
                .build();
    }
}
