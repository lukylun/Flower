package com.kkoch.admin.docs.auction;

import com.kkoch.admin.api.controller.auction.AuctionArticleApiController;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlePeriodSearchResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesResponse;
import com.kkoch.admin.api.service.auction.AuctionArticleQueryService;
import com.kkoch.admin.api.service.auction.AuctionArticleService;
import com.kkoch.admin.docs.RestDocsSupport;
import com.kkoch.admin.domain.Grade;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticlePeriodSearchCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuctionArticleApiControllerDocsTest extends RestDocsSupport {

    private final AuctionArticleService auctionArticleService = mock(AuctionArticleService.class);
    private final AuctionArticleQueryService auctionArticleQueryService = mock(AuctionArticleQueryService.class);

    @Override
    protected Object initController() {
        return new AuctionArticleApiController(auctionArticleService, auctionArticleQueryService);
    }

    @DisplayName("실시간 낙찰 내역(기간) 조회 API")
    @Test
    void getAuctionArticleList() throws Exception {
        AuctionArticlePeriodSearchResponse response1 = createAuctionArticlePeriodResponse("푸에고", 10, 9600, LocalDateTime.of(2023, 5, 3, 5, 0), "광주");
        AuctionArticlePeriodSearchResponse response2 = createAuctionArticlePeriodResponse("빅토리아", 20, 3400, LocalDateTime.of(2023, 5, 9, 5, 0), "인천");
        AuctionArticlePeriodSearchResponse response3 = createAuctionArticlePeriodResponse("헤라", 30, 3600, LocalDateTime.of(2023, 5, 8, 5, 0), "광주");
        AuctionArticlePeriodSearchResponse response4 = createAuctionArticlePeriodResponse("하젤", 15, 3030, LocalDateTime.of(2023, 5, 7, 5, 0), "서울");
        AuctionArticlePeriodSearchResponse response5 = createAuctionArticlePeriodResponse("푸에고", 25, 5500, LocalDateTime.of(2023, 5, 6, 5, 0), "광주");
        AuctionArticlePeriodSearchResponse response6 = createAuctionArticlePeriodResponse("헤라", 10, 2400, LocalDateTime.of(2023, 5, 5, 5, 0), "인천");
        AuctionArticlePeriodSearchResponse response7 = createAuctionArticlePeriodResponse("하젤", 30, 4920, LocalDateTime.of(2023, 5, 4, 5, 0), "서울");

        List<AuctionArticlePeriodSearchResponse> responses = List.of(response1, response2, response3, response4, response5, response6, response7);

        BDDMockito.given(auctionArticleQueryService.getAuctionArticlePeriodSearch(any(AuctionArticlePeriodSearchCond.class), any(Pageable.class))
        ).willReturn(new PageImpl<>(responses, PageRequest.of(0, 15), 100));

        mockMvc.perform(get("/admin-service/auction-articles/api")
                        .param("startDateTime", "2023-05-01")
                        .param("endDateTime", "2023-05-10")
                        .param("code", "절화")
                        .param("type", "장미")
                        .param("name", "")
                        .param("region", "")
                        .param("page", "0")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auction-article-search",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("startDateTime")
                                        .optional()
                                        .description("조회 시작 날짜"),
                                parameterWithName("endDateTime")
                                        .optional()
                                        .description("조회 마지막 날짜"),
                                parameterWithName("code")
                                        .optional()
                                        .description("구분코드"),
                                parameterWithName("type")
                                        .description("품목"),
                                parameterWithName("name")
                                        .description("품종"),
                                parameterWithName("region")
                                        .description("지역"),
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
                                        .description("경매품 데이터"),
                                fieldWithPath("data.content[].code").type(JsonFieldType.STRING)
                                        .description("경매품 구분코드"),
                                fieldWithPath("data.content[].type").type(JsonFieldType.STRING)
                                        .description("경매품 품목"),
                                fieldWithPath("data.content[].name").type(JsonFieldType.STRING)
                                        .description("경매품 품종"),
                                fieldWithPath("data.content[].grade").type(JsonFieldType.STRING)
                                        .description("경매품 등급"),
                                fieldWithPath("data.content[].count").type(JsonFieldType.NUMBER)
                                        .description("경매품 단(속)"),
                                fieldWithPath("data.content[].bidPrice").type(JsonFieldType.NUMBER)
                                        .description("경매품 낙찰 가격"),
                                fieldWithPath("data.content[].bidTime").type(JsonFieldType.STRING)
                                        .description("경매품 낙찰 시간"),
                                fieldWithPath("data.content[].region").type(JsonFieldType.STRING)
                                        .description("경매품 지역"),

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

    @DisplayName("경매 시작 시 경매품 목록 가져오기")
    @Test
    void getAuctionArticlesForAuction() throws Exception {
        AuctionArticlesResponse response1 = createAuctionArticleResponse(1L, "1-00001", "푸에고", 10, 10000, Grade.ADVANCED, "광주", "홍승준");
        AuctionArticlesResponse response2 = createAuctionArticleResponse(2L, "1-00002", "푸에고", 53, 6000, Grade.NORMAL, "서울", "홍승준");
        AuctionArticlesResponse response3 = createAuctionArticleResponse(3L, "1-00003", "빅토리아", 22, 8000, Grade.NORMAL, "서울", "홍승준");
        AuctionArticlesResponse response4 = createAuctionArticleResponse(4L, "1-00004", "푸에고", 7, 15000, Grade.SUPER, "광주", "신성주");
        AuctionArticlesResponse response5 = createAuctionArticleResponse(5L, "1-00005", "빅토리아", 15, 6000, Grade.NORMAL, "광주", "신성주");
        AuctionArticlesResponse response6 = createAuctionArticleResponse(6L, "1-00006", "빅토리아", 20, 9000, Grade.ADVANCED, "인천", "서용준");
        AuctionArticlesResponse response7 = createAuctionArticleResponse(7L, "1-00007", "푸에고", 10, 30000, Grade.SUPER, "인천", "서용준");

        List<AuctionArticlesResponse> responses = List.of(response1, response2, response3, response4, response5, response6, response7);

        BDDMockito.given(auctionArticleQueryService.getAuctionArticleList(anyLong())
        ).willReturn(responses);

        mockMvc.perform(get("/admin-service/auction-articles/{auctionId}", 1L)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auction-article-list",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("경매품 데이터"),
                                fieldWithPath("data[].auctionArticleId").type(JsonFieldType.NUMBER)
                                        .description("경매품 PK"),
                                fieldWithPath("data[].auctionNumber").type(JsonFieldType.STRING)
                                        .description("경매품 상장번호"),
                                fieldWithPath("data[].code").type(JsonFieldType.STRING)
                                        .description("경매품 구분코드"),
                                fieldWithPath("data[].type").type(JsonFieldType.STRING)
                                        .description("경매품 품목"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING)
                                        .description("경매품 품종"),
                                fieldWithPath("data[].count").type(JsonFieldType.NUMBER)
                                        .description("경매품 단(속)"),
                                fieldWithPath("data[].startPrice").type(JsonFieldType.NUMBER)
                                        .description("경매품 시작 가격"),
                                fieldWithPath("data[].grade").type(JsonFieldType.STRING)
                                        .description("경매품 등급"),
                                fieldWithPath("data[].region").type(JsonFieldType.STRING)
                                        .description("경매품 지역"),
                                fieldWithPath("data[].shipper").type(JsonFieldType.STRING)
                                        .description("경매품 출하자")
                        )
                ));
    }

    private static AuctionArticlesResponse createAuctionArticleResponse(long auctionArticleId, String auctionNumber, String name, int count, int startPrice, Grade grade, String region, String shipper) {
        return AuctionArticlesResponse.builder()
                .auctionArticleId(auctionArticleId)
                .auctionNumber(auctionNumber)
                .code("절화")
                .type("장미")
                .name(name)
                .count(count)
                .startPrice(startPrice)
                .grade(grade)
                .region(region)
                .shipper(shipper)
                .build();
    }

    private static AuctionArticlePeriodSearchResponse createAuctionArticlePeriodResponse(String name, int count, int bidPrice, LocalDateTime bidTime, String region) {
        return AuctionArticlePeriodSearchResponse.builder()
                .code("절화")
                .type("장미")
                .name(name)
                .grade(Grade.SUPER)
                .count(count)
                .bidPrice(bidPrice)
                .bidTime(bidTime)
                .region(region)
                .build();
    }
}
