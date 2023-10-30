package com.kkoch.user.domain.reservation;

import com.kkoch.user.domain.Grade;
import com.kkoch.user.domain.TimeBaseEntity;
import com.kkoch.user.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private int price;

    @Enumerated(STRING)
    @Column(nullable = false, length = 20)
    private Grade grade;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Long plantId;

    @Builder
    private Reservation(int count, int price, Grade grade, Member member, Long plantId) {
        this.count = count;
        this.price = price;
        this.grade = grade;
        this.member = member;
        this.plantId = plantId;
    }
}
