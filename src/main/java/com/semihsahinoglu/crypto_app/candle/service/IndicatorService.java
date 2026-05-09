package com.semihsahinoglu.crypto_app.candle.service;

import com.semihsahinoglu.crypto_app.candle.dto.IndicatorResponse;
import com.semihsahinoglu.crypto_app.candle.entity.Candle;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class IndicatorService {

    public IndicatorResponse calculateIndicators(List<Candle> candles) {

        if (candles == null || candles.size() < 50) return new IndicatorResponse(0, 0, 0, false, 0, 0);

        Collections.reverse(candles); // kronolojik sıraya çevir

        double rsi = calculateRSI(candles);
        double ema50 = calculateEMA(candles, 50);
        double avgVol = averageVolume(candles, 20);

        boolean volumeIncreasing = isVolumeIncreasing(candles);

        double support = calculateSupport(candles, 50);
        double resistance = calculateResistance(candles, 50);

        return new IndicatorResponse(rsi, ema50, avgVol, volumeIncreasing, support, resistance);
    }

    // RSI (14)
    private double calculateRSI(List<Candle> candles) {

        int period = 14;

        if (candles.size() < period + 1) return 50;

        double gain = 0;
        double loss = 0;

        int start = candles.size() - period;

        for (int i = start; i < candles.size(); i++) {
            double change = candles.get(i).getClose().doubleValue() - candles.get(i - 1).getClose().doubleValue();

            if (change > 0) gain += change;
            else loss += Math.abs(change);
        }

        double avgGain = gain / period;
        double avgLoss = loss / period;

        if (avgLoss == 0) return 100;

        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    // EMA
    private double calculateEMA(List<Candle> candles, int period) {

        if (candles.size() < period) return 0;

        double multiplier = 2.0 / (period + 1);

        double ema = candles.get(0).getClose().doubleValue();

        for (int i = 1; i < candles.size(); i++) {
            double price = candles.get(i).getClose().doubleValue();
            ema = ((price - ema) * multiplier) + ema;
        }

        return ema;
    }

    // Ortalama Volume (son N mum)
    private double averageVolume(List<Candle> candles, int period) {

        if (candles.size() < period) return 0;

        return candles.stream()
                .skip(candles.size() - period)
                .mapToDouble(c -> c.getVolume().doubleValue())
                .average()
                .orElse(0);
    }

    // Volume artıyor mu
    private boolean isVolumeIncreasing(List<Candle> candles) {

        if (candles.size() < 2) return false;

        double last = candles.get(candles.size() - 1).getVolume().doubleValue();
        double prev = candles.get(candles.size() - 2).getVolume().doubleValue();

        return last > prev;
    }

    // Support (son N mum en düşük)
    private double calculateSupport(List<Candle> candles, int period) {

        return candles.stream()
                .skip(Math.max(0, candles.size() - period))
                .mapToDouble(c -> c.getLow().doubleValue())
                .min()
                .orElse(0);
    }

    // Resistance (son N mum en yüksek)
    private double calculateResistance(List<Candle> candles, int period) {

        return candles.stream()
                .skip(Math.max(0, candles.size() - period))
                .mapToDouble(c -> c.getHigh().doubleValue())
                .max()
                .orElse(0);
    }
}