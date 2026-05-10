package com.semihsahinoglu.crypto_app.backtest.service;

import com.semihsahinoglu.crypto_app.backtest.entity.BacktestResult;
import com.semihsahinoglu.crypto_app.candle.dto.IndicatorResponse;
import com.semihsahinoglu.crypto_app.candle.entity.Candle;
import com.semihsahinoglu.crypto_app.candle.repository.CandleRepository;
import com.semihsahinoglu.crypto_app.candle.service.IndicatorService;
import com.semihsahinoglu.crypto_app.signal.entity.Signal;
import com.semihsahinoglu.crypto_app.signal.service.StrategyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BacktestService {

    private final CandleRepository candleRepository;
    private final IndicatorService indicatorService;
    private final StrategyService strategyService;

    public BacktestService(CandleRepository candleRepository, IndicatorService indicatorService, StrategyService strategyService) {
        this.candleRepository = candleRepository;
        this.indicatorService = indicatorService;
        this.strategyService = strategyService;
    }

    public BacktestResult runBacktest(String symbol, String interval) {

        List<Candle> candles = candleRepository.findBySymbolAndIntervalOrderByOpenTimeAsc(symbol, interval);

        int total = 0;
        int success = 0;
        int failed = 0;

        // ilk 100 candle skip
        for (int i = 100; i < candles.size() - 12; i++) {

            List<Candle> slice = candles.subList(i - 100, i);

            Candle current = candles.get(i);

            IndicatorResponse indicators = indicatorService.calculateIndicators(slice);

            Signal signal = strategyService.generateSignal(indicators, current.getClose().doubleValue(), slice);

            double currentPrice = current.getClose().doubleValue();

            double futurePrice = candles.get(i + 12).getClose().doubleValue();

            boolean buySignal = signal.type().equals("BUY") || signal.reason().contains("Alım ihtimali");

            boolean sellSignal = signal.type().equals("SELL") || signal.reason().contains("Satış baskısı");

            if (buySignal) {

                total++;

                if (futurePrice > currentPrice) success++;
                else failed++;

            }

            if (sellSignal) {

                total++;

                if (futurePrice < currentPrice) success++;
                else failed++;
            }
        }

        double winRate = total == 0 ? 0 : ((double) success / total) * 100;

        return new BacktestResult(total, success, failed, winRate);
    }
}