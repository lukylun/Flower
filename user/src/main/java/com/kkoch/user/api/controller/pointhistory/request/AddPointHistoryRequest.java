package com.kkoch.user.api.controller.pointhistory.request;

import com.kkoch.user.api.service.pointhistory.dto.AddPointHistoryDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddPointHistoryRequest {

    private String bank;
    private Integer amount;

    @Builder
    private AddPointHistoryRequest(String bank, Integer amount) {
        this.bank = bank;
        this.amount = amount;
    }

    public AddPointHistoryDto toAddPointHistoryDto(int status) {
        return AddPointHistoryDto.builder()
            .bank(this.bank)
            .amount(this.amount)
            .status(status)
            .build();
    }
}
