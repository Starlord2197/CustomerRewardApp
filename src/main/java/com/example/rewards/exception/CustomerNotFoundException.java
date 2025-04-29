// ================================
// CustomerNotFoundException.java
// ================================
package com.example.rewards.exception;

/**
 * Exception thrown when customer ID is not found in database.
 */
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
