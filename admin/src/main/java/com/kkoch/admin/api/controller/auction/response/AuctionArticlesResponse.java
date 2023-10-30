package com.kkoch.admin.api.controller.auction.response;

import com.kkoch.admin.domain.Grade;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuctionArticlesResponse {

    private Long auctionArticleId;
    // 상장번호
    private String auctionNumber;

    // 식물
    private Long plantId;

    private String code;
    private String type;
    private String name;

    private int count;
    private int startPrice;
    private String grade;
    private String region;
    private String shipper;

    @Builder
    public AuctionArticlesResponse(Long auctionArticleId, String auctionNumber, Long plantId, String code, String type, String name, int count, int startPrice, Grade grade, String region, String shipper) {
        this.auctionArticleId = auctionArticleId;
        this.auctionNumber = auctionNumber;
        this.plantId = plantId;
        this.code = code;
        this.type = type;
        this.name = name;
        this.count = count;
        this.startPrice = startPrice;
        this.grade = grade.getText();
        this.region = region;
        this.shipper = shipper;
    }
}
