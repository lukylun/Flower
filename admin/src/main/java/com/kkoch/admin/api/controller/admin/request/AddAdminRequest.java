package com.kkoch.admin.api.controller.admin.request;

import com.kkoch.admin.api.service.admin.dto.AddAdminDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class AddAdminRequest {

    @NotBlank
    private String loginId;
    @NotBlank
    private String loginPw;
    @NotBlank
    private String name;
    @NotBlank
    private String tel;
    @NotBlank
    private String position;

    @Builder
    private AddAdminRequest(String loginId, String loginPw, String name, String tel, String position) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.name = name;
        this.tel = tel;
        this.position = position;
    }

    public AddAdminDto toAddAdminDto() {
        AddAdminDto dto = AddAdminDto.builder()
                .loginId(this.loginId)
                .loginPw(this.loginPw)
                .name(this.name)
                .tel(this.tel)
                .position(this.position)
                .build();
        return dto;
    }
}
