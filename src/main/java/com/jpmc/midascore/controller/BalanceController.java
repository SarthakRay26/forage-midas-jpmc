package com.jpmc.midascore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpmc.midascore.component.BalanceQuerier;
import com.jpmc.midascore.foundation.Balance;

@RestController
public class BalanceController {
    
    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
    
    private final BalanceQuerier balanceQuerier;
    
    public BalanceController(BalanceQuerier balanceQuerier) {
        this.balanceQuerier = balanceQuerier;
    }
    
    @GetMapping("/balance")
    public ResponseEntity<Balance> getBalance(@RequestParam("userId") Long userId) {
        try {
            logger.info("Received balance query request for user ID: {}", userId);
            Balance balance = balanceQuerier.query(userId);
            logger.info("Returning balance for user ID {}: {}", userId, balance.toString());
            return ResponseEntity.ok(balance);
        } catch (Exception e) {
            logger.error("Error querying balance for user ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new Balance(0.0f));
        }
    }
}
