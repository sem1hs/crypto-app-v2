package com.semihsahinoglu.crypto_app.candle.service;

import com.semihsahinoglu.crypto_app.candle.entity.Candle;
import org.springframework.stereotype.Service;

@Service
public class CandlePatternService {

    // 🔥 Hammer Pattern
    public boolean isHammer(Candle candle) {

        double open = candle.getOpen().doubleValue();
        double close = candle.getClose().doubleValue();
        double high = candle.getHigh().doubleValue();
        double low = candle.getLow().doubleValue();

        double body = Math.abs(close - open);
        double lowerWick = Math.min(open, close) - low;
        double upperWick = high - Math.max(open, close);

        return lowerWick > body * 2 && upperWick < body;
    }

    // 🔥 Bullish Engulfing
    public boolean isBullishEngulfing(Candle prev, Candle current) {

        double prevOpen = prev.getOpen().doubleValue();
        double prevClose = prev.getClose().doubleValue();

        double currOpen = current.getOpen().doubleValue();
        double currClose = current.getClose().doubleValue();

        boolean prevBearish = prevClose < prevOpen;
        boolean currentBullish = currClose > currOpen;

        boolean engulfing = currOpen < prevClose && currClose > prevOpen;

        return prevBearish && currentBullish && engulfing;
    }

    // 🔥 Breakout Candle
    public boolean isBreakoutCandle(Candle candle, double resistance) {
        double close = candle.getClose().doubleValue();

        return close > resistance;
    }
}