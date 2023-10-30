package com.kkoch.user.api.service.reservation.dto;

import com.kkoch.user.domain.Grade;
import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.reservation.Reservation;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddReservationDto {

    private String type;
    private String name;
    private int count;
    private int price;
    private Grade grade;

    @Builder
    private AddReservationDto(String type, String name, int count, int price, Grade grade) {
        this.type = type;
        this.name = name;
        this.count = count;
        this.price = price;
        this.grade = grade;
    }

    public Reservation toEntity(Member member, Long plantId) {
        return Reservation.builder()
            .count(this.count)
            .price(this.price)
            .grade(this.grade)
            .member(member)
            .plantId(plantId)
            .build();
    }

}

