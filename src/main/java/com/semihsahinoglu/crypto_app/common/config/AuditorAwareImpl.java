package com.semihsahinoglu.crypto_app.common.config;

import org.springframework.data.domain.AuditorAware;


import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return Optional.empty();
//        }
        return Optional.of("semihsahinoglu");
    }
}
