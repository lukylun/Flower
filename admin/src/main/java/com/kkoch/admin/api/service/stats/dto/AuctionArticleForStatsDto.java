package com.kkoch.admin.api.service.stats.dto;

import com.kkoch.admin.domain.Grade;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class AuctionArticleForStatsDto {

    private Long plantId;

    private Grade grade;

    private int count;

    private int bidPrice;


    @Builder
    public AuctionArticleForStatsDto(Long plantId, Grade grade, int count, int bidPrice) {
        this.plantId = plantId;
        this.grade = grade;
        this.count = count;
        this.bidPrice = bidPrice;
    }
}
