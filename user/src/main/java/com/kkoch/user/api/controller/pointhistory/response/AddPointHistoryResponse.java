package com.kkoch.user.api.controller.pointhistory.response;

import com.kkoch.user.domain.pointhistory.PointHistory;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Data
public class AddPointHistoryResponse {

    private String bank;
    private int amount;
    private int status;
    private String createdDate;

    @Builder
    private AddPointHistoryResponse(String bank, int amount, int status, String createdDate) {
        this.bank = bank;
        this.amount = amount;
        this.status = status;
        this.createdDate = createdDate;
    }

    public static AddPointHistoryResponse of(PointHistory pointHistory) {
        return AddPointHistoryResponse.builder()
            .bank(pointHistory.getBank())
            .amount(pointHistory.getAmount())
            .status(pointHistory.getStatus())
            .createdDate(pointHistory.getCreatedDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)))
            .build();
    }
}
