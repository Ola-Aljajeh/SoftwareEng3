package com.bankingsystem.state;

import com.bankingsystem.account.Account;
import com.bankingsystem.account.InsufficientFundsException;
/**
 * ActiveState - Concrete State
 * 
 * Account is fully operational.
 */
public class ActiveState implements AccountState {
    
    @Override
    public void withdraw(Account account, double amount) throws InsufficientFundsException {
        // Normal withdrawal logic (delegated to Account)
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > account.getBalance()) {
            throw new InsufficientFundsException(
                String.format("Insufficient funds. Balance: %.2f, Requested: %.2f",
                    account.getBalance(), amount)
            );
        }
    }
    
    @Override
    public void deposit(Account account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }
    
    @Override
    public String getStateName() {
        return "Active";
    }
}
