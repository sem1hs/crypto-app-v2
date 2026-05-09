package com.semihsahinoglu.crypto_app.candle.entity;

import com.semihsahinoglu.crypto_app.common.entity.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;


@Entity
@Table(name = "candles")
public class Candle extends Auditable {

    private String symbol;

    private String interval;

    @Column(name = "open_time", nullable = false)
    private Long openTime;

    @Column(name = "close_time")
    private Long closeTime;

    @Column(precision = 19, scale = 8)
    private BigDecimal open;

    @Column(precision = 19, scale = 8)
    private BigDecimal high;

    @Column(precision = 19, scale = 8)
    private BigDecimal low;

    @Column(precision = 19, scale = 8)
    private BigDecimal close;

    @Column(precision = 19, scale = 8)
    private BigDecimal volume;

    public Candle() {
    }

    public Candle(String symbol, String interval, Long openTime, Long closeTime, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, BigDecimal volume) {
        this.symbol = symbol;
        this.interval = interval;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String symbol;
        private String interval;
        private Long openTime;
        private Long closeTime;
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private BigDecimal volume;

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder interval(String interval) {
            this.interval = interval;
            return this;
        }

        public Builder openTime(Long openTime) {
            this.openTime = openTime;
            return this;
        }

        public Builder closeTime(Long closeTime) {
            this.closeTime = closeTime;
            return this;
        }

        public Builder open(BigDecimal open) {
            this.open = open;
            return this;
        }

        public Builder close(BigDecimal close) {
            this.close = close;
            return this;
        }

        public Builder high(BigDecimal high) {
            this.high = high;
            return this;
        }

        public Builder low(BigDecimal low) {
            this.low = low;
            return this;
        }

        public Builder volume(BigDecimal volume) {
            this.volume = volume;
            return this;
        }

        public Candle build() {
            return new Candle(symbol, interval, openTime, closeTime, open, high, low, close, volume);
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public String getInterval() {
        return interval;
    }

    public Long getOpenTime() {
        return openTime;
    }

    public Long getCloseTime() {
        return closeTime;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public BigDecimal getVolume() {
        return volume;
    }
}
