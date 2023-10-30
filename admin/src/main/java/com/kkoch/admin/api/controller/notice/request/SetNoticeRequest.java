package com.kkoch.admin.api.controller.notice.request;

import com.kkoch.admin.api.service.notice.dto.SetNoticeDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class SetNoticeRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    public SetNoticeDto toSetNoticeDto() {
        return SetNoticeDto.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
