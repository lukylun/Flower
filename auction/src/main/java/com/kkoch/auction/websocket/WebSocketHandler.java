package com.kkoch.auction.websocket;

import com.kkoch.auction.client.AdminServiceClient;
import com.kkoch.auction.api.service.RedisService;
import com.kkoch.auction.api.service.dto.AuctionArticlesResponse;
import com.kkoch.auction.client.UserServiceClient;
import com.kkoch.auction.client.response.ReservationForAuctionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final RedisService redisService;
    private final AdminServiceClient adminServiceClient;
    private final UserServiceClient userServiceClient;

    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>(); //웹소켓 세션 store
    private static String videoSessionId = null; //비디오 세션
    private static String adminSession = null; //관리자 세션 -> 최초 소켓 생성자
    private static WebSocketSession admin = null;

    private final Deque<AuctionArticlesResponse> queue = new LinkedList<>(); //경매 순서 확인 큐

    //입장
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("connection session id = {}", session.getId());

        //방 생성자 외의 참가자는 videoSessionId 반환
        if (!sessions.isEmpty()) {
            session.sendMessage(new TextMessage(videoSessionId));
        }

        //web socket 세션 저장
        sessions.put(session.getId(), session);
    }

    //통신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("message session id = {}", session.getId());
        log.info("message = {}", message);

        JSONObject json = getJsonObject(message);

        //권한 확인 추출
        String role = getDataForJson(json, "role");
        log.info("role = {}", role);

        //고객인데 경매방이 열리지 않은 경우
        if (isClient(role)) {
            client(session, message);
        }

        if (isAdmin(role)) {
            admin(session, json);
        }
    }

    //퇴장
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("disconnection session id = {}", session.getId());

        String currentSessionId = session.getId();

        //세션 저장소에서 현재 세션 제거
        sessions.remove(currentSessionId);

        log.info("currentSessionId={}, adminSession={}", currentSessionId, adminSession);

        //현재 세션이 관리자 세션이라면 모든 세션 종료
        if (currentSessionId.equals(adminSession)) {
            //관리자 세션 초기화
            adminSession = null;
            //종료 메세지 출력
            sendEndMessage();
            //모든 세션 종료
            log.info("before session clear={}", sessions.keySet().size());
            sessions.clear();
            log.info("after session clear={}", sessions.keySet().size());
        }

        //세션이 비어 있는 경우
        if (sessions.isEmpty()) {
            //비디오 세션 초기화
            videoSessionId = null;
        }
        log.info("end adminSession = {}, videoSessionId = {}", adminSession, videoSessionId);
    }

    private boolean isClient(String role) {
        return role.equals("client");
    }
    private boolean isAdmin(String role) {
        return role.equals("admin");
    }

    private boolean isNotOpenAuction() {
        return videoSessionId == null;
    }

    private void sendEndMessage() {
        sessions.forEach((key, value) -> {
            try {
                value.sendMessage(new TextMessage("금일 경매가 종료되었습니다."));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendMessageAll(String data) {
        sessions.forEach((key, value) -> {
            try {
                value.sendMessage(new TextMessage(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private JSONObject getJsonObject(TextMessage message) throws ParseException {
        //json 파싱
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(message.getPayload());

        //json 객체 변환
        return (JSONObject) obj;
    }

    private String getDataForJson(JSONObject json, String key) {
        return (String) json.get(key);
    }

    private void client(WebSocketSession session, TextMessage message) throws IOException {
        String currentSessionId = session.getId();

        if (isNotOpenAuction()) {
            sessions.remove(currentSessionId);
            session.sendMessage(new TextMessage("not start auction"));
            return;
        }

        log.info("send message all session");
        sendMessageAll(message.getPayload());
    }

    private void admin(WebSocketSession session, JSONObject json) throws IOException {
        String currentSessionId = session.getId();
        String command = getDataForJson(json, "command");

        //open
        if (command.equals("open")) {
            log.info("open web socket");
            admin = session;
            adminSession = currentSessionId;
            videoSessionId = getDataForJson(json, "sessionId");
            log.info("adminSession={}, videoSessionId={}", adminSession, videoSessionId);
        }

        //start
        if (command.equals("start")) {
            log.info("auction start");
            String test = getDataForJson(json, "auctionId");
            log.info("auctionId = {}", test);

            Long auctionId = Long.parseLong(test);
            log.info("auctionId = {}", auctionId);

            List<AuctionArticlesResponse> responses = adminServiceClient.getAuctionArticlesForAuction(auctionId).getData();
            for (AuctionArticlesResponse response : responses) {
                log.info("for response = {}", response);
            }
            log.info("response size = {}", responses.size());

            redisService.insertList(responses);
            AuctionArticlesResponse nextArticle = redisService.getNextArticle();
            log.info("first data = {}", nextArticle);
            queue.add(nextArticle);

            String data = nextArticle.toJson("start");
            log.info("send first json data = {}", data);
            sendMessageAll(data);

            nextArticle = redisService.getNextArticle();
            queue.add(nextArticle);

            data = nextArticle.toJson("next");
            log.info("send second json data = {}", data);
            sendMessageAll(data);

            AuctionArticlesResponse auctionArticlesResponse = queue.pollFirst();
            log.info("AuctionArticlesResponse={}", auctionArticlesResponse);
            Long plantId = auctionArticlesResponse.getPlantId();
            ReservationForAuctionResponse response = userServiceClient.getReservationForAuction(plantId);
            admin.sendMessage(new TextMessage(response.toJson()));
        }

        //next
        if (command.equals("next")) {
            AuctionArticlesResponse nextArticle = redisService.getNextArticle();
            queue.add(nextArticle);

            String data = nextArticle.toJson("next");
            log.info("send next json data = {}", data);
            sendMessageAll(data);

            AuctionArticlesResponse auctionArticlesResponse = queue.pollFirst();
            Long plantId = auctionArticlesResponse.getPlantId();
            ReservationForAuctionResponse response = userServiceClient.getReservationForAuction(plantId);
            admin.sendMessage(new TextMessage(response.toJson()));
        }

        //close
        if (command.equals("close")) {
            String data = getDataForJson(json, "auctionId");
            log.info("auctionId = {}", data);

            Long auctionId = Long.parseLong(data);
            log.info("auctionId = {}", auctionId);

            adminServiceClient.setAuctionStatus(auctionId, "CLOSE");
            log.info("close web socket");
        }
    }
}
