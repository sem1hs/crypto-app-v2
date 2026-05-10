package com.semihsahinoglu.crypto_app.scheduler;

import com.semihsahinoglu.crypto_app.candle.service.CandleService;
import com.semihsahinoglu.crypto_app.signal.service.SignalService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CryptoScheduler {

    private final CandleService candleService;
    private final SignalService signalService;

    public CryptoScheduler(CandleService candleService, SignalService signalService) {
        this.candleService = candleService;
        this.signalService = signalService;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void run() {

        System.out.println("Scheduler çalıştı");

        candleService.fetchAndSaveCandles();
        signalService.getSignal("BTCTRY", "5m");
    }
}