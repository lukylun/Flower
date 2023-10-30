package com.kkoch.admin.api.service.admin.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditAdminDto {

    private String loginPw;
    private String tel;

    @Builder
    private EditAdminDto(String loginPw, String tel) {
        this.loginPw = loginPw;
        this.tel = tel;
    }

}
