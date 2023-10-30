package com.kkoch.admin.docs.auction;

import com.kkoch.admin.api.controller.auction.AuctionApiController;
import com.kkoch.admin.api.controller.auction.response.AuctionTitleResponse;
import com.kkoch.admin.api.service.auction.AuctionQueryService;
import com.kkoch.admin.api.service.auction.AuctionService;
import com.kkoch.admin.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuctionApiControllerDocsTest extends RestDocsSupport {

    private final AuctionService auctionService = mock(AuctionService.class);
    private final AuctionQueryService auctionQueryService = mock(AuctionQueryService.class);

    @Override
    protected Object initController() {
        return new AuctionApiController(auctionService, auctionQueryService);
    }

    @DisplayName("오픈되어있는 경매 일정 조회")
    @Test
    void getAuctionListForMember() throws Exception {
        AuctionTitleResponse response = AuctionTitleResponse.builder()
                .auctionId(1L)
                .title("23. 9. 20. 오전 5:00 절화 진행 중")
                .build();
        BDDMockito.given(auctionQueryService.getOpenAuction())
                .willReturn(response);

        mockMvc.perform(get("/admin-service/auctions/api"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auction-search",
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
                                fieldWithPath("data.auctionId").type(JsonFieldType.NUMBER)
                                        .description("경매 일정 PK"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("경매 타이틀")
                        )
                ));
    }
}
