// ================================
// RewardServiceImpl.java (Implementation)
// ================================

package com.example.rewards.service;

import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.rewards.dto.RewardResponse;
import com.example.rewards.exception.CustomerNotFoundException;
import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;


/**
 * Implementation of the RewardService interface.
 */
@Service
public class RewardServiceImpl implements RewardService{
	
	private static final Logger logger = LoggerFactory.getLogger(RewardServiceImpl.class);

	
	@Autowired
	private TransactionRepository repository;
	
	 /**
     * Calculates reward points for a specific customer.
     * @param customerId the customer's ID
     * @return RewardResponse containing monthly and total points
     */
	@Override
	public RewardResponse calculateRewards(Long customerId) {
		 logger.info("Calculating rewards for customerId: {}", customerId);
		if (customerId == null) {
			logger.error("Customer ID is null");
            throw new IllegalArgumentException("Customer ID must not be null");
        }
		List<Transaction> transactions = repository.findByCustomerId(customerId);
        if (transactions.isEmpty()) {
        	logger.warn("No transactions found for customerId: {}", customerId);
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
        Map<String, Integer> monthlyPoints = new HashMap<>();
        int total = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Transaction t : transactions) {
            int points = calculatePoints(t.getAmount());
            String month = t.getDate().format(formatter);
            monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
            total += points;
        }
        return new RewardResponse(customerId, monthlyPoints, total);
	}
	
	private int calculatePoints(Double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int)(amount - 100) * 2 + 50;
        } else if (amount > 50) {
            points += (int)(amount - 50);
        }
        return points;
    }

}
