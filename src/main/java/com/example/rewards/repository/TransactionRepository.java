// ================================
// TransactionRepository.java
// ================================

package com.example.rewards.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.rewards.model.Transaction;

/**
 * Repository interface for accessing transaction data.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	List<Transaction> findByCustomerId(Long customerId);

}
