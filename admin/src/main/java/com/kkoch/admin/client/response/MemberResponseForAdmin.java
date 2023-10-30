package com.kkoch.admin.client.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberResponseForAdmin {

    private Long id;
    private String email;
    private String name;
    private String tel;
    private String businessNumber;
    private int point;
    private boolean active;

}
