package com.kkoch.user.api.controller.member.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SetPasswordRequest {

    private String currentPwd;
    private String newPwd;

    @Builder
    private SetPasswordRequest(String currentPwd, String newPwd) {
        this.currentPwd = currentPwd;
        this.newPwd = newPwd;
    }
}
