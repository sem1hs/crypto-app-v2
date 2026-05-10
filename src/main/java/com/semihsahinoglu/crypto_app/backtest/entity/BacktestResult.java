package com.semihsahinoglu.crypto_app.backtest.entity;

public record BacktestResult(
        int totalSignals,
        int successfulSignals,
        int failedSignals,
        double winRate

) {
}