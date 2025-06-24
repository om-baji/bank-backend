package com.bank.transaction.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @Autowired
    private KafkaTemplate<String,Object> consumer;

    @KafkaListener(topics = "my-topic", groupId = "my-group-id")
    public void consume(String message) {
        System.out.println("Received message: " + message);
    }
}
