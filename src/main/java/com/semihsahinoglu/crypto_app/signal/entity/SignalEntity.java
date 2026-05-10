package com.semihsahinoglu.crypto_app.signal.entity;

import com.semihsahinoglu.crypto_app.common.entity.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "signals")
public class SignalEntity extends Auditable {

    private String symbol;

    private String interval;

    private String type;

    private Double price;

    private Integer confidence;

    @Column(length = 1000)
    private String reason;

    private Double rsi;

    private Double ema50;

    private Double supportLevel;

    private Double resistanceLevel;

    public SignalEntity() {
    }

    public SignalEntity(String symbol, String interval, String type, Double price, Integer confidence, String reason, Double rsi, Double ema50, Double supportLevel, Double resistanceLevel) {
        this.symbol = symbol;
        this.interval = interval;
        this.type = type;
        this.price = price;
        this.confidence = confidence;
        this.reason = reason;
        this.rsi = rsi;
        this.ema50 = ema50;
        this.supportLevel = supportLevel;
        this.resistanceLevel = resistanceLevel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String symbol;
        private String interval;
        private String type;
        private Double price;
        private Integer confidence;
        private String reason;
        private Double rsi;
        private Double ema50;
        private Double supportLevel;
        private Double resistanceLevel;

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder interval(String interval) {
            this.interval = interval;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder confidence(Integer confidence) {
            this.confidence = confidence;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder rsi(Double rsi) {
            this.rsi = rsi;
            return this;
        }

        public Builder ema50(Double ema50) {
            this.ema50 = ema50;
            return this;
        }

        public Builder supportLevel(Double supportLevel) {
            this.supportLevel = supportLevel;
            return this;
        }

        public Builder resistanceLevel(Double resistanceLevel) {
            this.resistanceLevel = resistanceLevel;
            return this;
        }

        public SignalEntity build() {
            return new SignalEntity(symbol, interval, type, price, confidence, reason, rsi, ema50, supportLevel, resistanceLevel);
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public String getInterval() {
        return interval;
    }

    public String getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public String getReason() {
        return reason;
    }

    public Double getRsi() {
        return rsi;
    }

    public Double getEma50() {
        return ema50;
    }

    public Double getSupportLevel() {
        return supportLevel;
    }

    public Double getResistanceLevel() {
        return resistanceLevel;
    }
}