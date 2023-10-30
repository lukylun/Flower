package com.kkoch.user.domain.pointhistory;

import com.kkoch.user.domain.TimeBaseEntity;
import com.kkoch.user.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

/**
 * 회원 포인트 내역 엔티티
 * @author 임우택
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends TimeBaseEntity {

    @Id @GeneratedValue
    @Column(name = "point_history_id")
    private Long id;

    @Column(nullable = false, updatable = false, length = 100)
    private String bank;

    @Column(nullable = false, updatable = false)
    private int amount;

    @Column(nullable = false, updatable = false)
    private int status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private PointHistory(String bank, int amount, int status, Member member) {
        this.bank = bank;
        this.amount = amount;
        this.status = status;
        this.member = member;
    }
}
