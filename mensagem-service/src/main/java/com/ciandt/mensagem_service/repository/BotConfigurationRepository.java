package com.ciandt.mensagem_service.repository;

import com.ciandt.mensagem_service.entity.BotConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BotConfigurationRepository extends JpaRepository<BotConfiguration, Long> {
    Optional<BotConfiguration> findByPhoneNumberAndActiveTrue(String phoneNumber);
}
