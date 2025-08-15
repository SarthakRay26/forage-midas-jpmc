package com.jpmc.midascore.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction_record")
public class TransactionRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_record_seq", allocationSize = 1)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;
    
    @Column(name = "amount", nullable = false)
    private Float amount;
    
    @Column(name = "incentive", nullable = false)
    private Float incentive;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    public TransactionRecord() {
        this.timestamp = LocalDateTime.now();
        this.incentive = 0.0f;
    }
    
    public TransactionRecord(UserRecord sender, UserRecord recipient, Float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = 0.0f;
        this.timestamp = LocalDateTime.now();
    }
    
    public TransactionRecord(UserRecord sender, UserRecord recipient, Float amount, Float incentive) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserRecord getSender() {
        return sender;
    }
    
    public void setSender(UserRecord sender) {
        this.sender = sender;
    }
    
    public UserRecord getRecipient() {
        return recipient;
    }
    
    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }
    
    public Float getAmount() {
        return amount;
    }
    
    public void setAmount(Float amount) {
        this.amount = amount;
    }
    
    public Float getIncentive() {
        return incentive;
    }
    
    public void setIncentive(Float incentive) {
        this.incentive = incentive;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", senderId=" + (sender != null ? sender.getId() : null) +
                ", recipientId=" + (recipient != null ? recipient.getId() : null) +
                ", amount=" + amount +
                ", incentive=" + incentive +
                ", timestamp=" + timestamp +
                '}';
    }
}
