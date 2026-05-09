package com.semihsahinoglu.crypto_app.analysis.service;

import com.semihsahinoglu.crypto_app.candle.dto.IndicatorResponse;
import com.semihsahinoglu.crypto_app.candle.entity.Candle;
import com.semihsahinoglu.crypto_app.candle.repository.CandleRepository;
import com.semihsahinoglu.crypto_app.candle.service.IndicatorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisService {

    private final CandleRepository candleRepository;
    private final IndicatorService indicatorService;

    public AnalysisService(CandleRepository candleRepository, IndicatorService indicatorService) {
        this.candleRepository = candleRepository;
        this.indicatorService = indicatorService;
    }

    public IndicatorResponse getIndicators(String symbol, String interval) {

        List<Candle> candles = candleRepository.findTop100BySymbolAndIntervalOrderByOpenTimeDesc(symbol, interval);

        return indicatorService.calculateIndicators(candles);
    }
}
