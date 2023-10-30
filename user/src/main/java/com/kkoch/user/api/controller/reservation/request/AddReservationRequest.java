package com.kkoch.user.api.controller.reservation.request;

import com.kkoch.user.api.service.reservation.dto.AddReservationDto;
import com.kkoch.user.domain.Grade;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class AddReservationRequest {

    private String type;
    private String name;
    private Grade grade;
    private Integer count;
    private Integer price;

    @Builder
    private AddReservationRequest(String type, String name, Grade grade, Integer count, Integer price) {
        this.type = type;
        this.name = name;
        this.grade = grade;
        this.count = count;
        this.price = price;
    }

    public AddReservationDto toAddReservationDto() {
        return AddReservationDto.builder()
            .type(this.type)
            .name(this.name)
            .grade(this.grade)
            .count(this.count)
            .price(this.price)
            .build();
    }
}
