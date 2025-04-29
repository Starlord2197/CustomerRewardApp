// ================================
// Transaction.java
// ================================

package com.example.rewards.model;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Entity representing a purchase transaction made by a customer.
 */
@Entity
@Table(name = "transactions")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private Long customerId;
	@Positive
	private Double amount;
	@PastOrPresent
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	public Transaction() {
	}

	public Transaction(Long id, Long customerId, Double amount, LocalDate date) {
		this.id = id;
		this.customerId = customerId;
		this.amount = amount;
		this.date = date;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	

}
