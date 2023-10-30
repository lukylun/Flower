package com.kkoch.admin.api.controller.auction.request;

import com.kkoch.admin.api.service.auction.dto.AddAuctionArticleDto;
import com.kkoch.admin.domain.Grade;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AddAuctionArticleRequest {

    @NotNull
    private Long plantId;
    @NotNull
    private Long auctionId;
    @NotNull
    private Grade grade;
    @NotNull
    private Integer count;
    @NotBlank
    private String region;
    @NotBlank
    private String shipper;
    @NotNull
    private Integer startPrice;

    @Builder
    private AddAuctionArticleRequest(Long plantId, Long auctionId, Grade grade, Integer count, String region, String shipper, Integer startPrice) {
        this.plantId = plantId;
        this.auctionId = auctionId;
        this.grade = grade;
        this.count = count;
        this.region = region;
        this.shipper = shipper;
        this.startPrice = startPrice;
    }

    public AddAuctionArticleDto toAddAuctionArticleDto() {
        return AddAuctionArticleDto.builder()
                .grade(this.grade)
                .count(this.count)
                .region(this.region)
                .shipper(this.shipper)
                .startPrice(this.startPrice)
                .build();
    }
}
