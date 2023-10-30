package com.kkoch.user.docs.alarm;

import com.kkoch.user.api.controller.alarm.AlarmController;
import com.kkoch.user.api.controller.alarm.response.AlarmResponse;
import com.kkoch.user.api.service.alarm.AlamService;
import com.kkoch.user.api.service.alarm.AlarmQueryService;
import com.kkoch.user.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AlarmControllerDocsTest extends RestDocsSupport {

    private final AlamService alamService = mock(AlamService.class);
    private final AlarmQueryService alarmQueryService = mock(AlarmQueryService.class);

    @Override
    protected Object initController() {
        return new AlarmController(alamService, alarmQueryService);
    }

    @DisplayName("알람 조회 API")
    @Test
    void getAlarms() throws Exception {
        AlarmResponse response1 = createAlarmResponse(1L, false, LocalDateTime.of(2023, 8, 8, 7, 10));
        AlarmResponse response2 = createAlarmResponse(2L, false, LocalDateTime.of(2023, 8, 8, 6, 30));
        AlarmResponse response3 = createAlarmResponse(3L, false, LocalDateTime.of(2023, 8, 8, 5, 50));
        List<AlarmResponse> responses = List.of(response1, response2, response3);

        given(alarmQueryService.searchAlarms(anyString()))
            .willReturn(responses);

        given(alamService.open(anyString()))
            .willReturn(3);


        mockMvc.perform(get("/{memberKey}/alarms", UUID.randomUUID().toString())
                .header("Authorization", "token"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("alarm-search",
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY)
                        .description("응답 데이터"),
                    fieldWithPath("data[].alarmId").type(JsonFieldType.NUMBER)
                        .description("알림 PK"),
                    fieldWithPath("data[].content").type(JsonFieldType.STRING)
                        .description("알림 내용"),
                    fieldWithPath("data[].open").type(JsonFieldType.BOOLEAN)
                        .description("알림 열람 여부"),
                    fieldWithPath("data[].createdDate").type(JsonFieldType.STRING)
                        .description("알림 등록일")
                )
            ));
    }

    private static AlarmResponse createAlarmResponse(Long alarmId, boolean open, LocalDateTime createdDate) {
        return AlarmResponse.builder()
            .alarmId(alarmId)
            .content("알림 내용")
            .open(open)
            .createdDate(createdDate)
            .build();
    }
}
