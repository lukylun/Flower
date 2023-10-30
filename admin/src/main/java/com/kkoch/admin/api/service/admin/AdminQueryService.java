package com.kkoch.admin.api.service.admin;

import com.kkoch.admin.api.controller.admin.LoginAdmin;
import com.kkoch.admin.api.controller.admin.response.AdminResponse;
import com.kkoch.admin.api.service.admin.dto.LoginDto;
import com.kkoch.admin.client.UserServiceClient;
import com.kkoch.admin.client.response.MemberResponseForAdmin;
import com.kkoch.admin.domain.admin.repository.AdminQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminQueryService {

    private final AdminQueryRepository adminQueryRepository;
    private final UserServiceClient userServiceClient;
    public List<AdminResponse> getAdmins() {
        return adminQueryRepository.getAdmins();
    }

    public LoginAdmin loginAdmin(LoginDto dto) {
        return adminQueryRepository.getLoginAdmin(dto.getLoginId(), dto.getLoginPw())
            .orElseThrow(() -> new NoSuchElementException("아이디와 비밀번호를 확인하세요"));
    }

    public List<MemberResponseForAdmin> getUsers(){
        return userServiceClient.getUsers();
    }


}
