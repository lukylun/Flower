package com.kkoch.admin.api.controller.category;

import com.kkoch.admin.ControllerTestSupport;
import com.kkoch.admin.api.controller.category.request.AddCategoryRequest;
import com.kkoch.admin.api.controller.category.request.SetCategoryRequest;
import com.kkoch.admin.api.controller.category.response.CategoryResponse;
import com.kkoch.admin.api.service.category.CategoryQueryService;
import com.kkoch.admin.api.service.category.CategoryService;
import com.kkoch.admin.api.service.category.dto.AddCategoryDto;
import com.kkoch.admin.api.service.category.dto.SetCategoryDto;
import com.kkoch.admin.domain.plant.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CategoryController.class})
class CategoryControllerTest extends ControllerTestSupport {

    final String URI = "/admin-service/categories";
    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryQueryService categoryQueryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @DisplayName("관계자는 최상위 카테고리를 등록할 수 있다.")
    @Test
    void addRootCategory() throws Exception {
        //given
        given(categoryService.addCategory(any(AddCategoryDto.class)))
                .willReturn(2L);

        AddCategoryRequest request = AddCategoryRequest.builder()
                .name("장미")
                .parentId(1L)
                .build();

        //when, then
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isNumber());
    }

    @DisplayName("관계자가 카테고리를 등록할 때 이름을 입력하지 않으면 예외가 발생한다.")
    @Test
    void addSubCategory() throws Exception {
        //given
        AddCategoryRequest request = AddCategoryRequest.builder()
                .name("")
                .build();

        //when, then
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));

    }

    @DisplayName("카테고리를 선택시 하위 카테고리를 조회 할 수 있다.")
    @Test
    void getCategories() throws Exception {
        //given
        List<CategoryResponse> categoryResponseList = new ArrayList<>();

        given(categoryQueryService.getCategoriesByParentId(anyLong()))
                .willReturn(categoryResponseList);

        //when
        mockMvc.perform(get(URI + "/{parentId}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @DisplayName("관계자가 카테고리를 선택하여 카테고리 이름을 수정할 수 있다.")
    @Test
    void setCategory() throws Exception {
        //given
        SetCategoryRequest request = SetCategoryRequest
                .builder()
                .changeName("바뀐 장미")
                .build();
        //when
        given(categoryService.setCategory(anyLong(), any(SetCategoryDto.class)))
                .willReturn(CategoryResponse.builder()
                        .categoryId(1L)
                        .name("바뀐 장미")
                        .level(2)
                        .build());
        //then
        mockMvc.perform(patch(URI + "/{categoryId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("301"))
                .andExpect(jsonPath("$.status").value("MOVED_PERMANENTLY"))
                .andExpect(jsonPath("$.message").value("카테고리가 수정되었습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @DisplayName("관계자는 카테고리를 선택해 삭제할 수 있다. 카테고리 삭제시 하위 카테고리 모두 삭제된다.")
    @Test
    void removeCategory() throws Exception {
        //given
        given(categoryService.removeCategory(anyLong())).willReturn(1L);

        //when, then
        mockMvc.perform(delete(URI + "/{categoryId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("301"))
                .andExpect(jsonPath("$.status").value("MOVED_PERMANENTLY"))
                .andExpect(jsonPath("$.message").value("카테고리가 삭제되었습니다."))
                .andExpect(jsonPath("$.data").isNumber());
    }


}