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

    public Signal generateSignal(IndicatorResponse data, double currentPrice, List<Candle> candles) {

        int buyScore = 0;
        int sellScore = 0;

        StringBuilder buyReason = new StringBuilder();
        StringBuilder sellReason = new StringBuilder();

        boolean nearSupport = Math.abs(currentPrice - data.support()) / currentPrice < 0.005;
        boolean nearResistance = Math.abs(currentPrice - data.resistance()) / currentPrice < 0.005;

        boolean trendUp = currentPrice > data.ema50();
        boolean trendDown = currentPrice < data.ema50();

        Candle last = candles.get(candles.size() - 1);
        Candle prev = candles.get(candles.size() - 2);

        boolean hammer = candlePatternService.isHammer(last);
        boolean bullishEngulfing = candlePatternService.isBullishEngulfing(prev, last);
        boolean breakout = candlePatternService.isBreakoutCandle(last, data.resistance());

        boolean bullishTrigger = data.rsi() < 35 || hammer || bullishEngulfing || breakout;
        boolean bearishTrigger = data.rsi() > 70 || (nearResistance && trendDown);

        if (hammer) {
            buyScore += 20;
            buyReason.append("Son mumda hammer formasyonu görüldü. Bu, düşüş sonrası tepki alımı ihtimalini artırır. ");
        }

        if (bullishEngulfing) {
            buyScore += 30;
            buyReason.append("Bullish engulfing formasyonu oluştu. Alıcılar son mumda satıcıları baskılamış görünüyor. ");
        }

        if (breakout) {
            buyScore += 25;
            buyReason.append("Fiyat direnç seviyesini kırmaya çalışıyor. Kırılım devam ederse yükseliş güçlenebilir. ");
        }

        if (data.rsi() < 35) {
            buyScore += 30;
            buyReason.append("RSI düşük seviyede. Coin kısa vadede aşırı satım bölgesine yaklaşmış olabilir. ");
        }

        if (trendUp) {
            buyScore += 30;
            buyReason.append("Fiyat EMA50 üzerinde. Kısa vadeli trend yukarı yönlü görünüyor. ");
        }

        if (data.volumeIncreasing()) {
            buyScore += 20;
            buyReason.append("Hacim artıyor. Bu, hareketin alıcılar tarafından desteklendiğini gösterebilir. ");
        }

        if (nearSupport) {
            buyScore += 20;
            buyReason.append("Fiyat destek bölgesine yakın. Bu bölgede tepki alımı gelme ihtimali var. ");
        }

        if (data.rsi() > 70) {
            sellScore += 35;
            sellReason.append("RSI yüksek seviyede. Coin kısa vadede aşırı alım bölgesine yaklaşmış olabilir. ");
        }

        if (trendDown) {
            sellScore += 30;
            sellReason.append("Fiyat EMA50 altında. Kısa vadeli trend aşağı yönlü görünüyor. ");
        }

        if (nearResistance) {
            sellScore += 20;
            sellReason.append("Fiyat direnç bölgesine yakın. Bu bölgede kar satışı gelme ihtimali var. ");
        }

        if (!data.volumeIncreasing()) {
            sellScore += 15;
            sellReason.append("Hacim zayıf. Yükseliş varsa bile güçlü alıcı desteği görünmüyor. ");
        }

        if (bullishTrigger && buyScore >= 70 && buyScore > sellScore) {
            return new Signal("BUY", buyScore, buyReason.toString());
        }

        if (bearishTrigger && sellScore >= 70 && sellScore > buyScore) {
            return new Signal("SELL", sellScore, sellReason.toString());
        }

        if (buyScore >= 50 && buyScore > sellScore) {
            return new Signal("HOLD", buyScore, buyReason + "Alım ihtimali var ancak trend teyidi henüz yeterince güçlü değil.");
        }

        if (sellScore >= 50 && sellScore > buyScore) {
            return new Signal("HOLD", sellScore, sellReason + "Satış baskısı var ancak net SELL için yeterli güç oluşmadı.");
        }

        return new Signal("HOLD", Math.max(buyScore, sellScore), "Piyasada net yön oluşmadı. Yeni işlem için daha güçlü teyit beklenebilir.");
    }
}