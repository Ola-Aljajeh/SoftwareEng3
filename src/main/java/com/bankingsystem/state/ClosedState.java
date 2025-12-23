package com.bankingsystem.state;

import com.bankingsystem.account.Account;
import com.bankingsystem.account.InsufficientFundsException;
/**
 * ClosedState - Concrete State
 * 
 * Account is closed. No operations are allowed.
 */
public class ClosedState implements AccountState {
    
    @Override
    public void withdraw(Account account, double amount) throws InsufficientFundsException {
        throw new IllegalStateException("Cannot withdraw from a closed account");
    }
    
    @Override
    public void deposit(Account account, double amount) {
        throw new IllegalStateException("Cannot deposit to a closed account");
    }
    
    @Override
    public String getStateName() {
        return "Closed";
    }
}
