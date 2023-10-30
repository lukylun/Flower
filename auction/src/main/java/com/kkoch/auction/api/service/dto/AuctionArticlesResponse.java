package com.kkoch.auction.api.service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AuctionArticlesResponse implements Serializable {

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
    public AuctionArticlesResponse(Long auctionArticleId, String auctionNumber, Long plantId, String code, String type, String name, int count, int startPrice, String grade, String region, String shipper) {
        this.auctionArticleId = auctionArticleId;
        this.auctionNumber = auctionNumber;
        this.plantId = plantId;
        this.code = code;
        this.type = type;
        this.name = name;
        this.count = count;
        this.startPrice = startPrice;
        this.grade = grade;
        this.region = region;
        this.shipper = shipper;
    }

    public String toJson(String command) {
        return String.format("{" +
                "\"auctionArticleId\":%d," +
                "\"auctionNumber\":\"%s\"," +
                "\"plantId\":%d," +
                "\"code\":\"%s\"," +
                "\"type\":\"%s\"," +
                "\"name\":\"%s\"," +
                "\"count\":%d," +
                "\"startPrice\":%d," +
                "\"grade\":\"%s\"," +
                "\"region\":\"%s\"," +
                "\"shipper\":\"%s\"," +
                "\"command\":\"%s\"" +
                "}", this.auctionArticleId,
            this.auctionNumber,
            this.plantId,
            this.code,
            this.type,
            this.name,
            this.count,
            this.startPrice,
            this.grade,
            this.region,
            this.shipper,
            command
        );
    }

}
