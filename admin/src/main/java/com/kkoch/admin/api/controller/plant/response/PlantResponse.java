package com.kkoch.admin.api.controller.plant.response;

import com.kkoch.admin.domain.plant.Plant;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Data
public class PlantResponse {

   private Long plantId;

   private String code;

   private String type;

   private String name;

   private String active;

   private String createDate;

    @Builder
    public PlantResponse(Long plantId, String code, String type, String name, String active, String createDate) {
        this.plantId = plantId;
        this.code = code;
        this.type = type;
        this.name = name;
        this.active = active;
        this.createDate = createDate;
    }

    public static PlantResponse of(Plant plant){
        String act = String.valueOf(plant.isActive());
        String creDate = plant.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm"));
        return PlantResponse.builder()
                .plantId(plant.getId())
                .code(plant.getCode().getName())
                .type(plant.getType().getName())
                .name(plant.getName().getName())
                .active(act)
                .createDate(creDate)
                .build();
    }
}
