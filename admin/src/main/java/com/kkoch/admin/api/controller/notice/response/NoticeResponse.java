package com.kkoch.admin.api.controller.notice.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NoticeResponse {

    private Integer no;
    private Long noticeId;
    private String title;
    private String content;
    private Boolean active;

    @Builder
    public NoticeResponse(Long noticeId, String title, String content, Boolean active) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.active = active;
    }
}
