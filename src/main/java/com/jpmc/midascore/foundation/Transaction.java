package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jpmc.midascore.entity.UserRecord;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    private long senderId;
    private long recipientId;
    private float amount;
    private String name;

    public Transaction() {
    }

    public Transaction(String name,long senderId, long recipientId, float amount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.name = name;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getName() {return name;}

    public void setName(String name) { this.name = name;}

    @Override
    public String toString() {
        return "Transaction {senderId=" + senderId + ", name=" + name + ", recipientId=" + recipientId + ", amount=" + amount + "}";
    }



}
