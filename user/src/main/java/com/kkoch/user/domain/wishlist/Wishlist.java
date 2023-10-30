package com.kkoch.user.domain.wishlist;

import com.kkoch.user.domain.TimeBaseEntity;
import com.kkoch.user.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Wishlist extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // TODO: 2023-07-26 연관관계 끊기 
    @Column(nullable = false)
    private Long auctionArticleId;

    @Builder
    private Wishlist(Member member, Long auctionArticleId) {
        this.member = member;
        this.auctionArticleId = auctionArticleId;
    }
}
