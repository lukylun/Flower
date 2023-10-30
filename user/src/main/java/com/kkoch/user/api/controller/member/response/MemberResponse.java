package com.kkoch.user.api.controller.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kkoch.user.domain.member.Member;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponse {

    private String email;
    private String name;
    private String memberKey;

    @Builder
    private MemberResponse(String email, String name, String memberKey) {
        this.email = email;
        this.name = name;
        this.memberKey = memberKey;
    }

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
            .email(member.getEmail())
            .name(member.getName())
            .memberKey(member.getMemberKey())
            .build();
    }
}
