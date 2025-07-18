package com.projectsky.synthetichumancorestarter.exception.exceptions;

import com.projectsky.synthetichumancorestarter.exception.enums.ExceptionMessage;

public class QueueOverflowException extends RuntimeException {
    public QueueOverflowException() {
        super(ExceptionMessage.FULL.getValue());
    }
}
