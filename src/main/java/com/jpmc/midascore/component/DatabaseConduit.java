package com.jpmc.midascore.component;

import org.springframework.stereotype.Component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public TransactionRecord saveTransaction(Transaction transaction) {
        UserRecord sender = findUserById(transaction.getSenderId());
        UserRecord recipient = findUserById(transaction.getRecipientId());
        
        if (sender == null || recipient == null) {
            throw new IllegalArgumentException("Sender or recipient not found");
        }
        
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount());
        return transactionRepository.save(transactionRecord);
    }

    public TransactionRecord saveTransactionWithUsers(UserRecord sender, UserRecord recipient, Float amount) {
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, amount);
        return transactionRepository.save(transactionRecord);
    }

    public TransactionRecord saveTransactionWithIncentive(UserRecord sender, UserRecord recipient, Float amount, Float incentive) {
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, amount, incentive);
        return transactionRepository.save(transactionRecord);
    }

    public UserRecord findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserRecord findUserByName(String name) {
        return userRepository.findByName(name);
    }

    public void updateUser(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

}
