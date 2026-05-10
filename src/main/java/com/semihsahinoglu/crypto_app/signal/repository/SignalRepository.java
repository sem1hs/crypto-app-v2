package com.semihsahinoglu.crypto_app.signal.repository;

import com.semihsahinoglu.crypto_app.signal.entity.SignalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignalRepository extends JpaRepository<SignalEntity, Long> {
}
