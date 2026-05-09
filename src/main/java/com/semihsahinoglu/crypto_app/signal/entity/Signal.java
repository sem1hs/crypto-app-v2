package com.semihsahinoglu.crypto_app.signal.entity;

public record Signal(
        String type,
        int confidence,
        String reason
) {
}
