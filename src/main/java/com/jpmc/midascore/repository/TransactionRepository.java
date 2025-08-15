package com.jpmc.midascore.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionRecord, Long> {
    
    // Find transactions by sender
    List<TransactionRecord> findBySender(UserRecord sender);
    
    // Find transactions by recipient
    List<TransactionRecord> findByRecipient(UserRecord recipient);
    
    // Find transactions within a time range
    List<TransactionRecord> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    // Find transactions by sender or recipient (useful for getting all transactions for a user)
    List<TransactionRecord> findBySenderOrRecipient(UserRecord sender, UserRecord recipient);
}
