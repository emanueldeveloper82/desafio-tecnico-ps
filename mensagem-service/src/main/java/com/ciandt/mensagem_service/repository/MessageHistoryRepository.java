package com.ciandt.mensagem_service.repository;

import com.ciandt.mensagem_service.entity.MessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageHistoryRepository extends JpaRepository<MessageHistory, Long> {
}
