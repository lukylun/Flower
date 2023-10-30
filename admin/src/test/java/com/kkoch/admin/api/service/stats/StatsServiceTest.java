package com.kkoch.admin.api.service.stats;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.service.stats.dto.AuctionArticleForStatsDto;
import com.kkoch.admin.domain.Grade;
import com.kkoch.admin.domain.plant.Category;
import com.kkoch.admin.domain.plant.Plant;
import com.kkoch.admin.domain.plant.Stats;
import com.kkoch.admin.domain.plant.repository.CategoryRepository;
import com.kkoch.admin.domain.plant.repository.PlantRepository;
import com.kkoch.admin.domain.plant.repository.StatsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.groups.Tuple.tuple;

class StatsServiceTest extends IntegrationTestSupport {

    @Autowired
    StatsService statsService;

    @Autowired
    StatsRepository statsRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PlantRepository plantRepository;

    @DisplayName("전일 낙찰된 경매품의 평균 단가를 계산해 낙찰 통계에 저장한다.")
    @Test
    void addStats() throws Exception {
        //given
        Category code = createCategory("절화", null);
        Category name = createCategory("장미(스탠다드)", code);
        Category type1 = createCategory("하젤", name);
        Category type2 = createCategory("클레라", name);

        Plant plant1 = createPlant(code, name, type1);
        Plant plant2 = createPlant(code, name, type2);

        List<AuctionArticleForStatsDto> dto = createAuctionArticleList(plant1.getId(), plant2.getId());

        //when
        statsService.saveHistoryBidStats(dto);

        //then
        List<Stats> results = statsRepository.findAll();
        Assertions.assertThat(results).hasSize(5)
                .extracting("priceAvg", "priceMax", "priceMin", "grade", "count")
                .containsExactlyInAnyOrder(
                        tuple(1750, 2000, 1500, Grade.SUPER, 20),
                        tuple(1300, 1300, 1300, Grade.ADVANCED, 10),
                        tuple(1000, 1500, 500, Grade.NORMAL, 30),
                        tuple(5000, 5000, 5000, Grade.SUPER, 20),
                        tuple(3000, 3000, 3000, Grade.NORMAL, 10)
                );
    }

    private List<AuctionArticleForStatsDto> createAuctionArticleList(long plantId1, long plantId2) {
        List<AuctionArticleForStatsDto> lists = new ArrayList<>();
        lists.add(createDto(plantId1, Grade.ADVANCED, 1300, 10));
        lists.add(createDto(plantId1, Grade.SUPER, 2000, 10));
        lists.add(createDto(plantId1, Grade.SUPER, 1500, 10));
        lists.add(createDto(plantId1, Grade.NORMAL, 500, 10));
        lists.add(createDto(plantId1, Grade.NORMAL, 1000, 10));
        lists.add(createDto(plantId1, Grade.NORMAL, 1500, 10));
        lists.add(createDto(plantId2, Grade.SUPER, 5000, 10));
        lists.add(createDto(plantId2, Grade.SUPER, 5000, 10));
        lists.add(createDto(plantId2, Grade.NORMAL, 3000, 10));

        return lists;
    }

    private AuctionArticleForStatsDto createDto(Long plantId, Grade grade, int bidPrice, int count) {
        return AuctionArticleForStatsDto.builder()
                .plantId(plantId)
                .grade(grade)
                .bidPrice(bidPrice)
                .count(count)
                .build();
    }

    private Category createCategory(String name, Category parent) {
        Category category = Category.builder()
                .name(name)
                .active(true)
                .parent(parent)
                .build();
        return categoryRepository.save(category);
    }

    private Plant createPlant(Category code, Category name, Category type) {
        Plant plant = Plant.builder()
                .active(true)
                .code(code)
                .name(name)
                .type(type)
                .build();
        return plantRepository.save(plant);
    }

}