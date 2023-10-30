package com.kkoch.admin.api.controller.notice.request;

import com.kkoch.admin.api.service.notice.dto.AddNoticeDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class AddNoticeRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Builder
    private AddNoticeRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public AddNoticeDto toAddNoticeDto() {
        return AddNoticeDto.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }

}
