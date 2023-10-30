package com.kkoch.user.api.controller.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponseForAdmin {

    private Long id;
    private String email;
    private String name;
    private String tel;
    private String businessNumber;
    private int point;
    private boolean active;

    @Builder
    public MemberResponseForAdmin(Long id, String email, String name, String tel, String businessNumber, int point, boolean active) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.tel = tel;
        this.businessNumber = businessNumber;
        this.point = point;
        this.active = active;
    }
}
