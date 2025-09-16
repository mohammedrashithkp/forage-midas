package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final String topic;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public KafkaProducer(@Value("${general.kafka-topic}") String topic, KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String transactionLine) {
        String[] transactionData = transactionLine.split(", ");
        if (transactionData.length == 3) {
            // Generate a transaction name since it's not provided
            String transactionName = "txn-" + System.currentTimeMillis();
            kafkaTemplate.send(topic, new Transaction(
                    transactionName,
                    Long.parseLong(transactionData[0].trim()), // senderId
                    Long.parseLong(transactionData[1].trim()), // recipientId
                    Float.parseFloat(transactionData[2].trim()) // amount
            ));
        } else {
            System.err.println("Invalid transaction format: " + transactionLine);
        }
    }
}