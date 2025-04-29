// ================================
// RewardController.java
// ================================
package com.example.rewards.controller;

import com.example.rewards.dto.RewardResponse;
import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling reward-related endpoints.
 * 
 * Base URL: http://localhost:8080/api
 */
@RestController
@RequestMapping("/api")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @Autowired
    private TransactionRepository transactionRepository;
    
    /**
     * Endpoint to add a new transaction.
     * URL: POST http://localhost:8080/api/transactions 
     * @param transaction Transaction data
     * @return Saved Transaction
     */

    @PostMapping("/transactions")
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    
    /**
     * Endpoint to fetch rewards for a specific customer by ID.
     * URL: GET http://localhost:8080/api/rewards/{customerId} 
     * @param customerId ID of the customer
     * @return RewardResponse containing reward points
     */

    @GetMapping("/rewards/{customerId}")
    public RewardResponse getRewards(@PathVariable Long customerId) {
        return rewardService.calculateRewards(customerId);
    }
    
    /**
     * Endpoint to fetch all transactions.
     * URL: GET http://localhost:8080/api/transactions 
     * @return List of all transactions
     */

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}

