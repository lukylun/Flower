package com.kkoch.user.api.controller.alarm.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class AlarmResponse {

    private Long alarmId;
    private String content;
    private boolean open;
    private String createdDate;

    @Builder
    public AlarmResponse(Long alarmId, String content, boolean open, LocalDateTime createdDate) {
        this.alarmId = alarmId;
        this.content = content;
        this.open = open;
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm"));
    }
}
