package com.jpmc.midascore.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;

@Component
public class IncentiveService {
    
    private static final Logger logger = LoggerFactory.getLogger(IncentiveService.class);
    
    @Value("${incentive.api.url:http://localhost:8080/incentive}")
    private String incentiveApiUrl;
    
    private final RestTemplate restTemplate;

    public IncentiveService() {
        this.restTemplate = new RestTemplate();
    }

    public Incentive getIncentive(Transaction transaction) {
        try {
            logger.info("Calling incentive API for transaction: senderId={}, recipientId={}, amount={}", 
                       transaction.getSenderId(), transaction.getRecipientId(), transaction.getAmount());
            
            Incentive incentive = restTemplate.postForObject(incentiveApiUrl, transaction, Incentive.class);
            
            if (incentive != null) {
                logger.info("Received incentive amount: {}", incentive.getAmount());
                return incentive;
            } else {
                logger.warn("Incentive API returned null response");
                return new Incentive(0.0f);
            }
        } catch (Exception e) {
            logger.error("Failed to call incentive API: {}", e.getMessage(), e);
            // Return zero incentive on error to avoid blocking transactions
            return new Incentive(0.0f);
        }
    }
}
