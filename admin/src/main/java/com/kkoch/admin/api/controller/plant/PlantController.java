package com.kkoch.admin.api.controller.plant;

import com.kkoch.admin.api.controller.plant.response.PlantResponse;
import com.kkoch.admin.api.service.plant.PlantQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/admin-service/intranet")
public class PlantController {

    private final PlantQueryService plantQueryService;

    @GetMapping("/plants")
    public String plantPage(Model model) {
        log.info("<식물 목록 요청> Controller");
        List<PlantResponse> responses = plantQueryService.getPlants();
        model.addAttribute("plants", responses);
        return "plant";
    }
}