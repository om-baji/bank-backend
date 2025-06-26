package com.bank.notification_service.controllers;

import com.bank.notification_service.models.Transaction;
import com.bank.notification_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerController {

    @Autowired
    private NotificationService service;

    @KafkaListener(
            topics = "bank.transaction.service",
            groupId = "micro-1",
            containerFactory = "listenerContainerFactory"
    )
    public void listenerKafka(Transaction transaction) {
        log.warn("KAFKA RECEIVED TRANSACTION: {}", transaction.getId());
        service.transactionNotification(transaction);
    }
}
