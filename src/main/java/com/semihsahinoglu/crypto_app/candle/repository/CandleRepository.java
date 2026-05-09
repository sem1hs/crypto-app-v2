package com.semihsahinoglu.crypto_app.candle.repository;

import com.semihsahinoglu.crypto_app.candle.entity.Candle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandleRepository extends CrudRepository<Candle, UUID> {

    boolean existsBySymbolAndIntervalAndOpenTime(String symbol, String interval, Long openTime);

    List<Candle> findTop100BySymbolAndIntervalOrderByOpenTimeDesc(String symbol, String interval);
}
