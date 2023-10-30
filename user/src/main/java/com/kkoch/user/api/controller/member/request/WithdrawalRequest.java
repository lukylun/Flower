package com.kkoch.user.api.controller.member.request;

import lombok.Builder;
import lombok.Data;

@Data
public class WithdrawalRequest {

    private String pwd;

    @Builder
    private WithdrawalRequest(String pwd) {
        this.pwd = pwd;
    }
}
