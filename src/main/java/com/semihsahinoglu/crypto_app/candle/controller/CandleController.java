package com.semihsahinoglu.crypto_app.candle.controller;

import com.semihsahinoglu.crypto_app.candle.service.CandleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/candle")
public class CandleController {

    private final CandleService candleService;

    public CandleController(CandleService candleService) {
        this.candleService = candleService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<String> fetch() {
        candleService.fetchAndSaveCandles();
        return ResponseEntity.status(HttpStatus.OK).body("Ok");
    }
}