package com.projectsky.synthetichumancorestarter.audit.enums;

public enum AuditMode {
    CONSOLE,
    KAFKA;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
