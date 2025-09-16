package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public void processTransaction(Transaction transaction) {
        System.out.println("Processing transaction: " + transaction);

        try {
            Incentive incentive = restTemplate.postForObject(
                    "http://localhost:8080/incentive" ,
                    transaction,
                    Incentive.class
            );

            float incentiveAmount = incentive != null ? incentive.getAmount() :0;

            // Validate and get users
            Optional<UserRecord> senderOpt = userRepository.findById(transaction.getSenderId());
            Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());

            if (senderOpt.isEmpty()) {
                System.out.println("Invalid sender ID: " + transaction.getSenderId());
                // Record failed transaction
                TransactionRecord failedRecord = new TransactionRecord();
                failedRecord.setName(transaction.getName());
                failedRecord.setAmount((double) transaction.getAmount());
                failedRecord.setStatus("FAILED");
                transactionRepository.save(failedRecord);
                return;
            }

            if (recipientOpt.isEmpty()) {
                System.out.println("Invalid recipient ID: " + transaction.getRecipientId());
                // Record failed transaction
                TransactionRecord failedRecord = new TransactionRecord();
                failedRecord.setName(transaction.getName());
                failedRecord.setAmount((double) transaction.getAmount());
                failedRecord.setStatus("FAILED");
                failedRecord.setIncentive((double) incentiveAmount);
                transactionRepository.save(failedRecord);
                return;
            }

            UserRecord sender = senderOpt.get();
            UserRecord recipient = recipientOpt.get();

            // Validate sender balance
            if (sender.getBalance() < transaction.getAmount()) {
                System.out.println("Insufficient balance. Sender: " + sender.getName() +
                        ", Balance: " + sender.getBalance() +
                        ", Required: " + transaction.getAmount());
                // Record failed transaction
                TransactionRecord failedRecord = new TransactionRecord(
                        transaction.getName(),
                        transaction.getAmount(),
                        sender,
                        recipient,
                        "FAILED",
                        incentiveAmount
                );
                transactionRepository.save(failedRecord);
                return;
            }

            // Process the transaction
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

            // Save updated users
            userRepository.save(sender);
            userRepository.save(recipient);

            // Record successful transaction
            TransactionRecord record = new TransactionRecord(
                    transaction.getName(),
                    transaction.getAmount(),
                    sender,
                    recipient,
                    "COMPLETED" ,
                    incentiveAmount
            );
            transactionRepository.save(record);

            System.out.println("Transaction completed successfully: " + record);
            System.out.println("Sender (" + sender.getName() + ") new balance: " + sender.getBalance());
            System.out.println("Recipient (" + recipient.getName() + ") new balance: " + recipient.getBalance());

        } catch (Exception e) {
            System.err.println("Error processing transaction: " + e.getMessage());
            e.printStackTrace();
            // Transaction will be rolled back automatically due to @Transactional
        }
    }
}