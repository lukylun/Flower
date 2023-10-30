package com.kkoch.user.client;

import com.kkoch.user.client.response.PlantNameResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "admin-service")
public interface PlantServiceClient {

    @GetMapping("/admin-service/plants/reservation")
    Long getPlantId(@SpringQueryMap Map<String, String> param);

    @GetMapping("/admin-service/plants/names")
    List<PlantNameResponse> getPlantNames(@SpringQueryMap Map<String, List<Long>> param);
}
