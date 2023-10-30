package com.kkoch.user.api.service.member.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginMemberDto {

    private String email;

    private String loginPw;

    @Builder
    private LoginMemberDto(String email, String loginPw) {
        this.email = email;
        this.loginPw = loginPw;
    }
}
