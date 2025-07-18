package com.projectsky.synthetichumancorestarter.exception.dto;

import lombok.Builder;

@Builder
public record SubError(
        String object,
        String field,
        Object rejectedValue,
        String message
) {
}