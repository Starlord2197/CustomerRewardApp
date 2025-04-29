// ================================
// RewardControllerTests.java
// ================================
package com.example.rewards;
 
import com.example.rewards.controller.RewardController;
import com.example.rewards.dto.RewardResponse;
import com.example.rewards.exception.CustomerNotFoundException;
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
import static org.mockito.BDDMockito.given;
 
import java.util.HashMap;
import java.util.Map;
 
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
 
/**
* Unit tests for RewardController.
*/
@ExtendWith(MockitoExtension.class)
public class RewardControllerTest {
 
    private MockMvc mockMvc;
 
    @Mock
    private RewardService rewardService; // Mocking RewardService dependency
 
    @InjectMocks
    private RewardController rewardController; // Injecting mocked service into controller
 
    @BeforeEach
    public void setup() {
        // Initialize MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(rewardController).build();
    }
 
    /**
     * Positive Test Case:
     * Test that reward points are fetched successfully for a valid customer.
     * - Mocks RewardService to return a predefined RewardResponse.
     * - Verifies that the response is HTTP 200 OK.
     * - Verifies the correct JSON structure and values.
     */
    @Test
    @DisplayName("✅ Should return reward points successfully for valid customerId")
    public void testGetRewardPoints_Success() throws Exception {
        // Prepare mock reward points data
        Map<String, Integer> monthlyPoints = new HashMap<>();
        monthlyPoints.put("January", 100);
        monthlyPoints.put("February", 200);
 
        // Mocked response from service
        RewardResponse rewardResponse = new RewardResponse(1L, monthlyPoints, 300);
 
        // Mock the service behavior
        when(rewardService.calculateRewards(anyLong())).thenReturn(rewardResponse);
 
        // Perform GET request and validate response
        mockMvc.perform(get("/rewards/{customerId}", 1L))
               .andExpect(status().isOk()) // HTTP 200
               .andExpect(jsonPath("$.customerId").value(1L)) // Validate customerId
               .andExpect(jsonPath("$.monthlyPoints.January").value(100)) // Validate January points
               .andExpect(jsonPath("$.monthlyPoints.February").value(200)) // Validate February points
               .andExpect(jsonPath("$.totalPoints").value(300)); // Validate total points
    }
 
    /**
     * Negative Test Case:
     * Test that an error is returned when customer is not found.
     * - Mocks RewardService to throw RuntimeException.
     * - Verifies that the controller returns an error response (e.g., 404 Not Found).
     */
    
    @Test
    @DisplayName("❌ Should return 404 Not Found when customerId does not exist")
    public void testGetRewardPoints_CustomerNotFound() throws Exception {
        // Mock the service to throw a CustomerNotFoundException

    	lenient().when(rewardService.calculateRewards(999L))
        .thenThrow(new CustomerNotFoundException("Customer not found"));
 

        // Perform GET request and validate that an error occurs
        mockMvc.perform(get("/rewards/{customerId}", 999L))
            .andExpect(status().isNotFound()); // Expected HTTP 404
    }
    
}