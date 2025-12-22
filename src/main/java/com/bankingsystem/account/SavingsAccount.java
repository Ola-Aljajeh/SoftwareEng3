package com.bankingsystem.account;

/**
 * SavingsAccount - Leaf node in Composite Pattern
 * 
 * Represents a standard savings account with interest-bearing characteristics.
 */
public class SavingsAccount extends BaseAccount {
    
    private static final double MINIMUM_BALANCE = 100.0;
    private static final String ACCOUNT_TYPE = "Savings Account";
    
    public SavingsAccount(double initialBalance) {
        super(initialBalance);
        if (initialBalance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException("Minimum balance for savings account is $" + MINIMUM_BALANCE);
        }
    }
    
    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }
    
    @Override
    public String getDescription() {
        return "Savings Account [" + accountId + "] - Balance: $" + balance;
    }
}
