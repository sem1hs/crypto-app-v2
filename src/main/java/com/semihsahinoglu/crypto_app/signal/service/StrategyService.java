package com.semihsahinoglu.crypto_app.signal.service;

import com.semihsahinoglu.crypto_app.candle.dto.IndicatorResponse;
import com.semihsahinoglu.crypto_app.candle.entity.Candle;
import com.semihsahinoglu.crypto_app.candle.service.CandlePatternService;
import com.semihsahinoglu.crypto_app.signal.entity.Signal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyService {

    private final CandlePatternService candlePatternService;

    public StrategyService(CandlePatternService candlePatternService) {
        this.candlePatternService = candlePatternService;
    }

    public Signal generateSignal(
            IndicatorResponse data,
            double currentPrice,
            List<Candle> candles
    ) {

        int buyScore = 0;
        int sellScore = 0;

        StringBuilder buyReason = new StringBuilder();
        StringBuilder sellReason = new StringBuilder();

        boolean nearSupport =
                Math.abs(currentPrice - data.support()) / currentPrice < 0.005;

        boolean nearResistance =
                Math.abs(currentPrice - data.resistance()) / currentPrice < 0.005;

        boolean trendUp = currentPrice > data.ema50();
        boolean trendDown = currentPrice < data.ema50();

        Candle last = candles.get(candles.size() - 1);
        Candle prev = candles.get(candles.size() - 2);

        // =========================
        // PATTERN DETECTION
        // =========================

        boolean hammer =
                candlePatternService.isHammer(last);

        boolean bullishEngulfing =
                candlePatternService.isBullishEngulfing(prev, last);

        boolean breakout =
                candlePatternService.isBreakoutCandle(
                        last,
                        data.resistance()
                );

        // BUY trigger şartı
        boolean bullishTrigger =
                data.rsi() < 35
                        || hammer
                        || bullishEngulfing
                        || breakout;

        // SELL trigger şartı
        boolean bearishTrigger =
                data.rsi() > 70
                        || (nearResistance && trendDown);

        // =========================
        // BUY CONDITIONS
        // =========================

        if (hammer) {
            buyScore += 20;
            buyReason.append("Hammer pattern, ");
        }

        if (bullishEngulfing) {
            buyScore += 30;
            buyReason.append("Bullish engulfing, ");
        }

        if (breakout) {
            buyScore += 25;
            buyReason.append("Direnç kırılımı, ");
        }

        if (data.rsi() < 35) {
            buyScore += 30;
            buyReason.append("RSI düşük, ");
        }

        if (trendUp) {
            buyScore += 30;
            buyReason.append("Trend yukarı, ");
        }

        if (data.volumeIncreasing()) {
            buyScore += 20;
            buyReason.append("Hacim artıyor, ");
        }

        if (nearSupport) {
            buyScore += 20;
            buyReason.append("Destek bölgesine yakın, ");
        }

        // =========================
        // SELL CONDITIONS
        // =========================

        if (data.rsi() > 70) {
            sellScore += 35;
            sellReason.append("RSI yüksek, ");
        }

        if (trendDown) {
            sellScore += 30;
            sellReason.append("Trend aşağı, ");
        }

        if (nearResistance) {
            sellScore += 20;
            sellReason.append("Direnç bölgesine yakın, ");
        }

        if (!data.volumeIncreasing()) {
            sellScore += 15;
            sellReason.append("Hacim zayıf, ");
        }

        // =========================
        // FINAL DECISION
        // =========================

        // STRONG BUY
        if (
                bullishTrigger
                        && buyScore >= 70
                        && buyScore > sellScore
        ) {
            return new Signal(
                    "BUY",
                    buyScore,
                    buyReason.toString()
            );
        }

        // STRONG SELL
        if (
                bearishTrigger
                        && sellScore >= 70
                        && sellScore > buyScore
        ) {
            return new Signal(
                    "SELL",
                    sellScore,
                    sellReason.toString()
            );
        }

        // WEAK BUY
        if (buyScore >= 50 && buyScore > sellScore) {
            return new Signal(
                    "HOLD",
                    buyScore,
                    "Alım ihtimali var ancak güçlü değil"
            );
        }

        // WEAK SELL
        if (sellScore >= 50 && sellScore > buyScore) {
            return new Signal(
                    "HOLD",
                    sellScore,
                    "Satış baskısı var ancak güçlü değil"
            );
        }

        return new Signal(
                "HOLD",
                Math.max(buyScore, sellScore),
                "Net bir sinyal oluşmadı"
        );
    }
}