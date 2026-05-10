package com.semihsahinoglu.crypto_app.signal.service;

import com.semihsahinoglu.crypto_app.candle.dto.IndicatorResponse;
import com.semihsahinoglu.crypto_app.candle.entity.Candle;
import com.semihsahinoglu.crypto_app.candle.repository.CandleRepository;
import com.semihsahinoglu.crypto_app.candle.service.IndicatorService;
import com.semihsahinoglu.crypto_app.signal.entity.Signal;
import com.semihsahinoglu.crypto_app.signal.entity.SignalEntity;
import com.semihsahinoglu.crypto_app.signal.mapper.SignalMapper;
import com.semihsahinoglu.crypto_app.signal.repository.SignalRepository;
import com.semihsahinoglu.crypto_app.telegram.service.TelegramService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SignalService {

    private final CandleRepository candleRepository;
    private final IndicatorService indicatorService;
    private final StrategyService strategyService;
    private final SignalRepository signalRepository;
    private final SignalMapper signalMapper;
    private final TelegramService telegramService;

    public SignalService(CandleRepository candleRepository, IndicatorService indicatorService, StrategyService strategyService, SignalRepository signalRepository, SignalMapper signalMapper, TelegramService telegramService) {
        this.candleRepository = candleRepository;
        this.indicatorService = indicatorService;
        this.strategyService = strategyService;
        this.signalRepository = signalRepository;
        this.signalMapper = signalMapper;
        this.telegramService = telegramService;
    }

    public Signal getSignal(String symbol, String interval) {

        List<Candle> candles = candleRepository.findTop100BySymbolAndIntervalOrderByOpenTimeDesc(symbol, interval);

        if (candles.isEmpty()) return new Signal("HOLD", 0, "No data");

        Collections.reverse(candles);

        IndicatorResponse indicators = indicatorService.calculateIndicators(candles);

        double currentPrice = candles.get(candles.size() - 1).getClose().doubleValue();

        Signal signal = strategyService.generateSignal(indicators, currentPrice, candles);

        SignalEntity signalEntity = signalMapper.toEntity(signal, indicators, symbol, interval, currentPrice);

        SignalEntity savedEntity = signalRepository.save(signalEntity);

        String message = """
            🚨 KRIPTO SIGNAL
            
            Coin: %s
            Type: %s
            Price: %.2f
            Confidence: %d
            
            📊 Indicators
            
            RSI: %.2f
            EMA50: %.2f
            Avg Volume: %.2f
            Volume Increasing: %s
            Support: %.2f
            Resistance: %.2f
            
            🧠 Reason:
            %s
            """
                .formatted(
                        symbol,
                        signal.type(),
                        currentPrice,
                        signal.confidence(),

                        indicators.rsi(),
                        indicators.ema50(),
                        indicators.avgVolume(),
                        indicators.volumeIncreasing(),
                        indicators.support(),
                        indicators.resistance(),

                        signal.reason()
                );

        telegramService.sendMessage(message);

        return signal;
    }
}