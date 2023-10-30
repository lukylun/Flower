package com.kkoch.user.api.service.pointhistory.dto;

import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.pointhistory.PointHistory;
import lombok.Builder;
import lombok.Data;

@Data
public class AddPointHistoryDto {

    private String bank;
    private int amount;
    private int status;

    @Builder
    private AddPointHistoryDto(String bank, int amount, int status) {
        this.bank = bank;
        this.amount = amount;
        this.status = status;
    }

    public PointHistory toEntity(Member member) {
        return PointHistory.builder()
            .bank(this.bank)
            .amount(this.amount)
            .status(this.status)
            .member(member)
            .build();
    }
}
