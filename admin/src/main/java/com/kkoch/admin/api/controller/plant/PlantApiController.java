package com.kkoch.admin.api.controller.plant;

import com.kkoch.admin.api.controller.plant.response.PlantNameResponse;
import com.kkoch.admin.api.service.plant.PlantQueryService;
import com.kkoch.admin.api.service.plant.PlantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin-service/plants")
@RestController
@Slf4j
public class PlantApiController {

    private final PlantQueryService plantQueryService;

    @GetMapping("/reservation")
    public Long getPlantId(
            @RequestParam String type,
            @RequestParam String name
    ) {
        log.info("<식물 pk 요청> Controller type={}, name={}", type, name);
        return plantQueryService.getPlantId(type, name);
    }

    @GetMapping("/names")
    public List<PlantNameResponse> getPlantNames(
            @RequestParam List<Long> plantIds
    ) {
        log.info("call PlantApiController.getPlantNames");
        log.info("plantIds={}", plantIds);
        List<PlantNameResponse> responses = plantQueryService.getPlantNames(plantIds);
        log.info("response size={}", responses.size());
        return responses;
    }
}