package com.kkoch.admin.domain.trade.repository.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TradeSearchCond {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private TradeSearchCond(LocalDate currentDate, int term) {
        this.startDateTime = currentDate.minusDays(term - 1).atStartOfDay();
        this.endDateTime = currentDate.plusDays(1).atStartOfDay();
    }

    public static TradeSearchCond of(LocalDate currentDate, int term) {
        return new TradeSearchCond(currentDate, term);
    }
}
