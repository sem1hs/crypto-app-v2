package com.semihsahinoglu.crypto_app.common.config;

import com.semihsahinoglu.crypto_app.candle.entity.BinanceProperties;
import com.semihsahinoglu.crypto_app.telegram.entity.TelegramProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final BinanceProperties binanceProperties;
    private final TelegramProperties telegramProperties;

    public WebClientConfig(BinanceProperties binanceProperties, TelegramProperties telegramProperties) {
        this.binanceProperties = binanceProperties;
        this.telegramProperties = telegramProperties;
    }

    @Bean
    public WebClient binanceWebClient() {
        ExchangeFilterFunction logFilter = (request, next) -> {
            System.out.println("Request: " + request.url());
            return next.exchange(request).doOnNext(response -> System.out.println("Response status: " + response.statusCode()));
        };

        return WebClient.builder()
                .baseUrl(binanceProperties.getBaseUrl())
                .filter(logFilter)
                .build();
    }

    @Bean
    public WebClient telegramWebClient() {
        ExchangeFilterFunction logFilter = (request, next) -> {
            System.out.println("Request: " + request.url());
            return next.exchange(request).doOnNext(response -> System.out.println("Response status: " + response.statusCode()));
        };

        return WebClient.builder()
                .baseUrl("https://api.telegram.org")
                .filter(logFilter)
                .build();
    }
}