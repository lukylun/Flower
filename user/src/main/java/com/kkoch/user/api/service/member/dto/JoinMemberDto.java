package com.kkoch.user.api.service.member.dto;

import com.kkoch.user.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinMemberDto {

    private String email;
    private String pwd;
    private String name;
    private String tel;
    private String businessNumber;
    private String memberKey;

    @Builder
    private JoinMemberDto(String email, String pwd, String name, String tel, String businessNumber, String memberKey) {
        this.email = email;
        this.pwd = pwd;
        this.name = name;
        this.tel = tel;
        this.businessNumber = businessNumber;
        this.memberKey = memberKey;
    }

    public Member toEntity(String encryptedPwd) {
        return Member.builder()
            .email(this.email)
            .encryptedPwd(encryptedPwd)
            .name(this.name)
            .tel(this.tel)
            .businessNumber(this.businessNumber)
            .point(0)
            .active(true)
            .memberKey(this.memberKey)
            .build();
    }
}
