package com.kkoch.user.api.controller.pointhistory.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Data
@NoArgsConstructor
public class PointHistoryResponse {

    private int no;
    private String bank;
    private int amount;
    private int status;
    private String createdDate;

    @Builder
    public PointHistoryResponse(String bank, int amount, int status, LocalDateTime createdDate) {
        this.bank = bank;
        this.amount = amount;
        this.status = status;
        this.createdDate = createdDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }
}
