package com.semihsahinoglu.crypto_app.common.config;

import com.semihsahinoglu.crypto_app.candle.entity.BinanceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final BinanceProperties binanceProperties;

    public WebClientConfig(BinanceProperties binanceProperties) {
        this.binanceProperties = binanceProperties;
    }

    @Bean
    public WebClient webClient() {
        ExchangeFilterFunction logFilter = (request, next) -> {
            System.out.println("Request: " + request.url());
            return next.exchange(request).doOnNext(response -> System.out.println("Response status: " + response.statusCode()));
        };

        return WebClient.builder()
                .baseUrl(binanceProperties.getBaseUrl())
                .filter(logFilter)
                .build();
    }
}