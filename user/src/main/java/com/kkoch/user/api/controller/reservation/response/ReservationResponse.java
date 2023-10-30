package com.kkoch.user.api.controller.reservation.response;

import com.kkoch.user.domain.reservation.Reservation;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationResponse {

    private String type;
    private String name;
    private String grade;
    private int count;
    private int price;

    @Builder
    public ReservationResponse(String type, String name, String grade, int count, int price) {
        this.type = type;
        this.name = name;
        this.grade = grade;
        this.count = count;
        this.price = price;
    }

    public static ReservationResponse of(Reservation reservation, String type, String name) {
        return ReservationResponse.builder()
            .type(type)
            .name(name)
            .grade(reservation.getGrade().getText())
            .count(reservation.getCount())
            .price(reservation.getPrice())
            .build();
    }
}
