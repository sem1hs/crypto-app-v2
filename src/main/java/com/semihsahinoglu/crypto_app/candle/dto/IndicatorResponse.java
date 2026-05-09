package com.semihsahinoglu.crypto_app.candle.dto;

public record IndicatorResponse(
        double rsi,
        double ema50,
        double avgVolume,
        boolean volumeIncreasing,
        double support,
        double resistance
) {
}
