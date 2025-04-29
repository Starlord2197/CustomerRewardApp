package com.example.rewards.controller;
 
import com.example.rewards.dto.RewardResponse;
import com.example.rewards.exception.CustomerNotFoundException;
import com.example.rewards.exception.GlobalExceptionHandler;
import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
 
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
 
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 
@ExtendWith(MockitoExtension.class)
public class RewardControllerTest {
 
    private MockMvc mockMvc;
 
    @Mock
    private RewardService rewardService;
 
    @Mock
    private TransactionRepository transactionRepository;
 
    @InjectMocks
    private RewardController rewardController;
 
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(rewardController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Ensure 404/500 are handled
                .build();
    }
 
    /**
     * ✅ Valid customer reward calculation
     */
    @Test
    @DisplayName("✅ Should return rewards for valid customer")
    public void testGetRewardPoints_Success() throws Exception {
        Map<String, Integer> monthlyPoints = new HashMap<>();
        monthlyPoints.put("2024-01", 90);
        monthlyPoints.put("2024-02", 30);
        RewardResponse response = new RewardResponse(1L, monthlyPoints, 120);
 
        when(rewardService.calculateRewards(anyLong())).thenReturn(response);
 
        mockMvc.perform(get("/api/rewards/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.monthlyPoints.2024-01").value(90))
                .andExpect(jsonPath("$.monthlyPoints.2024-02").value(30))
                .andExpect(jsonPath("$.totalPoints").value(120));
    }
 
    /**
     * ❌ Customer not found → 404
     */
    @Test
    @DisplayName("❌ Customer not found should return 404")
    public void testCustomerNotFound() throws Exception {
        lenient().when(rewardService.calculateRewards(999L))
                .thenThrow(new CustomerNotFoundException("Customer not found"));
 
        mockMvc.perform(get("/api/rewards/{customerId}", 999L))
                .andExpect(status().isNotFound());
    }
 
    /**
     * 🔄 No reward transactions → should return 0
     */
    @Test
    @DisplayName("✅ Valid customer with no rewards should return 0")
    public void testCustomerWithNoRewards() throws Exception {
        RewardResponse response = new RewardResponse(5L, Collections.emptyMap(), 0);
        when(rewardService.calculateRewards(5L)).thenReturn(response);
 
        mockMvc.perform(get("/api/rewards/{customerId}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(5L))
                .andExpect(jsonPath("$.totalPoints").value(0))
                .andExpect(jsonPath("$.monthlyPoints").isEmpty());
    }
 
    /**
     * ❌ Simulate server-side failure → 500
     */
    @Test
    @DisplayName("❌ Should return 500 Internal Server Error when unexpected exception occurs")
    public void testServiceThrowsGenericException() throws Exception {
        when(rewardService.calculateRewards(4L)).thenThrow(new RuntimeException("Database failure"));
 
        mockMvc.perform(get("/api/rewards/{customerId}", 4L))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("An unexpected error occurred")));
    }
 
    /**
     * ✅ Add transaction via POST
     */
    @Test
    @DisplayName("✅ Should save transaction successfully")
    public void testAddTransaction() throws Exception {
        String newTransactionJson = """
            {
                "customerId": 10,
                "amount": 120.5,
                "date": "2024-03-01"
            }
        """;
 
        Transaction savedTransaction = new Transaction(1L, 10L, 120.5, LocalDate.of(2024, 3, 1));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
 
        mockMvc.perform(post("/api/transactions")
                .contentType("application/json")
                .content(newTransactionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(10))
                .andExpect(jsonPath("$.amount").value(120.5))
                .andExpect(jsonPath("$.date").value("2024-03-01"));
    }
}