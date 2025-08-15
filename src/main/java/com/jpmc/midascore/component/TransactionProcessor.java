package com.jpmc.midascore.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;

@Component
public class TransactionProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessor.class);
    private final DatabaseConduit databaseConduit;
    private final IncentiveService incentiveService;

    public TransactionProcessor(DatabaseConduit databaseConduit, IncentiveService incentiveService) {
        this.databaseConduit = databaseConduit;
        this.incentiveService = incentiveService;
    }

    @Transactional
    public boolean processTransaction(Transaction transaction) {
        logger.info("Processing transaction: senderId={}, recipientId={}, amount={}", 
                   transaction.getSenderId(), transaction.getRecipientId(), transaction.getAmount());

        // Validate and process the transaction
        UserRecord sender = databaseConduit.findUserById(transaction.getSenderId());
        UserRecord recipient = databaseConduit.findUserById(transaction.getRecipientId());

        // Validation: Check if sender exists
        if (sender == null) {
            logger.warn("Transaction rejected: Sender with ID {} not found", transaction.getSenderId());
            return false;
        }

        // Validation: Check if recipient exists
        if (recipient == null) {
            logger.warn("Transaction rejected: Recipient with ID {} not found", transaction.getRecipientId());
            return false;
        }

        // Validation: Check if sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Transaction rejected: Sender {} has insufficient balance. Current: {}, Required: {}", 
                       sender.getName(), sender.getBalance(), transaction.getAmount());
            return false;
        }

        // All validations passed - process the transaction
        try {
            // Get incentive from API
            Incentive incentive = incentiveService.getIncentive(transaction);
            float incentiveAmount = incentive.getAmount();
            
            // Update balances
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

            // Save updated users
            databaseConduit.updateUser(sender);
            databaseConduit.updateUser(recipient);

            // Save transaction record with incentive
            TransactionRecord transactionRecord = databaseConduit.saveTransactionWithIncentive(
                sender, recipient, transaction.getAmount(), incentiveAmount);

            logger.info("Transaction processed successfully: ID={}, {} -> {}, Amount={}, Incentive={}", 
                       transactionRecord.getId(), sender.getName(), recipient.getName(), 
                       transaction.getAmount(), incentiveAmount);
            
            return true;
        } catch (Exception e) {
            logger.error("Failed to process transaction: {}", e.getMessage(), e);
            return false;
        }
    }
}
