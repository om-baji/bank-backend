package com.example.email_svc.config;

import com.example.email_svc.models.ConsumerObject;
import com.example.email_svc.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @Autowired
    private EmailService service;

    @KafkaListener(topics = "bank.email.service", groupId = "micro-1")
    public void listen(ConsumerObject message) {
        System.out.println("Object reached! " + message);
        service.sendMail(message);

    }
}