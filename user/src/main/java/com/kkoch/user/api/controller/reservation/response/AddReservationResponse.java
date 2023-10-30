package com.kkoch.user.api.controller.reservation.response;

import com.kkoch.user.domain.reservation.Reservation;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class AddReservationResponse {

    private int count;
    private int price;
    private String grade;
    private String createdDate;

    @Builder
    private AddReservationResponse(int count, int price, String grade, LocalDateTime createdDate) {
        this.count = count;
        this.price = price;
        this.grade = grade;
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm"));
    }

    public static AddReservationResponse of(Reservation reservation) {
        return AddReservationResponse.builder()
            .count(reservation.getCount())
            .price(reservation.getPrice())
            .grade(reservation.getGrade().getText())
            .createdDate(reservation.getCreatedDate())
            .build();
    }
}
