package com.kkoch.admin.api.controller.admin.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class AdminResponse {
    private  Long adminId;
    private String loginId;
    private String loginPw;
    private String name;
    private String tel;
    private String position;
    private String createdDate;
    private boolean active;

    @Builder
    public AdminResponse(Long adminId, String loginId, String loginPw, String name, String tel, String position, LocalDateTime createdDate, boolean active) {
        this.adminId = adminId;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.name = name;
        this.tel = tel;
        this.position = position;
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.active = active;
    }
}
