package com.bankingsystem.state;

import com.bankingsystem.account.Account;
import com.bankingsystem.account.InsufficientFundsException;
/**
 * SuspendedState - Concrete State
 * 
 * Account is suspended. Neither deposits nor withdrawals are allowed.
 */
public class SuspendedState implements AccountState {
    
    @Override
    public void withdraw(Account account, double amount) throws InsufficientFundsException {
        throw new IllegalStateException("Cannot withdraw from a suspended account");
    }
    
    @Override
    public void deposit(Account account, double amount) {
        throw new IllegalStateException("Cannot deposit to a suspended account");
    }
    
    @Override
    public String getStateName() {
        return "Suspended";
    }
}
