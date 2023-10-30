package com.kkoch.user.docs.pointhistory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kkoch.user.api.controller.pointhistory.PointHistoryController;
import com.kkoch.user.api.controller.pointhistory.request.AddPointHistoryRequest;
import com.kkoch.user.api.controller.pointhistory.response.AddPointHistoryResponse;
import com.kkoch.user.api.controller.pointhistory.response.PointHistoryResponse;
import com.kkoch.user.api.service.pointhistory.PointHistoryQueryService;
import com.kkoch.user.api.service.pointhistory.PointHistoryService;
import com.kkoch.user.api.service.pointhistory.dto.AddPointHistoryDto;
import com.kkoch.user.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * PointHistory REST Docs 테스트
 * @author 임우택
 */
public class PointHistoryControllerDocsTest extends RestDocsSupport {

    private final PointHistoryService pointHistoryService = mock(PointHistoryService.class);
    private final PointHistoryQueryService pointHistoryQueryService = mock(PointHistoryQueryService.class);

    @Override
    protected Object initController() {
        return new PointHistoryController(pointHistoryService, pointHistoryQueryService);
    }

    @DisplayName("포인트 충전 API")
    @Test
    void chargePointHistory() throws Exception {
        AddPointHistoryRequest request = AddPointHistoryRequest.builder()
            .bank("신한은행")
            .amount(10_000_000)
            .build();

        AddPointHistoryResponse response = AddPointHistoryResponse.builder()
            .bank("신한은행")
            .amount(10_000_000)
            .status(1)
            .createdDate(LocalDateTime.of(2023, 8, 1, 18, 0).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)))
            .build();

        given(pointHistoryService.addPointHistory(anyString(), any(AddPointHistoryDto.class)))
            .willReturn(response);

        mockMvc.perform(
                post("/{memberKey}/points/charge", UUID.randomUUID().toString())
                    .header("Authorization", "token")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("point-history-charge",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("bank").type(JsonFieldType.STRING)
                        .description("은행명"),
                    fieldWithPath("amount").type(JsonFieldType.NUMBER)
                        .description("금액")
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
                    fieldWithPath("data.bank").type(JsonFieldType.STRING)
                        .description("은행명"),
                    fieldWithPath("data.amount").type(JsonFieldType.NUMBER)
                        .description("금액"),
                    fieldWithPath("data.status").type(JsonFieldType.NUMBER)
                        .description("구분"),
                    fieldWithPath("data.createdDate").type(JsonFieldType.STRING)
                        .description("등록일")
                )
            ));

    }

    @DisplayName("포인트 내역 API")
    @Test
    void usePointHistory() throws Exception {
        AddPointHistoryRequest request = AddPointHistoryRequest.builder()
            .bank("신한은행")
            .amount(5_000_000)
            .build();

        AddPointHistoryResponse response = AddPointHistoryResponse.builder()
            .bank("신한은행")
            .amount(5_000_000)
            .status(2)
            .createdDate(LocalDateTime.of(2023, 8, 1, 18, 0).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)))
            .build();

        given(pointHistoryService.addPointHistory(anyString(), any(AddPointHistoryDto.class)))
            .willReturn(response);

        mockMvc.perform(
                post("/{memberKey}/points/use", UUID.randomUUID().toString())
                    .header("Authorization", "token")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("point-history-use",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("bank").type(JsonFieldType.STRING)
                        .description("은행명"),
                    fieldWithPath("amount").type(JsonFieldType.NUMBER)
                        .description("금액")
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
                    fieldWithPath("data.bank").type(JsonFieldType.STRING)
                        .description("은행명"),
                    fieldWithPath("data.amount").type(JsonFieldType.NUMBER)
                        .description("금액"),
                    fieldWithPath("data.status").type(JsonFieldType.NUMBER)
                        .description("구분"),
                    fieldWithPath("data.createdDate").type(JsonFieldType.STRING)
                        .description("등록일")
                )
            ));

    }

    @DisplayName("포인트 내역 조회 API")
    @Test
    void getMyPointHistories() throws Exception {
        PointHistoryResponse response1 = createPointHistoryResponse();
        PointHistoryResponse response2 = createPointHistoryResponse();
        List<PointHistoryResponse> content = List.of(response1, response2);
        PageRequest pageRequest = PageRequest.of(0, 10);

        given(pointHistoryQueryService.getMyPointHistories(anyString(), any(Pageable.class)))
            .willReturn(new PageImpl<>(content, pageRequest, 2));

        mockMvc.perform(
                get("/{memberKey}/points", UUID.randomUUID().toString())
                    .header("Authorization", "token")
                    .param("pageNum", "0")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("point-history-search",
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("pageNum")
                        .description("페이지 번호")
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
                    fieldWithPath("data.content[].no").type(JsonFieldType.NUMBER)
                        .description("번호"),
                    fieldWithPath("data.content[].bank").type(JsonFieldType.STRING)
                        .description("은행명"),
                    fieldWithPath("data.content[].amount").type(JsonFieldType.NUMBER)
                        .description("금액"),
                    fieldWithPath("data.content[].status").type(JsonFieldType.NUMBER)
                        .description("구분(1: 충전, 2: 사용)"),
                    fieldWithPath("data.content[].createdDate").type(JsonFieldType.STRING)
                        .description("등록일"),
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

    private PointHistoryResponse createPointHistoryResponse() {
        return PointHistoryResponse.builder()
            .bank("신한은행")
            .amount(10_000_000)
            .status(1)
            .createdDate(LocalDateTime.of(2023, 8, 1, 15, 0))
            .build();
    }
}
