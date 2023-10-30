package com.kkoch.admin.api.controller.auction.request;

import com.kkoch.admin.api.service.auction.dto.SetAuctionDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SetAuctionRequest {

    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private Integer code;

    @Builder
    private SetAuctionRequest(LocalDateTime startTime, Integer code) {
        this.startTime = startTime;
        this.code = code;
    }

    public SetAuctionDto toSetAuctionDto() {
        return SetAuctionDto.builder()
                .code(this.code)
                .startTime(this.startTime)
                .build();
    }

//    public SetAuctionDto toSetAuctionDto(SetAuctionRequest request, LocalDateTime startTime) {
//        return SetAuctionDto.builder()
//                .code(request.getCode())
//                .startTime(startTime)
//                .build();
//    }
}
