package com.kkoch.admin.api.controller.admin.request;

import com.kkoch.admin.api.service.admin.dto.EditAdminDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class EditAdminRequest {

    @NotBlank
    private String loginPw;
    @NotBlank
    private String tel;

    @Builder
    public EditAdminRequest(String loginPw, String tel) {
        this.loginPw = loginPw;
        this.tel = tel;
    }

    public EditAdminDto toEditAdminDto() {
        EditAdminDto dto = EditAdminDto.builder()
                .loginPw(this.loginPw)
                .tel(this.tel)
                .build();
        return dto;
    }
}
