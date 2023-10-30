package com.kkoch.admin.domain.trade;

import com.kkoch.admin.domain.TimeBaseEntity;
import com.kkoch.admin.domain.auction.AuctionArticle;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trade extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long id;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private LocalDateTime tradeTime;

    @Column(nullable = false)
    private boolean pickupStatus;

    @Column(nullable = false)
    private boolean active;

    private String memberKey;

    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuctionArticle> articles = new ArrayList<>();

    @Builder
    private Trade(int totalPrice, LocalDateTime tradeTime, boolean pickupStatus, boolean active, String memberKey, List<AuctionArticle> articles) {
        this.totalPrice = totalPrice;
        this.tradeTime = tradeTime;
        this.pickupStatus = pickupStatus;
        this.active = active;
        this.memberKey = memberKey;
        this.articles = articles;
    }

    //== 연관관계 편의 메서드 ==//
    public static Trade createTrade(int totalPrice, String memberKey, List<AuctionArticle> auctionArticles) {
        Trade trade = Trade.builder()
                .totalPrice(totalPrice)
                .tradeTime(LocalDateTime.now())
                .pickupStatus(false)
                .active(true)
                .memberKey(memberKey)
                .articles(auctionArticles)
                .build();

        auctionArticles.forEach(auctionArticle -> auctionArticle.updateTrade(trade));

        return trade;
    }

    //== 비즈니스 로직 ==//
    public void pickup() {
        if (this.pickupStatus) {
            throw new IllegalArgumentException("이미 픽업한 상품입니다.");
        }
        this.pickupStatus = true;
    }

    public void setTotalPrice(int price) {
        this.totalPrice += price;
    }

    public void addAuctionArticle(AuctionArticle auctionArticle) {
        articles.add(auctionArticle);
    }

    public void remove() {
        this.active = false;
    }
}
