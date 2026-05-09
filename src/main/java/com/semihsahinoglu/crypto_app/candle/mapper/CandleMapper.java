package com.semihsahinoglu.crypto_app.candle.mapper;

import com.semihsahinoglu.crypto_app.candle.entity.BinanceProperties;
import com.semihsahinoglu.crypto_app.candle.entity.Candle;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CandleMapper {

    private final BinanceProperties binanceProperties;

    public CandleMapper(BinanceProperties binanceProperties) {
        this.binanceProperties = binanceProperties;
    }

    public Candle mapToCandle(List<Object> data) {

        return Candle.builder()
                .symbol(binanceProperties.getApp().getTrading().getSymbol())
                .interval(binanceProperties.getApp().getTrading().getInterval())
                .openTime(((Number) data.get(0)).longValue())
                .open(new BigDecimal((String) data.get(1)))
                .high(new BigDecimal((String) data.get(2)))
                .low(new BigDecimal((String) data.get(3)))
                .close(new BigDecimal((String) data.get(4)))
                .volume(new BigDecimal((String) data.get(5)))
                .closeTime(((Number) data.get(6)).longValue())
                .build();
    }
}
