package com.kkoch.admin.api.service.plant;

import com.kkoch.admin.api.controller.plant.response.PlantNameResponse;
import com.kkoch.admin.api.controller.plant.response.PlantResponse;
import com.kkoch.admin.domain.plant.repository.PlantQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PlantQueryService {

    private final PlantQueryRepository plantQueryRepository;

    public List<PlantResponse> getPlants() {
        return plantQueryRepository.getPlants().stream().map(PlantResponse::of).collect(Collectors.toList());
    }

    public Long getPlantId(String type, String name) {
        return plantQueryRepository.findPlantId(type, name);
    }

    public List<PlantNameResponse> getPlantNames(List<Long> plantIds) {
        return plantQueryRepository.findPlantNames(plantIds);
    }
}
