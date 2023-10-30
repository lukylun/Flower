package com.kkoch.auction.api.service;


import com.kkoch.auction.IntegrationTestSupport;
import com.kkoch.auction.api.controller.request.EventParticipant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Transactional
class EventServiceTest extends IntegrationTestSupport {

    @Autowired
    private RedisService rankingService;

    @DisplayName("[레디스 테스트] 1등 테스트")
    @Test
    void joinEvent() {
        //given
        EventParticipant participant1 = new EventParticipant("member1", 23L, 10000);
        EventParticipant participant2 = new EventParticipant("member2", 23L, 10020);
        EventParticipant participant3 = new EventParticipant("member3", 23L, 9090);

        //when
        boolean result3 = rankingService.joinEvent(participant3);
        boolean result2 = rankingService.joinEvent(participant2);
        boolean result1 = rankingService.joinEvent(participant1);

        //then
        assertThat(result2).isFalse();
        assertThat(result1).isFalse();
        assertThat(result3).isTrue();
    }

    @DisplayName("[레디스 테스트]")
    @Test
    void joinEvent_여러명() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            String memberKry = i + "";
            Long auctionArticleId = (long) (i / 10);
            Integer price = i + 10000;

            executorService.submit(() -> {
                try {
                    rankingService.joinEvent(new EventParticipant(memberKry, auctionArticleId, price));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }
}