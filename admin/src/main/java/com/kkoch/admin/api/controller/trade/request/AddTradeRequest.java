package com.kkoch.admin.api.controller.trade.request;

import com.kkoch.admin.api.service.trade.dto.AddTradeDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AddTradeRequest {

    @NotEmpty
    private String memberKey;

    @NotNull
    private Long auctionArticleId;

    @NotNull
    private Integer price;

    @Builder
    private AddTradeRequest(String memberKey, Long auctionArticleId, Integer price) {
        this.memberKey = memberKey;
        this.auctionArticleId = auctionArticleId;
        this.price = price;
    }

    public AddTradeDto toAddTradeDto() {
        return AddTradeDto.builder()
                .memberKey(this.memberKey)
                .auctionArticleId(this.auctionArticleId)
                .price(this.price)
                .build();
    }

}
