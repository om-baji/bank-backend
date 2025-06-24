package com.bank.transaction.components;

import com.bank.transaction.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Producer {

    @Autowired
    private KafkaTemplate<String,Object> producer;

    public void messageProducer(String topic,Map<String,Object> map) {
        producer.send(topic,map);
    }

    public void messageProducer(String topic, Transaction transaction) {
        producer.send(topic,transaction);
    }
}