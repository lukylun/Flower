package com.kkoch.admin.api.service.stats;

import com.kkoch.admin.api.controller.stats.response.StatsResponse;
import com.kkoch.admin.api.service.stats.dto.AuctionArticleForStatsDto;
import com.kkoch.admin.domain.plant.repository.StatsQueryRepository;
import com.kkoch.admin.domain.plant.repository.dto.StatsSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StatsQueryService {

    private final StatsQueryRepository statsQueryRepository;

    public List<AuctionArticleForStatsDto> getAuctionList() {
        return statsQueryRepository.findAuctionArticleByTime();
    }

    public List<StatsResponse> getStatsByCond(StatsSearchCond cond){
        return statsQueryRepository.findByCond(cond);
    }
}
