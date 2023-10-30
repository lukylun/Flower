package com.kkoch.admin.api.service.plant;

import com.kkoch.admin.domain.plant.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PlantService {

    private final PlantRepository plantRepository;

}
