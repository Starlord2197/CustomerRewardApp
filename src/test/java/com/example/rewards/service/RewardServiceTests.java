// ================================
// RewardServiceTests.java
// ================================

package com.example.rewards.service;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.rewards.dto.RewardResponse;
import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;

import jakarta.validation.ConstraintViolationException;
 
/**
 * Unit tests for RewardService.
 * These tests cover valid reward calculations, fallback logic, and invalid data handling.
 */

@SpringBootTest
public class RewardServiceTests {
 
    @Autowired
    private TransactionRepository repository;
 
    @Autowired
    private RewardService rewardService;
 
    /**
     * Setup test data before each test.
     * Clears the DB and inserts known transactions for predictable reward calculation.
     */
    
    @BeforeEach
    public void setup() {
        repository.deleteAll();
        repository.save(new Transaction(null, 1L, 120.0, LocalDate.of(2024, 1, 15)));
        repository.save(new Transaction(null, 1L, 80.0, LocalDate.of(2024, 2, 10)));
        repository.save(new Transaction(null, 2L, 150.0, LocalDate.of(2024, 1, 20)));
    }
 
    /**
     * ✅ Valid case: reward points for customer 1 (2 transactions)
     */
    
    @Test
    @DisplayName("✅ Customer 1 Reward Points Calculation")
    public void testCustomer1Rewards() {
        RewardResponse response = rewardService.calculateRewards(1L);
        assertEquals(120 - 100, 20);
        assertEquals(90 + 30, response.getTotalPoints());
    }
 
    /**
     * ✅ Valid case: reward points for customer 2 (1 large transaction)
     */
    
    @Test
    @DisplayName("✅ Customer 2 Reward Points Calculation")
    public void testCustomer2Rewards() {
        RewardResponse response = rewardService.calculateRewards(2L);
        assertEquals(150 - 100, 50);
        assertEquals(150, response.getTotalPoints());
    }
 
    /**
     * 🔄 Fallback case: customer has no transactions → should return 0
     */
    
    @Test
    @DisplayName("❌ No Transactions → Fallback to 0 Points")
    public void testNoRewards_FallbackTriggered() {
        RewardResponse response = rewardService.calculateRewards(3L);
        assertEquals(0, response.getTotalPoints());
        assertEquals(Collections.emptyMap(), response.getMonthlyPoints());
    }
 
    /**
     * 🔄 Fallback case: null customerId → should return 0 with empty map
     */
    
    @Test
    @DisplayName("🔄 Null Customer ID returns fallback instead of exception")
    public void testNullCustomerId() {
        RewardResponse response = rewardService.calculateRewards(null);
        assertEquals(0, response.getTotalPoints());
        assertTrue(response.getMonthlyPoints().isEmpty());
    }
 
    /**
     * ❌ Validation case: saving negative amount should trigger ConstraintViolationException.
     */
    
    @Test
    @DisplayName("❌ Saving negative amount should throw ConstraintViolationException (manual)")
    public void testNegativeAmountTransaction() {
        Transaction invalidTxn = new Transaction(null, 4L, -50.0, LocalDate.now());
     
        try {
            repository.saveAndFlush(invalidTxn); // must trigger flush to validate
            fail("Expected ConstraintViolationException, but none was thrown.");
        } catch (ConstraintViolationException e) {
            // ✅ Expected exception — test passes
            System.out.println("Validation error caught as expected: " + e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception type thrown: " + e);
        }
    }
}
