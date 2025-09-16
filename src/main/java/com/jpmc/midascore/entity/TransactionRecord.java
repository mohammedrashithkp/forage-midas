package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String status; // COMPLETED or FAILED

    @Column(nullable = false)
    private Double incentive;

    // Default constructor
    public TransactionRecord() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for successful transactions
    public TransactionRecord(String name, float amount, UserRecord sender, UserRecord recipient, String status , float incentive) {
        this();
        this.name = name;
        this.amount = (double) amount;
        this.sender = sender;
        this.recipient = recipient;
        this.status = status;
        this.incentive = (double)incentive;
    }

    // Getters and Setters (keep your existing ones)
    public Long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public UserRecord getSender() { return sender; }
    public void setSender(UserRecord sender) { this.sender = sender; }
    public UserRecord getRecipient() { return recipient; }
    public void setRecipient(UserRecord recipient) { this.recipient = recipient; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getIncentive() { return incentive; }
    public void setIncentive(Double incentive) { this.incentive = incentive; } // Add this setter
    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", senderId=" + (sender != null ? sender.getId() : null) +
                ", recipientId=" + (recipient != null ? recipient.getId() : null) +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}