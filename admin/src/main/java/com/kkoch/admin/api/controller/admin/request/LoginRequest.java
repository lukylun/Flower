package com.kkoch.admin.api.controller.admin.request;

import com.kkoch.admin.api.service.admin.dto.LoginDto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class LoginRequest {

    @NotBlank
    private String loginId;
    @NotBlank
    private String loginPw;

    @Builder
    public LoginRequest(String loginId, String loginPw) {
        this.loginId = loginId;
        this.loginPw = loginPw;
    }

    public LoginDto toLoginDto() {
        LoginDto dto = LoginDto.builder()
                .loginId(this.loginId)
                .loginPw(this.loginPw)
                .build();
        return dto;
    }
}
