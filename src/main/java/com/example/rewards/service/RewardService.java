// ================================
// RewardService.java (Interface)
// ================================

package com.example.rewards.service;

import com.example.rewards.dto.RewardResponse;

/**
 * Interface for reward point calculation service.
 */
public interface RewardService {
	
	/**
     * Calculates reward points for a specific customer.
     * 
     * @param customerId the customer's ID
     * @return RewardResponse containing monthly and total points
     */
	
	RewardResponse calculateRewards(Long customerId);
//	RewardResponse fallbackReward(Long customerId, Throwable t);

}
