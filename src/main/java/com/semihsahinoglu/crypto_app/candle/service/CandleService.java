package com.semihsahinoglu.crypto_app.candle.service;

import com.semihsahinoglu.crypto_app.candle.client.BinanceClient;
import com.semihsahinoglu.crypto_app.candle.entity.BinanceProperties;
import com.semihsahinoglu.crypto_app.candle.entity.Candle;
import com.semihsahinoglu.crypto_app.candle.repository.CandleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandleService {

    private final BinanceClient binanceClient;
    private final CandleRepository candleRepository;
    private final BinanceProperties properties;

    public CandleService(BinanceClient binanceClient, CandleRepository candleRepository, BinanceProperties properties) {
        this.binanceClient = binanceClient;
        this.candleRepository = candleRepository;
        this.properties = properties;
    }

    public void fetchAndSaveCandles() {
        List<Candle> candles = binanceClient.getCandles();

        if (candles.isEmpty()) return;

        int savedCount = 0;

        for (Candle candle : candles) {
            boolean exists = candleRepository.existsBySymbolAndIntervalAndOpenTime(candle.getSymbol(), candle.getInterval(), candle.getOpenTime());

            if (!exists) {
                candleRepository.save(candle);
                savedCount++;
            }
        }
        System.out.println("Saved candles: " + savedCount);
    }
}
