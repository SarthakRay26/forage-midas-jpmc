package com.jpmc.midascore.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;

@Component
public class BalanceQuerier {
    
    private static final Logger logger = LoggerFactory.getLogger(BalanceQuerier.class);
    private final DatabaseConduit databaseConduit;

    public BalanceQuerier(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    public Balance query(Long id) {
        UserRecord user = databaseConduit.findUserById(id);
        if (user != null) {
            logger.info("User {} (ID: {}) has balance: {}", user.getName(), id, user.getBalance());
            return new Balance(user.getBalance());
        } else {
            logger.warn("User with ID {} not found", id);
            return new Balance(0.0f);
        }
    }

    public float getBalanceByName(String name) {
        UserRecord user = databaseConduit.findUserByName(name);
        if (user != null) {
            logger.info("User {} has balance: {}", name, user.getBalance());
            return user.getBalance();
        } else {
            logger.warn("User {} not found", name);
            return 0.0f;
        }
    }

    public float getBalanceById(Long id) {
        UserRecord user = databaseConduit.findUserById(id);
        if (user != null) {
            logger.info("User {} (ID: {}) has balance: {}", user.getName(), id, user.getBalance());
            return user.getBalance();
        } else {
            logger.warn("User with ID {} not found", id);
            return 0.0f;
        }
    }
}
