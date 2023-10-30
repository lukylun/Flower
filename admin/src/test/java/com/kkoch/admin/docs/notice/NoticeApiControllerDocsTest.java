package com.kkoch.admin.docs.notice;

import com.kkoch.admin.api.controller.notice.NoticeApiController;
import com.kkoch.admin.api.controller.notice.response.NoticeResponse;
import com.kkoch.admin.api.service.notice.NoticeQueryService;
import com.kkoch.admin.docs.RestDocsSupport;
import com.kkoch.admin.domain.notice.repository.dto.NoticeSearchCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class NoticeApiControllerDocsTest extends RestDocsSupport {

    private final NoticeQueryService noticeQueryService = mock(NoticeQueryService.class);

    @Override
    protected Object initController() {
        return new NoticeApiController(noticeQueryService);
    }

    @DisplayName("공지사항 목록 조회 API")
    @Test
    void getNotices() throws Exception {

        NoticeResponse response1 = createNoticeResponse("2022년 서울경기권 졸업 일정 분포", "2020년 서울경기권 주요 학교 졸업일정분포를 게시하오니 첨부파일을 확인하여 주시기 바랍니다.", LocalDate.of(2019, 12, 17).atStartOfDay());
        NoticeResponse response2 = createNoticeResponse("2022년 학교별 졸업예정표(서울경기인천)", "2022년 학교별 졸업예정표(서울경기인천)입니다.", LocalDate.of(2022, 1, 10).atStartOfDay());
        NoticeResponse response3 = createNoticeResponse("2023년 학교별 졸업예정표(서울,경기,인천)", "해당 일정은 예정이며, 각 학교별 상황에 따라 유동성 있게 변경 될 수 있습니다.", LocalDate.of(2022, 12, 9).atStartOfDay());

        List<NoticeResponse> content = List.of(response1, response2, response3);
        PageRequest request = PageRequest.of(0, 10);

        given(noticeQueryService.getNotices(any(NoticeSearchCond.class), any(Pageable.class)))
            .willReturn(new PageImpl<>(content, request, content.size()));

        mockMvc.perform(
                get("/admin-service/notices")
                    .param("type", "1")
                    .param("keyword", "졸업")
                    .param("pageNum", "1")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("notice-search",
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("type")
                        .description("검색조건(1: 제목, 2: 내용)"),
                    parameterWithName("keyword")
                        .description("검색 키워드"),
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
                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                        .description("공지사항 제목"),
                    fieldWithPath("data.content[].content").type(JsonFieldType.STRING)
                        .description("공지사항 내용"),
                    fieldWithPath("data.content[].createdDate").type(JsonFieldType.STRING)
                        .description("작성일"),
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

    @DisplayName("공지사항 단건 조회 API")
    @Test
    void getNotice() throws Exception {

        NoticeResponse response = createNoticeResponse("2022년 서울경기권 졸업 일정 분포", "2020년 서울경기권 주요 학교 졸업일정분포를 게시하오니 첨부파일을 확인하여 주시기 바랍니다.", LocalDate.of(2019, 12, 17).atStartOfDay());

        given(noticeQueryService.getNotice(anyLong()))
            .willReturn(response);

        mockMvc.perform(
                get("/admin-service/notices/{noticeId}", 1L)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("notice-detail-search",
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
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("공지사항 제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING)
                        .description("공지사항 내용"),
                    fieldWithPath("data.createdDate").type(JsonFieldType.STRING)
                        .description("작성일")
                )
            ));
    }

    private NoticeResponse createNoticeResponse(String title, String content, LocalDateTime createdDate) {
        return NoticeResponse.builder()
            .title(title)
            .content(content)
//        (createdDate)
            .build();
    }
}
