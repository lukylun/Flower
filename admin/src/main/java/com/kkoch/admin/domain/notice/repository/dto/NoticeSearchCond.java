package com.kkoch.admin.domain.notice.repository.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class NoticeSearchCond {

    private String title;
    private String content;

    @Builder
    private NoticeSearchCond(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
