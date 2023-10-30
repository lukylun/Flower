package com.kkoch.user.api.controller.member.request;

import com.kkoch.user.api.service.member.dto.LoginMemberDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class LoginRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @Builder
    private LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginMemberDto toLoginMemberDto() {
        return LoginMemberDto.builder()
                .email(this.email)
                .loginPw(this.password)
                .build();
    }
}
