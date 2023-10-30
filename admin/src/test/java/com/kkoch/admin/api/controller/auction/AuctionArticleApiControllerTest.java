package com.kkoch.admin.api.controller.auction;


import com.kkoch.admin.ControllerTestSupport;
import com.kkoch.admin.api.controller.auction.request.AddAuctionArticleRequest;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlePeriodSearchResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesForAdminResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesResponse;
import com.kkoch.admin.api.service.auction.AuctionArticleQueryService;
import com.kkoch.admin.api.service.auction.AuctionArticleService;
import com.kkoch.admin.api.service.auction.dto.AddAuctionArticleDto;
import com.kkoch.admin.domain.Grade;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticlePeriodSearchCond;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticleSearchForAdminCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AuctionArticleApiController.class})
class AuctionArticleApiControllerTest extends ControllerTestSupport {

    @MockBean
    private AuctionArticleService auctionArticleService;
    @MockBean
    private AuctionArticleQueryService auctionArticleQueryService;

    @DisplayName("[경매품 조회] 경매 시 경매품 목록")
    @Test
    void getAuctionArticleForAuction() throws Exception {
        //given
        List<AuctionArticlesResponse> list = List.of();

        BDDMockito.given(auctionArticleQueryService.getAuctionArticleList(anyLong()))
                .willReturn(list);

        //when //then
        mockMvc.perform(
                        get("/admin-service/auction-articles/{auctionId}", 1L)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @DisplayName("[경매 실적 조회] 관리자용")
    @Test
    void getAuctionArticleForAdmin() throws Exception {
        //given
        List<AuctionArticlesForAdminResponse> list = List.of();

        BDDMockito.given(auctionArticleQueryService.getAuctionArticleListForAdmin(any(AuctionArticleSearchForAdminCond.class)))
                .willReturn(list);

        //when //then
        mockMvc.perform(

                        get("/admin-service/auction-articles")
                                .queryParam("endDateTime", "2023-08-01")
                                .queryParam("code", "절화")
                                .queryParam("type", "장미")
                                .queryParam("name", "푸에고")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @DisplayName("[경매 실적 조회]")
    @Test
    void getAuctionArticlePeriod() throws Exception {
        //given
        List<AuctionArticlePeriodSearchResponse> list = List.of();
        PageImpl<AuctionArticlePeriodSearchResponse> responses = new PageImpl<>(list);

        BDDMockito.given(auctionArticleQueryService.getAuctionArticlePeriodSearch(any(AuctionArticlePeriodSearchCond.class), any(Pageable.class)))
                .willReturn(responses);

        //when //then
        mockMvc.perform(

                        get("/admin-service/auction-articles/api")
                                .queryParam("startDateTime", "2023-08-01")
                                .queryParam("endDateTime", "2023-08-05")
                                .queryParam("code", "절화")
                                .queryParam("type", "장미")
                                .queryParam("name", "푸에고")
                                .queryParam("region", "")
                                .queryParam("page", "0")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @DisplayName("[경매품 등록] 존재하지 않는 경매일정에 경매품을 등록하면 예외가 발생한다.")
    @Test
    void addAuctionArticleScheduleError() throws Exception {
        //given
        AddAuctionArticleRequest request = generateAddAuctionArticleRequest();

        BDDMockito.given(auctionArticleService.addAuctionArticle(anyLong(), anyLong(), any(AddAuctionArticleDto.class)))
                .willThrow(new NoSuchElementException("존재하지 않는 경매 일정입니다."));

        //when
        //then
        mockMvc.perform(
                        post("/admin-service/auction-articles")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 경매 일정입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @DisplayName("[경매품 등록] 존재하지 않는 식물을 경매품으로 등록하면 예외가 발생한다.")
    @Test
    void addAuctionArticlePlantError() throws Exception {
        //given
        AddAuctionArticleRequest request = generateAddAuctionArticleRequest();

        BDDMockito.given(auctionArticleService.addAuctionArticle(anyLong(), anyLong(), any(AddAuctionArticleDto.class)))
                .willThrow(new NoSuchElementException("존재하지 않는 식물입니다."));

        //when
        //then
        mockMvc.perform(
                        post("/admin-service/auction-articles")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 식물입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("[경매품 등록]")
    @Test
    void addAuctionArticle() throws Exception {
        //given
        AddAuctionArticleRequest request = generateAddAuctionArticleRequest();

        //when
        //then
        mockMvc.perform(
                        post("/admin-service/auction-articles")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isNumber());
    }

    private static AddAuctionArticleRequest generateAddAuctionArticleRequest() {
        return AddAuctionArticleRequest.builder()
                .auctionId(1L)
                .plantId(1L)
                .startPrice(20000)
                .count(20)
                .grade(Grade.NONE)
                .shipper("꽃파파")
                .region("광주")
                .build();
    }
}