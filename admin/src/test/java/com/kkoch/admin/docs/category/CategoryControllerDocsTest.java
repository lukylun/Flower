package com.kkoch.admin.docs.category;

import com.kkoch.admin.api.controller.category.CategoryController;
import com.kkoch.admin.api.service.category.CategoryQueryService;
import com.kkoch.admin.api.service.category.CategoryService;
import com.kkoch.admin.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerDocsTest extends RestDocsSupport {

    private final CategoryService categoryService = mock(CategoryService.class);
    private final CategoryQueryService categoryQueryService = mock(CategoryQueryService.class);

    @Override
    protected Object initController() {
        return new CategoryController(categoryService, categoryQueryService);
    }

    @DisplayName("카테고리 품목 조회 API")
    @Test
    void getTypes() throws Exception {
        List<String> types = List.of("소재", "장미(스탠다드)");

        given(categoryQueryService.getTypesForMember(anyString()))
            .willReturn(types);

        mockMvc.perform(
            get("/admin-service/categories/type")
                .param("code", "절화")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("category-type-search",
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("code")
                        .description("구분코드")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data[]").type(JsonFieldType.ARRAY)
                        .description("품목명")
                )
            ));
    }

    @DisplayName("카테고리 품종 조회 API")
    @Test
    void getNames() throws Exception {
        List<String> names = List.of("하젤", "도미니카", "빅토리아", "클라린스");

        given(categoryQueryService.getNamesForMember(anyString(), anyString()))
            .willReturn(names);

        mockMvc.perform(
                get("/admin-service/categories/name")
                    .param("code", "절화")
                    .param("type", "품목")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("category-name-search",
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("code")
                        .description("구분코드"),
                    parameterWithName("type")
                        .description("품목명")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data[]").type(JsonFieldType.ARRAY)
                        .description("품종명")
                )
            ));
    }
}
