package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final TransactionService transactionService;

    // Inject TransactionService through constructor
    public KafkaConsumerService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "transaction-topic", groupId = "consumer-group")
    public void consume(Transaction transaction) {
        System.out.println("Received transaction: " + transaction);

        // Process the transaction through your service
        transactionService.processTransaction(transaction);
    }
}