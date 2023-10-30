package com.kkoch.admin.domain.plant.repository.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StatsSearchCond {

    private String type;

    private String name;

    private int searchDay;

    @Builder
    public StatsSearchCond(String name, String type, int searchDay) {
        this.name = name;
        this.type = type;
        this.searchDay = searchDay;
    }
}
