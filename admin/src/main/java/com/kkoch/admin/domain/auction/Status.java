package com.kkoch.admin.domain.auction;

import lombok.Getter;

@Getter
public enum Status {

    READY("준비 중"), OPEN("진행 중"), CLOSE("마감");

    private final String text;

    Status(String text) {
        this.text = text;
    }
}
