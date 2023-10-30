package com.kkoch.admin.api.controller.admin;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginAdmin {

    private Long id;
    private String authority;

    @Builder
    public LoginAdmin(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }
}
