// ================================
// RewardServiceTests.java
// ================================
package com.example.rewards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.service.RewardService;

/**
 * Unit tests for RewardService.
 */
@SpringBootTest
public class RewardServiceTests {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RewardService service;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        repository.save(new Transaction(null, 1L, 120.0, LocalDate.of(2024, 1, 15)));
        repository.save(new Transaction(null, 1L, 80.0, LocalDate.of(2024, 2, 10)));
        repository.save(new Transaction(null, 2L, 150.0, LocalDate.of(2024, 1, 20)));
    }

    @Test
    public void testCustomer1Rewards() {
        assertEquals(90 + 30, service.calculateRewards(1L).getTotalPoints());
    }

    @Test
    public void testCustomer2Rewards() {
        assertEquals((150 - 100) * 2 + 50, service.calculateRewards(2L).getTotalPoints());
    }

    
    @Test
    public void testNoRewards() {
        var response = service.calculateRewards(3L);
        assertEquals(0, response.getTotalPoints());
        assertTrue(response.getMonthlyPoints().isEmpty());
    }

}
