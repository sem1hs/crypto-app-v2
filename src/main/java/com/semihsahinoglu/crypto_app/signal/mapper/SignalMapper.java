package com.semihsahinoglu.crypto_app.signal.mapper;

import com.semihsahinoglu.crypto_app.candle.dto.IndicatorResponse;
import com.semihsahinoglu.crypto_app.signal.entity.Signal;
import com.semihsahinoglu.crypto_app.signal.entity.SignalEntity;
import org.springframework.stereotype.Component;

@Component
public class SignalMapper {

    public SignalEntity toEntity(
            Signal signal,
            IndicatorResponse indicators,
            String symbol,
            String interval,
            double currentPrice
    ) {

        return SignalEntity.builder()
                .symbol(symbol)
                .interval(interval)
                .type(signal.type())
                .price(currentPrice)
                .confidence(signal.confidence())
                .reason(signal.reason())
                .rsi(indicators.rsi())
                .ema50(indicators.ema50())
                .supportLevel(indicators.support())
                .resistanceLevel(indicators.resistance())
                .build();
    }
}
