package com.kkoch.admin.domain.auction.repository;

import com.kkoch.admin.domain.auction.AuctionArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionArticleRepository extends JpaRepository<AuctionArticle, Long> {

    List<AuctionArticle> findByIdIn(List<Long> ids);
}
