package com.semihsahinoglu.crypto_app.signal.service;

import com.semihsahinoglu.crypto_app.candle.dto.IndicatorResponse;
import com.semihsahinoglu.crypto_app.candle.entity.Candle;
import com.semihsahinoglu.crypto_app.candle.repository.CandleRepository;
import com.semihsahinoglu.crypto_app.candle.service.IndicatorService;
import com.semihsahinoglu.crypto_app.signal.entity.Signal;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SignalService {

    private final CandleRepository candleRepository;
    private final IndicatorService indicatorService;
    private final StrategyService strategyService;

    public SignalService(CandleRepository candleRepository, IndicatorService indicatorService, StrategyService strategyService) {
        this.candleRepository = candleRepository;
        this.indicatorService = indicatorService;
        this.strategyService = strategyService;
    }

    public Signal getSignal(String symbol, String interval) {

        List<Candle> candles = candleRepository.findTop100BySymbolAndIntervalOrderByOpenTimeDesc(symbol, interval);

        if (candles.isEmpty()) return new Signal("HOLD", 0, "No data");

        Collections.reverse(candles);

        IndicatorResponse indicators = indicatorService.calculateIndicators(candles);

        double currentPrice = candles.get(candles.size() - 1).getClose().doubleValue();

        return strategyService.generateSignal(indicators, currentPrice);
    }
}