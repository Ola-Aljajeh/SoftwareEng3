package com.bankingsystem.interest;

import com.bankingsystem.account.Account;

/**
 * InterestStrategy - Strategy Pattern Interface
 * 
 * Defines the contract for different interest calculation strategies.
 * Allows interest calculation logic to be selected and changed at runtime
 * without modifying the Account classes.
 */
public interface InterestStrategy {
    
    /**
     * Calculate interest for the given account
     * 
     * @param account the account to calculate interest for
     * @return the calculated interest amount
     */
    double calculate(Account account);
    
    /**
     * Get a description of this strategy
     */
    String getDescription();
}
