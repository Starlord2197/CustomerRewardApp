// ================================
// RewardsApplication.java
// ================================

package com.example.rewards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Rewards Spring Boot application.
 * This application calculates customer reward points based on transactions.
 */
@SpringBootApplication
public class CustomerRewardsApiApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerRewardsApiApplication.class);

	public static void main(String[] args) {
		logger.debug("Starting Customer Rewards API application...");
		SpringApplication.run(CustomerRewardsApiApplication.class, args);
		logger.debug("Application started successfully on http://localhost:8080");
	}

}
