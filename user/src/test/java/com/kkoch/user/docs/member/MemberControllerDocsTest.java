package com.kkoch.user.docs.member;

import com.kkoch.user.api.controller.member.MemberController;
import com.kkoch.user.api.controller.member.request.LoginRequest;
import com.kkoch.user.api.controller.member.request.SetPasswordRequest;
import com.kkoch.user.api.controller.member.request.WithdrawalRequest;
import com.kkoch.user.api.controller.member.response.MemberInfoResponse;
import com.kkoch.user.api.controller.member.response.MemberResponse;
import com.kkoch.user.api.service.member.MemberQueryService;
import com.kkoch.user.api.service.member.MemberService;
import com.kkoch.user.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MemberControllerDocsTest extends RestDocsSupport {

    private final MemberService memberService = mock(MemberService.class);
    private final MemberQueryService memberQueryService = mock(MemberQueryService.class);

    @Override
    protected Object initController() {
        return new MemberController(memberService, memberQueryService);
    }

    @DisplayName("회원 정보 조회 API")
    @Test
    void getMyInfo() throws Exception {
        MemberInfoResponse response = MemberInfoResponse.builder()
            .email("ssafy@ssafy.com")
            .name("김싸피")
            .tel("010-1234-1234")
            .businessNumber("123-12-12345")
            .build();

        given(memberQueryService.getMyInfo(anyString()))
            .willReturn(response);

        mockMvc.perform(get("/{memberKey}", UUID.randomUUID().toString())
                .header("Authorization", "token"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("my-info",
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
                    fieldWithPath("data.email").type(JsonFieldType.STRING)
                        .description("사용자 이메일"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING)
                        .description("사용자 이름"),
                    fieldWithPath("data.tel").type(JsonFieldType.STRING)
                        .description("사용자 연락처"),
                    fieldWithPath("data.businessNumber").type(JsonFieldType.STRING)
                        .description("사용자 사업자번호")
                )
            ));
    }

    @DisplayName("회원 비밀번호 변경 API")
    @Test
    void setPassword() throws Exception {
        String memberKey = UUID.randomUUID().toString();

        SetPasswordRequest request = SetPasswordRequest.builder()
            .currentPwd("ssafy1234@")
            .newPwd("ssafyc204!")
            .build();

        MemberResponse response = MemberResponse.builder()
            .email("ssafy@ssafy.com")
            .name("김싸피")
            .memberKey(memberKey)
            .build();

        given(memberService.setPassword(anyString(), anyString(), anyString()))
            .willReturn(response);

        mockMvc.perform(patch("/{memberKey}/pwd", memberKey)
                .header("Authorization", "token")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("my-password",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("currentPwd").type(JsonFieldType.STRING)
                        .description("현재 비밀번호"),
                    fieldWithPath("newPwd").type(JsonFieldType.STRING)
                        .description("새 비밀번호")
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
                    fieldWithPath("data.email").type(JsonFieldType.STRING)
                        .description("사용자 이메일"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING)
                        .description("사용자 이름"),
                    fieldWithPath("data.memberKey").type(JsonFieldType.STRING)
                        .description("사용자 고유키")
                )
            ));
    }

    @DisplayName("회원 탈퇴 API")
//    @Test
    void withdrawal() throws Exception {
        String memberKey = UUID.randomUUID().toString();

        WithdrawalRequest request = WithdrawalRequest.builder()
            .pwd("ssafy1234!")
            .build();

        MemberResponse response = MemberResponse.builder()
            .email("ssafy@ssafy.com")
            .name("김싸피")
            .memberKey(memberKey)
            .build();

        given(memberService.withdrawal(anyString(), anyString()))
            .willReturn(response);

        mockMvc.perform(delete("/{memberKey}", memberKey)
                .header("Authorization", "token")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("withdrawal",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("pwd").type(JsonFieldType.STRING)
                        .description("현재 비밀번호")
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
                    fieldWithPath("data.email").type(JsonFieldType.STRING)
                        .description("사용자 이메일"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING)
                        .description("사용자 이름"),
                    fieldWithPath("data.memberKey").type(JsonFieldType.STRING)
                        .description("사용자 고유키")
                )
            ));
    }
}
