package com.kkoch.admin.domain.trade.repository;

import com.kkoch.admin.domain.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    @Query("select t from Trade t where t.memberKey=:memberKey and t.createdDate >= :tradeDate")
    Optional<Trade> findByMemberKey(@Param("memberKey") String memberKey, @Param("tradeDate") LocalDateTime tradeDate);
}
