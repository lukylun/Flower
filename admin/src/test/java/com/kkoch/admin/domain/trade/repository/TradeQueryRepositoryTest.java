package com.kkoch.admin.domain.trade.repository;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.trade.response.TradeDetailResponse;
import com.kkoch.admin.api.controller.trade.response.TradeResponse;
import com.kkoch.admin.domain.trade.Trade;
import com.kkoch.admin.domain.trade.repository.dto.TradeSearchCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
class TradeQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private TradeQueryRepository tradeQueryRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @DisplayName("회원은 삭제되지 않은 낙찰 내역을 특정 기간으로 조회를 할 수 있다.")
    @Test
    void findByCondition() {
        //given
        LocalDate currentDate = LocalDate.of(2023, 7, 10);

        Trade trade1 = createTrade(LocalDate.of(2023, 7, 3).atStartOfDay(), true);
        Trade trade2 = createTrade(LocalDate.of(2023, 7, 4).atStartOfDay(), true);
        Trade trade3 = createTrade(LocalDate.of(2023, 7, 5).atStartOfDay(), false);
        Trade trade4 = createTrade(LocalDate.of(2023, 7, 10).atStartOfDay(), true);

        TradeSearchCond cond = TradeSearchCond.of(currentDate, 7);
        PageRequest pageRequest = PageRequest.of(0, 20);

        //when
        List<TradeResponse> responses = tradeQueryRepository.findByCondition("memberKey", cond, pageRequest);

        //then
        assertThat(responses).hasSize(2)
                .extracting("tradeDate", "count")
                .containsExactlyInAnyOrder(
                        tuple(trade2.getTradeTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), 0),
                        tuple(trade4.getTradeTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), 0)
                );
    }

    @DisplayName("회원의 삭제되지 않은 낙찰 내역중 조건을 만족하는 데이터의 수를 조회할 수 있다.")
    @Test
    void getTotalCount() {
        //given
        LocalDate currentDate = LocalDate.of(2023, 7, 10);

        Trade trade1 = createTrade(LocalDate.of(2023, 7, 3).atStartOfDay(), true);
        Trade trade2 = createTrade(LocalDate.of(2023, 7, 4).atStartOfDay(), true);
        Trade trade3 = createTrade(LocalDate.of(2023, 7, 5).atStartOfDay(), false);
        Trade trade4 = createTrade(LocalDate.of(2023, 7, 10).atStartOfDay(), true);

        TradeSearchCond cond = TradeSearchCond.of(currentDate, 7);

        //when
        int totalCount = tradeQueryRepository.getTotalCount("memberKey", cond);

        //then
        assertThat(totalCount).isEqualTo(2);
    }

    @DisplayName("회원은 본인의 낙찰 내역을 상세조회 할 수 있다.")
    @Test
    void findById() {
        //given
        LocalDateTime tradeDate = LocalDate.of(2023, 7, 10).atStartOfDay();
        Trade trade = createTrade(tradeDate, true);

        //when
        TradeDetailResponse response = tradeQueryRepository.findById(trade.getId());

        //then
        assertThat(response)
                .extracting("totalPrice", "tradeTime", "pickupStatus")
                .containsExactlyInAnyOrder(10_000, tradeDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm")), false);
    }

    private Trade createTrade(LocalDateTime tradeDate, boolean active) {
        Trade trade = Trade.builder()
                .totalPrice(10_000)
                .tradeTime(tradeDate)
                .pickupStatus(false)
                .active(active)
                .memberKey("memberKey")
                .articles(new ArrayList<>())
                .build();
        return tradeRepository.save(trade);
    }
}