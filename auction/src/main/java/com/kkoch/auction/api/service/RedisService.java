package com.kkoch.auction.api.service;

import com.kkoch.auction.api.controller.request.EventParticipant;
import com.kkoch.auction.api.service.dto.AuctionArticlesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long EXPIRE_TIME_SECONDS = 60; // 유효 기간을 1분으로 설정
    private static final long USER_NUMBERS_EXPIRE_TIME_SECONDS = 600; // 유효 기간을 10분으로 설정
    private static final String USER_NUMBERS = "userNumbers";
    private static final String AUCTION_LIST = "auctionList";

    public void insertList(List<AuctionArticlesResponse> articles) {
//        ObjectOutputStream out = new ObjectOutputStream();
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        for (AuctionArticlesResponse article : articles) {
            byte[] serialize = serializer.serialize(article);
//            log.info("직렬화 된 byte[] = {}", serialize);
            String serializeArticle = Base64.getEncoder().encodeToString(serialize);
//            log.info("직렬화 된 스트링 넣기 전= {}", serializeArticle);
            redisTemplate.opsForList().rightPush(AUCTION_LIST, serializeArticle);
        }
    }

    public AuctionArticlesResponse getNextArticle() {
        String serialize = redisTemplate.opsForList().leftPop(AUCTION_LIST);
//        log.info("꺼낸 직렬화 된 스트링 = {}", serialize);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        byte[] decode = Base64.getDecoder().decode(serialize);
//        log.info("디코딩 된 byte[] = {}", decode);
        AuctionArticlesResponse article = serializer.deserialize(decode, AuctionArticlesResponse.class);
//        Object deserialize = serializer.deserialize(serialize.getBytes());
//        AuctionArticlesResponse article = null;
//        if (deserialize instanceof AuctionArticlesResponse) {
//            log.info("역직렬화 가능");
//            article = (AuctionArticlesResponse) deserialize;
//        }
//        AuctionArticlesResponse article = (AuctionArticlesResponse) serializer.deserialize(serialize.getBytes());
        return article;
    }

    public int getNumber(String memberKey) {
        HashOperations<String, String, String> redisHash = redisTemplate.opsForHash();

        if (redisHash.hasKey(USER_NUMBERS, memberKey)) {
            String currMember = redisHash.get(USER_NUMBERS, memberKey);
            redisTemplate.expire(USER_NUMBERS, USER_NUMBERS_EXPIRE_TIME_SECONDS, TimeUnit.SECONDS);
            return Integer.parseInt(currMember);
        }
        String newNumber = String.valueOf(redisHash.size(USER_NUMBERS) + 1);
        log.info("{} 번호 발급 : {}", memberKey, newNumber);
        redisHash.put(USER_NUMBERS, memberKey, newNumber);
        redisTemplate.expire(USER_NUMBERS, USER_NUMBERS_EXPIRE_TIME_SECONDS, TimeUnit.SECONDS);
        return Integer.parseInt(newNumber);
    }

    public boolean joinEvent(EventParticipant participant) {
        String auctionArticleId = participant.getAuctionArticleId();
        String memberKey = participant.getMemberKey();
        String price = String.valueOf(participant.getPrice());

        if (Boolean.FALSE.equals(redisTemplate.hasKey(auctionArticleId))) {
            log.info("새로운 경매품: {}", auctionArticleId);
        }

        Bidding(auctionArticleId, memberKey, price);

        List<String> winner = redisTemplate.opsForList().range(auctionArticleId, 0, 0);
        if (winner == null) {
            return false;
        }
        log.info("경매품 {}의 1등: {}", auctionArticleId, winner);
        return !winner.isEmpty() && winner.get(0).equals(memberKey);
    }

    private void Bidding(String auctionArticleId, String memberKey, String price) {
        log.info("경매품 {}의 참가자: {}", auctionArticleId, memberKey);

        redisTemplate.opsForList().rightPush(auctionArticleId, memberKey);
        redisTemplate.expire(auctionArticleId, EXPIRE_TIME_SECONDS, TimeUnit.SECONDS);

        HashOperations<String, String, String> redisHash = redisTemplate.opsForHash();
        String priceKey = getPriceKey(auctionArticleId);
        if (redisHash.get(priceKey, memberKey) == null) {
            redisHash.put(priceKey, memberKey, price);
        }
        redisTemplate.expire(priceKey, EXPIRE_TIME_SECONDS, TimeUnit.SECONDS);
    }

    private static String getPriceKey(String auctionArticleId) {
        return "price-" + auctionArticleId;
    }

    public String getWinnerKey(String auctionArticleId) {
        return redisTemplate.opsForList().index(auctionArticleId, 0);
    }

    public String getWinnerPrice(String priceKey, String winnerKey) {
        HashOperations<String, String, String> redisHash = redisTemplate.opsForHash();
        return redisHash.get(priceKey, winnerKey);
    }
}
