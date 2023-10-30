package com.kkoch.admin.api.controller.auction.request;

import com.kkoch.admin.api.service.auction.dto.AddAuctionDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AddAuctionRequest {

    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private Integer code;

    public AddAuctionRequest(LocalDateTime startTime, Integer code) {
        this.startTime = startTime;
        this.code = code;
    }

    public AddAuctionDto toAddAuctionDto() {
        return AddAuctionDto.builder()
                .code(this.code)
                .startTime(this.startTime)
                .build();
    }
}
