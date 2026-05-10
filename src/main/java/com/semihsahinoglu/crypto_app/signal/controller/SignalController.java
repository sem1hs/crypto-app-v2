package com.semihsahinoglu.crypto_app.signal.controller;

import com.semihsahinoglu.crypto_app.signal.entity.Signal;
import com.semihsahinoglu.crypto_app.signal.service.SignalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/signal")
public class SignalController {

    private final SignalService signalService;

    public SignalController(SignalService signalService) {
        this.signalService = signalService;
    }

    @GetMapping
    public ResponseEntity<Signal> getSignal(@RequestParam(defaultValue = "BTCTRY") String symbol, @RequestParam(defaultValue = "5m") String interval) {
        Signal signal = signalService.getSignal(symbol, interval);
        return ResponseEntity.status(HttpStatus.OK).body(signal);
    }
}