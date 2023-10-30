package com.kkoch.user.api.controller.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberInfoResponse {

    private String email;
    private String name;
    private String tel;
    private String businessNumber;

    @Builder
    public MemberInfoResponse(String email, String name, String tel, String businessNumber) {
        this.email = email;
        this.name = name;
        this.tel = tel;
        this.businessNumber = businessNumber;
    }
}
