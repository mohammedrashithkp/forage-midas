package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate; // Use Object to handle both

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // For your UserController
    public void sendMessage(String message) {
        kafkaTemplate.send("transaction-topic", message);
        System.out.println("Message sent: " + message);
    }

    // For Transaction objects (evaluation tests)
    public void sendTransaction(Transaction transaction) {
        kafkaTemplate.send("transaction-topic", transaction);
        System.out.println("Transaction sent: " + transaction);
    }
}