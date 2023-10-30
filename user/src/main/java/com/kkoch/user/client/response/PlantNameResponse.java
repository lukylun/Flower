package com.kkoch.user.client.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlantNameResponse {

    private Long plantId;
    private String type;
    private String name;
}
