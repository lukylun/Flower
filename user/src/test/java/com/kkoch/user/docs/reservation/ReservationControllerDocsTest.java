package com.kkoch.user.docs.reservation;

import com.kkoch.user.api.controller.reservation.ReservationController;
import com.kkoch.user.api.controller.reservation.request.AddReservationRequest;
import com.kkoch.user.api.controller.reservation.response.AddReservationResponse;
import com.kkoch.user.api.controller.reservation.response.ReservationResponse;
import com.kkoch.user.api.service.reservation.ReservationQueryService;
import com.kkoch.user.api.service.reservation.ReservationService;
import com.kkoch.user.api.service.reservation.dto.AddReservationDto;
import com.kkoch.user.docs.RestDocsSupport;
import com.kkoch.user.domain.Grade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.kkoch.user.domain.Grade.*;
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

public class ReservationControllerDocsTest extends RestDocsSupport {

    private final ReservationService reservationService = mock(ReservationService.class);
    private final ReservationQueryService reservationQueryService = mock(ReservationQueryService.class);

    @Override
    protected Object initController() {
        return new ReservationController(reservationService, reservationQueryService);
    }

    @DisplayName("거래 예약 등록 API")
    @Test
    void addReservation() throws Exception {
        AddReservationRequest request = AddReservationRequest.builder()
            .type("장미(스탠다드)")
            .name("하젤")
            .grade(SUPER)
            .count(10)
            .price(2500)
            .build();

        AddReservationResponse response = AddReservationResponse.builder()
            .count(10)
            .price(2500)
            .grade(SUPER.getText())
            .createdDate(LocalDateTime.now())
            .build();

        given(reservationService.addReservation(anyString(), any(AddReservationDto.class)))
            .willReturn(response);

        mockMvc.perform(post("/{memberKey}/reservations", UUID.randomUUID().toString())
                .header("Authorization", "token")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("reservation-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("type").type(JsonFieldType.STRING)
                        .description("품목명"),
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("품종명"),
                    fieldWithPath("grade").type(JsonFieldType.STRING)
                        .description("예약 등급"),
                    fieldWithPath("count").type(JsonFieldType.NUMBER)
                        .description("예약 단수"),
                    fieldWithPath("price").type(JsonFieldType.NUMBER)
                        .description("예약 가격")
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
                    fieldWithPath("data.count").type(JsonFieldType.NUMBER)
                        .description("예약 단수"),
                    fieldWithPath("data.price").type(JsonFieldType.NUMBER)
                        .description("예약 가격"),
                    fieldWithPath("data.grade").type(JsonFieldType.STRING)
                        .description("예약 등급"),
                    fieldWithPath("data.createdDate").type(JsonFieldType.STRING)
                        .description("예약 등록일")
                )
            ));
    }

    @DisplayName("거래 예약 조회 API")
    @Test
    void reservations() throws Exception {

        ReservationResponse response1 = createReservationResponse(SUPER, 10, 3500);
        ReservationResponse response2 = createReservationResponse(ADVANCED, 20, 3000);
        ReservationResponse response3 = createReservationResponse(NORMAL, 10, 2000);
        List<ReservationResponse> responses = List.of(response1, response2, response3);

        PageRequest pageRequest = PageRequest.of(0, 10);

        given(reservationQueryService.getMyReservations(anyString(), any(Pageable.class)))
            .willReturn(new PageImpl<>(responses, pageRequest, responses.size()));

        mockMvc.perform(
                get("/{memberKey}/reservations", UUID.randomUUID().toString())
                    .header("Authorization", "token")
                    .param("pageNum", "0")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("reservation-search",
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
                    fieldWithPath("data.content[].type").type(JsonFieldType.STRING)
                        .description("품목명"),
                    fieldWithPath("data.content[].name").type(JsonFieldType.STRING)
                        .description("품종명"),
                    fieldWithPath("data.content[].grade").type(JsonFieldType.STRING)
                        .description("예약 등급"),
                    fieldWithPath("data.content[].count").type(JsonFieldType.NUMBER)
                        .description("예약 단수"),
                    fieldWithPath("data.content[].price").type(JsonFieldType.NUMBER)
                        .description("예약 가격"),
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

    private ReservationResponse createReservationResponse(Grade grade, int count, int price) {
        return ReservationResponse.builder()
            .type("장미(스탠다드)")
            .name("하젤")
            .grade(grade.getText())
            .count(count)
            .price(price)
            .build();
    }
}
