package com.kkoch.user.api.controller.reservation.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class SetReservationRequest {

    @NonNull
    private Integer count;
    @NonNull
    private Integer price;

}
