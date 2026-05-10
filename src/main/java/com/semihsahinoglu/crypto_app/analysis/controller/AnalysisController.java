package com.semihsahinoglu.crypto_app.analysis.controller;

import com.semihsahinoglu.crypto_app.analysis.service.AnalysisService;
import com.semihsahinoglu.crypto_app.candle.dto.IndicatorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/indicators")
    public ResponseEntity<IndicatorResponse> getIndicators(@RequestParam(defaultValue = "BTCTRY") String symbol, @RequestParam(defaultValue = "5m") String interval) {
        IndicatorResponse response = analysisService.getIndicators(symbol, interval);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}