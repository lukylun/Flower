package com.kkoch.admin.domain.trade.repository.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class TradeSearchCondTest {

    @DisplayName("현재 날짜와 기간을 입력하면 검색 시작일과 종료일을 알 수 있다.")
    @Test
    void calculationStartDateAndEndDate() {
        //given
        LocalDate currentDate = LocalDate.of(2023, 7, 10);

        //when
        TradeSearchCond cond = TradeSearchCond.of(currentDate, 7);

        //then
        assertThat(cond)
                .extracting("startDateTime", "endDateTime")
                .containsExactlyInAnyOrder(
                        LocalDate.of(2023, 7, 4).atStartOfDay(),
                        LocalDate.of(2023, 7, 11).atStartOfDay()
                );

    }
}