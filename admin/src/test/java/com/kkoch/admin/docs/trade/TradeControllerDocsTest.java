package com.kkoch.admin.docs.trade;

import com.kkoch.admin.api.controller.trade.TradeController;
import com.kkoch.admin.api.controller.trade.request.AddTradeRequest;
import com.kkoch.admin.api.controller.trade.response.SuccessfulBid;
import com.kkoch.admin.api.controller.trade.response.TradeDetailResponse;
import com.kkoch.admin.api.controller.trade.response.TradeResponse;
import com.kkoch.admin.api.service.trade.TradeQueryService;
import com.kkoch.admin.api.service.trade.TradeService;
import com.kkoch.admin.api.service.trade.dto.AddTradeDto;
import com.kkoch.admin.docs.RestDocsSupport;
import com.kkoch.admin.domain.Grade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TradeControllerDocsTest extends RestDocsSupport {

    private final TradeService tradeService = mock(TradeService.class);
    private final TradeQueryService tradeQueryService = mock(TradeQueryService.class);

    @Override
    protected Object initController() {
        return new TradeController(tradeService, tradeQueryService);
    }

    @DisplayName("낙찰 내역을 등록하는 API")
    @Test
    void addTrade() throws Exception {
        AddTradeRequest request = AddTradeRequest.builder()
                .memberKey(UUID.randomUUID().toString())
                .auctionArticleId(1L)
                .price(3000)
                .build();

        given(tradeService.addTrade(any(AddTradeDto.class), any(LocalDateTime.class)))
                .willReturn(4L);

        mockMvc.perform(post("/admin-service/trades")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("trade-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("memberToken").type(JsonFieldType.STRING)
                                        .description("회원 고유키"),
                                fieldWithPath("auctionArticleId").type(JsonFieldType.NUMBER)
                                        .description("경매품 pk"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("낙찰 가격")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("응답 데이터")
                        )));
    }

    @DisplayName("낙찰 내역을 조회하는 API")
    @Test
    void getMyTrades() throws Exception {
        TradeResponse tradeResponse1 = createTradeResponse(1L, 10000, LocalDate.of(2023, 7, 10).atStartOfDay(), true, 10);
        TradeResponse tradeResponse2 = createTradeResponse(2L, 20000, LocalDate.of(2023, 7, 17).atStartOfDay(), true, 20);
        TradeResponse tradeResponse3 = createTradeResponse(3L, 30000, LocalDate.of(2023, 7, 24).atStartOfDay(), false, 30);
        List<TradeResponse> responses = List.of(tradeResponse1, tradeResponse2, tradeResponse3);
        PageRequest pageRequest = PageRequest.of(0, 20);

        given(tradeQueryService.getMyTrades(anyString(), any(Pageable.class)))
                .willReturn(new PageImpl<>(responses, pageRequest, 100));

        mockMvc.perform(get("/admin-service/trades/{memberId}", 1L)
                        .param("term", "1")
                        .param("page", "0")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("trade-search",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("term")
                                        .description("기간"),
                                parameterWithName("page")
                                        .description("페이지")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.content").type(JsonFieldType.ARRAY)
                                        .description("낙찰 내역 데이터"),
                                fieldWithPath("data.content[].tradeId").type(JsonFieldType.NUMBER)
                                        .description("낙찰 내역 PK"),
                                fieldWithPath("data.content[].totalPrice").type(JsonFieldType.NUMBER)
                                        .description("총 거래 가격"),
                                fieldWithPath("data.content[].tradeDate").type(JsonFieldType.STRING)
                                        .description("거래 시간"),
                                fieldWithPath("data.content[].pickupStatus").type(JsonFieldType.BOOLEAN)
                                        .description("픽업여부"),
                                fieldWithPath("data.content[].count").type(JsonFieldType.NUMBER)
                                        .description("낙찰한 경매품 갯수"),
                                fieldWithPath("data.pageable").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("응답 데이터"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("응답 데이터"),
                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("응답 데이터"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("응답 데이터"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("응답 데이터"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("응답 데이터"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("응답 데이터"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("응답 데이터"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                                        .description("총 페이지 수"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                                        .description("DB의 전체 데이터 갯수"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지라면 true"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                        .description("페이지 당 나타낼 수 있는 데이터의 갯수"),
                                fieldWithPath("data.sort").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("응답 데이터"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("응답 데이터"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("응답 데이터"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("실제 데이터의 갯수"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                                        .description("첫번째 페이지이면 true"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
                                        .description("리스트가 비어있는지 여부")
                        )
                ));
    }

    @DisplayName("낙찰 내역 상세조회 API")
    @Test
    void getTrade() throws Exception {
        SuccessfulBid auctionArticle = createSuccessfulBid();
        TradeDetailResponse response = TradeDetailResponse.builder()
                .totalPrice(10000)
                .tradeTime(LocalDate.of(2023, 7, 10).atStartOfDay())
                .pickupStatus(false)
                .build();
        response.insertAuctionArticles(Collections.singletonList(auctionArticle));

        given(tradeQueryService.getTrade(anyLong()))
                .willReturn(response);

        mockMvc.perform(
                        get("/admin-service/trades/{memberToken}/{tradeId}", "memberToken", 1L)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data.totalPrice").value(10000))
                .andExpect(jsonPath("$.data.pickupStatus").value(false))
                .andDo(document("trade-search-detail",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.totalPrice").type(JsonFieldType.NUMBER)
                                        .description("총 낙찰 가격"),
                                fieldWithPath("data.tradeTime").type(JsonFieldType.STRING)
                                        .description("거래 시간"),
                                fieldWithPath("data.pickupStatus").type(JsonFieldType.BOOLEAN)
                                        .description("픽업 여부"),
                                fieldWithPath("data.successfulBid").type(JsonFieldType.ARRAY)
                                        .description("경매품 정보 리스트"),
                                fieldWithPath("data.successfulBid[].code").type(JsonFieldType.STRING)
                                        .description("식물 구분 코드"),
                                fieldWithPath("data.successfulBid[].name").type(JsonFieldType.STRING)
                                        .description("식물 품종 명"),
                                fieldWithPath("data.successfulBid[].type").type(JsonFieldType.STRING)
                                        .description("식물 품목 명"),
                                fieldWithPath("data.successfulBid[].grade").type(JsonFieldType.STRING)
                                        .description("경매품 등급"),
                                fieldWithPath("data.successfulBid[].count").type(JsonFieldType.NUMBER)
                                        .description("경매품 단수"),
                                fieldWithPath("data.successfulBid[].bidPrice").type(JsonFieldType.NUMBER)
                                        .description("낙찰 가격"),
                                fieldWithPath("data.successfulBid[].bidTime").type(JsonFieldType.STRING)
                                        .description("낙찰 시간"),
                                fieldWithPath("data.successfulBid[].region").type(JsonFieldType.STRING)
                                        .description("출하 지역")
                        )
                ));

    }

    private static SuccessfulBid createSuccessfulBid() {
        return SuccessfulBid.builder()
                .code("절화")
                .name("장미(스탠다드)")
                .type("하젤")
                .grade(Grade.NONE)
                .count(10)
                .bidPrice(10_000)
                .bidTime(LocalDateTime.of(2023, 7, 10, 11, 30))
                .region("광주")
                .build();
    }

    @DisplayName("경매품을 픽업하는 API")
    @Test
    void pickup() throws Exception {
        given(tradeService.pickup(anyLong()))
                .willReturn(1L);

        mockMvc.perform(
                        patch("/admin-service/trades/{tradeId}", 1L)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isNumber())
                .andDo(document("trade-pickup",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("응답 데이터")
                        )
                ));
    }

    @DisplayName("낙찰 내역을 삭제하는 API")
    @Test
    void removeTrade() throws Exception {
        given(tradeService.remove(anyLong()))
                .willReturn(1L);

        mockMvc.perform(
                delete("/admin-service/trades/{tradeId}", 1L)
        );
    }

    private TradeResponse createTradeResponse(long tradeId, int totalPrice, LocalDateTime tradeDate, boolean pickupStatus, int count) {
        return TradeResponse.builder()
                .count(count)
                .build();
    }
}
