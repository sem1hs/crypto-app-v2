package com.semihsahinoglu.crypto_app.telegram.service;

import com.semihsahinoglu.crypto_app.telegram.entity.TelegramProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramService {

    private final WebClient webClient;
    private final TelegramProperties telegramProperties;

    public TelegramService(@Qualifier("telegramWebClient") WebClient webClient, TelegramProperties telegramProperties) {
        this.webClient = webClient;
        this.telegramProperties = telegramProperties;
    }

    public void sendMessage(String text) {

        Map<String, String> body = new HashMap<>();

        body.put("chat_id", telegramProperties.getChat().getId());
        body.put("text", text);

        webClient.post()
                .uri("/bot" + telegramProperties.getBot().getToken() + "/sendMessage")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}