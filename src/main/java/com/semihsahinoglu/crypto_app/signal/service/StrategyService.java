package com.semihsahinoglu.crypto_app.signal.service;

import com.semihsahinoglu.crypto_app.candle.dto.IndicatorResponse;
import com.semihsahinoglu.crypto_app.signal.entity.Signal;
import org.springframework.stereotype.Service;

@Service
public class StrategyService {

    public Signal generateSignal(IndicatorResponse data, double currentPrice) {

        int score = 0;
        StringBuilder reason = new StringBuilder();

        boolean nearSupport =
                Math.abs(currentPrice - data.support()) / currentPrice < 0.01;

        boolean nearResistance =
                Math.abs(currentPrice - data.resistance()) / currentPrice < 0.01;

        // RSI
        if (data.rsi() < 30) {
            score += 30;
            reason.append("RSI aşırı satım, ");
        }

        // RSI yüksek ama direkt SELL değil
        if (data.rsi() > 70) {
            if (nearResistance && data.volumeIncreasing()) {
                return new Signal("SELL", 85, "RSI yüksek + direnç + hacim artışı");
            }
        }

        // Trend
        if (currentPrice > data.ema50()) {
            score += 30;
            reason.append("Trend yukarı, ");
        }

        // Volume
        if (data.volumeIncreasing()) {
            score += 20;
            reason.append("Hacim artıyor, ");
        }

        // Support
        if (nearSupport) {
            score += 20;
            reason.append("Destek bölgesine yakın, ");
        }

        // Resistance artık direkt SELL değil
        if (nearResistance) {
            reason.append("Direnç bölgesine yakın, ");
        }

        // BUY
        if (score >= 70 && !nearResistance) {
            return new Signal("BUY", score, reason.toString());
        }

        // Weak SELL
        if (nearResistance && data.rsi() > 65) {
            return new Signal("SELL", 60, "Dirençten zayıf dönüş sinyali");
        }

        return new Signal("HOLD", score, "Güçlü bir sinyal yok");
    }
}