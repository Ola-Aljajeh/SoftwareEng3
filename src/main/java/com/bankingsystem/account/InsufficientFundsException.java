package com.bankingsystem.account;

/**
 * Exception thrown when attempting to withdraw more funds than available
 */
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
