// ================================
// RewardResponse.java
// ================================
package com.example.rewards.dto;

import java.util.Map;

/**
 * DTO representing reward points summary per customer.
 */
public class RewardResponse {
	
	private Long customerId;
	private Map<String, Integer> monthlyPoints;
	private Integer totalPoints;
	
	public RewardResponse(Long customerId, Map<String, Integer> monthlyPoints, Integer totalPoints) {
		this.customerId = customerId;
		this.monthlyPoints = monthlyPoints;
		this.totalPoints = totalPoints;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public Map<String, Integer> getMonthlyPoints() {
		return monthlyPoints;
	}

	public Integer getTotalPoints() {
		return totalPoints;
	}

}
