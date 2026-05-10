package com.semihsahinoglu.crypto_app.candle.client;

import com.semihsahinoglu.crypto_app.candle.entity.BinanceProperties;
import com.semihsahinoglu.crypto_app.candle.entity.Candle;
import com.semihsahinoglu.crypto_app.candle.mapper.CandleMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
public class BinanceClient {

    private final WebClient webClient;
    private final BinanceProperties binanceProperties;
    private final CandleMapper candleMapper;

    public BinanceClient(@Qualifier("binanceWebClient") WebClient webClient, BinanceProperties binanceProperties, CandleMapper candleMapper) {
        this.webClient = webClient;
        this.binanceProperties = binanceProperties;
        this.candleMapper = candleMapper;
    }

    public List<Candle> getCandles() {
        List<List<Object>> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v3/klines")
                        .queryParam("symbol", binanceProperties.getApp().getTrading().getSymbol())
                        .queryParam("interval", binanceProperties.getApp().getTrading().getInterval())
                        .queryParam("limit", binanceProperties.getApp().getTrading().getLimit())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<List<Object>>>() {
                })
                .block();

        if (response == null || response.isEmpty()) return Collections.emptyList();

        return response.stream().map(candleMapper::mapToCandle).toList();
    }
}
