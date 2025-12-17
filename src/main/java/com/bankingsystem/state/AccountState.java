package com.bankingsystem.state;

import com.bankingsystem.account.Account;
import com.bankingsystem.account.InsufficientFundsException;

/**
 * AccountState - State Pattern Interface
 * 
 * Defines the contract for different account states.
 * Behavior changes based on the account's current state.
 */
public interface AccountState {
    
    /**
     * Handle withdrawal in this state
     */
    void withdraw(Account account, double amount) throws InsufficientFundsException;
    
    /**
     * Handle deposit in this state
     */
    void deposit(Account account, double amount);
    
    /**
     * Get the state name
     */
    String getStateName();
}
