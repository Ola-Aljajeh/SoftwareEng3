package com.bankingsystem.account;

import com.bankingsystem.interest.InterestStrategy;
import com.bankingsystem.notification.NotificationObserver;

/**
 * Account Interface - Component in Composite Pattern
 * 
 * Defines the contract for all account types (leaf and composite).
 * Treats single accounts and groups of accounts uniformly.
 */
public interface Account {
    
    /**
     * Get the account ID
     */
    String getAccountId();
    
    /**
     * Get the current balance
     */
    double getBalance();
    
    /**
     * Deposit funds into the account
     */
    void deposit(double amount);
    
    /**
     * Withdraw funds from the account
     */
    void withdraw(double amount) throws InsufficientFundsException;
    
    /**
     * Apply interest to the account using the current strategy
     */
    void applyInterest();
    
    /**
     * Set the interest calculation strategy
     */
    void setInterestStrategy(InterestStrategy strategy);
    
    /**
     * Get the account type
     */
    String getAccountType();
    
    /**
     * Subscribe to notifications
     */
    void subscribe(NotificationObserver observer);
    
    /**
     * Unsubscribe from notifications
     */
    void unsubscribe(NotificationObserver observer);
    
    /**
     * Get account description (useful for composite accounts)
     */
    String getDescription();
}
