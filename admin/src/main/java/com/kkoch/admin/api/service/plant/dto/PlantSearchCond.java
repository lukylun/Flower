package com.kkoch.admin.api.service.plant.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PlantSearchCond {

    private String code;

    private String type;

    private String name;

    @Builder
    private PlantSearchCond(String code, String type, String name) {
        this.code = code;
        this.type = type;
        this.name = name;
    }
}
