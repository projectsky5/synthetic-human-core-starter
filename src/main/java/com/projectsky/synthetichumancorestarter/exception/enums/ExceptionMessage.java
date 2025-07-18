package com.projectsky.synthetichumancorestarter.exception.enums;

import lombok.Getter;

public enum ExceptionMessage {
    FULL("Очередь задач переполнена");

    @Getter
    private final String value;

    ExceptionMessage(String value) {
        this.value = value;
    }
}
