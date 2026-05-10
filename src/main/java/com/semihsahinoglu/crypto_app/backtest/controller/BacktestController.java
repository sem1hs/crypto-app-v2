package com.semihsahinoglu.crypto_app.backtest.controller;

import com.semihsahinoglu.crypto_app.backtest.entity.BacktestResult;
import com.semihsahinoglu.crypto_app.backtest.service.BacktestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/backtest")
public class BacktestController {

    private final BacktestService backtestService;

    public BacktestController(BacktestService backtestService) {
        this.backtestService = backtestService;
    }

    @GetMapping
    public ResponseEntity<BacktestResult> run() {
        BacktestResult backtestResult = backtestService.runBacktest("BTCTRY", "5m");
        return ResponseEntity.ok(backtestResult);
    }
}
