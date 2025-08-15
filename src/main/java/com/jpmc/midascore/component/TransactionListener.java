package com.jpmc.midascore.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.foundation.Transaction;

@Component
public class TransactionListener {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
    private final TransactionProcessor transactionProcessor;

    public TransactionListener(TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void handleTransaction(Transaction transaction) {
        logger.info("Received transaction: senderId={}, recipientId={}, amount={}", 
                   transaction.getSenderId(), transaction.getRecipientId(), transaction.getAmount());
        
        // Process transaction with validation and balance updates
        boolean success = transactionProcessor.processTransaction(transaction);
        
        if (success) {
            logger.info("Transaction processed successfully");
        } else {
            logger.warn("Transaction was rejected or failed to process");
        }
    }
}
