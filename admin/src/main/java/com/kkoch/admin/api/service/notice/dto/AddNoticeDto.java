package com.kkoch.admin.api.service.notice.dto;

import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.notice.Notice;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddNoticeDto {

    private String title;
    private String content;

    @Builder
    private AddNoticeDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Notice toEntity(Long adminId) {
        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .active(true)
                .admin(Admin.toEntity(adminId))
                .build();
    }
}
