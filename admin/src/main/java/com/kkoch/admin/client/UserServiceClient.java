package com.kkoch.admin.client;

import com.kkoch.admin.client.response.MemberResponseForAdmin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name ="user-service")
public interface UserServiceClient {

    @GetMapping("/members")
    List<MemberResponseForAdmin> getUsers();

}
