package com.kkoch.admin.api.controller.plant.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlantNameResponse {

    private Long plantId;
    private String type;
    private String name;

    @Builder
    public PlantNameResponse(Long plantId, String type, String name) {
        this.plantId = plantId;
        this.type = type;
        this.name = name;
    }
}
