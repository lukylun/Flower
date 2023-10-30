package com.kkoch.admin.api.service.auction.dto;

import com.kkoch.admin.domain.Grade;
import com.kkoch.admin.domain.auction.AuctionArticle;
import lombok.Builder;
import lombok.Data;

@Data
public class AddAuctionArticleDto {

    private Grade grade;
    private int count;
    private String region;
    private String shipper;
    private int startPrice;

    @Builder
    private AddAuctionArticleDto(Grade grade, int count, String region, String shipper, int startPrice) {
        this.grade = grade;
        this.count = count;
        this.region = region;
        this.shipper = shipper;
        this.startPrice = startPrice;
    }

}
