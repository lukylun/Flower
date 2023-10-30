package com.kkoch.admin.api.service.stats;

import com.kkoch.admin.api.service.stats.dto.AuctionArticleForStatsDto;
import com.kkoch.admin.domain.Grade;
import com.kkoch.admin.domain.plant.Plant;
import com.kkoch.admin.domain.plant.Stats;
import com.kkoch.admin.domain.plant.repository.PlantRepository;
import com.kkoch.admin.domain.plant.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Transactional
@Service
public class StatsService {

    private final StatsRepository statsRepository;

    private final PlantRepository plantRepository;

    public void saveHistoryBidStats(List<AuctionArticleForStatsDto> dto) {
        // 경매품을 식물 별로 저장
        Map<Long, List<AuctionArticleForStatsDto>> articleByPlant = getAuctionArticleByPlant(dto);

        for (Map.Entry<Long, List<AuctionArticleForStatsDto>> entry : articleByPlant.entrySet()) {
            Long plantId = entry.getKey();
            List<AuctionArticleForStatsDto> plantList = entry.getValue();
            // 식물 당 등급별로 저장
            Map<Grade, List<AuctionArticleForStatsDto>> gradeDataMap = getAuctionArticleByGrade(plantList);

            saveStatsByGradeOfPlant(gradeDataMap, plantId);
        }
    }

    private Map<Long, List<AuctionArticleForStatsDto>> getAuctionArticleByPlant(List<AuctionArticleForStatsDto> dto) {
        Map<Long, List<AuctionArticleForStatsDto>> plantDataMap = new HashMap<>();
        for (AuctionArticleForStatsDto data : dto) {
            Long plantId = data.getPlantId();
            List<AuctionArticleForStatsDto> plantList = plantDataMap.getOrDefault(plantId, new ArrayList<>());
            plantList.add(data);
            plantDataMap.put(plantId, plantList);
        }
        return plantDataMap;
    }

    private Map<Grade, List<AuctionArticleForStatsDto>> getAuctionArticleByGrade(List<AuctionArticleForStatsDto> plantList) {
        Map<Grade, List<AuctionArticleForStatsDto>> gradeDataMap = new HashMap<>();
        for (AuctionArticleForStatsDto data : plantList) {
            Grade grade = data.getGrade();
            List<AuctionArticleForStatsDto> gradeList = gradeDataMap.getOrDefault(grade, new ArrayList<>());
            gradeList.add(data);
            gradeDataMap.put(grade, gradeList);
        }

        return gradeDataMap;
    }

    private void saveStatsByGradeOfPlant(Map<Grade, List<AuctionArticleForStatsDto>> gradeDataMap, Long plantId) {

        for (Map.Entry<Grade, List<AuctionArticleForStatsDto>> entry : gradeDataMap.entrySet()) {
            Grade grade = entry.getKey();
            List<AuctionArticleForStatsDto> gradeList = entry.getValue();

            int sum = 0;
            int totalGenus = 0;
            int count = 0;
            int maxBidPrice = Integer.MIN_VALUE;
            int minBidPrice = Integer.MAX_VALUE;

            for (AuctionArticleForStatsDto data : gradeList) {
                int bidPrice = data.getBidPrice();
                int bidGenus = data.getCount();
                sum += bidPrice;
                totalGenus += bidGenus;
                count++;
                maxBidPrice = Math.max(maxBidPrice, bidPrice);
                minBidPrice = Math.min(minBidPrice, bidPrice);
            }
            int avgBidPrice = sum / count;

            addStats(avgBidPrice, maxBidPrice, minBidPrice, grade, totalGenus, plantId);
        }
    }

    public void addStats(int avgBidPrice, int maxBidPrice, int minBidPrice, Grade grade, int totalGenus, Long plantId) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 식물입니다."));

        Stats stats = Stats.builder()
                .priceAvg(avgBidPrice)
                .priceMax(maxBidPrice)
                .priceMin(minBidPrice)
                .grade(grade)
                .count(totalGenus)
                .plant(plant)
                .build();

        statsRepository.save(stats);
    }
}
