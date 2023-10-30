package com.kkoch.admin.api.service.notice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SetNoticeDto {

    private String title;
    private String content;

    @Builder
    private SetNoticeDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
