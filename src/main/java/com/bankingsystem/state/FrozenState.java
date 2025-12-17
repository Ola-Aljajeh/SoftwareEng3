package com.bankingsystem.state;

import com.bankingsystem.account.Account;
import com.bankingsystem.account.InsufficientFundsException;
/**
 * FrozenState - Concrete State
 * 
 * Account is frozen. Only deposits are allowed; withdrawals are forbidden.
 */
public class FrozenState implements AccountState {
    
    @Override
    public void withdraw(Account account, double amount) throws InsufficientFundsException {
        throw new IllegalStateException("Cannot withdraw from a frozen account");
    }
    
    @Override
    public void deposit(Account account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }
    
    @Override
    public String getStateName() {
        return "Frozen";
    }
}
