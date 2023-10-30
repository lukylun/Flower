package com.kkoch.admin.api.controller.plant;

import com.kkoch.admin.ControllerTestSupport;
import com.kkoch.admin.api.controller.plant.request.GetPlantRequest;
import com.kkoch.admin.api.controller.plant.response.PlantResponse;
import com.kkoch.admin.api.service.plant.PlantQueryService;
import com.kkoch.admin.api.service.plant.dto.PlantSearchCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PlantController.class})
class PlantControllerTest extends ControllerTestSupport {

    final String URI = "/admin-service/plants";

    @MockBean
    private PlantQueryService plantQueryService;

    @DisplayName("관계자는 부류, 품목, 품종으로 식물을 검색 할 수 있다.")
//    @Test
    void getPlants() throws Exception {
        //given
        GetPlantRequest request = GetPlantRequest.builder()
                .code("절화")
                .name("장미")
                .build();

        List<PlantResponse> responses = List.of();
        //when
//        given(plantQueryService.getPlant(any(PlantSearchCond.class))).willReturn(responses);

        //then
        mockMvc.perform(
                        get(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray());
    }

}