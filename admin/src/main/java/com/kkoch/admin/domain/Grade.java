package com.kkoch.admin.domain;

import lombok.Getter;

@Getter
public enum Grade {
    NONE("테스트"),
    SUPER("특급"),
    ADVANCED("상급"),
    NORMAL("보통");


    private final String text;

    Grade(String text) {
        this.text = text;
    }
}
