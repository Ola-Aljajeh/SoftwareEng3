package com.bankingsystem.account;

/**
 * CheckingAccount - Leaf node in Composite Pattern
 * 
 * Represents a checking account with transaction capabilities.
 */
public class CheckingAccount extends BaseAccount {
    
    private static final double OVERDRAFT_LIMIT = 500.0;
    private static final String ACCOUNT_TYPE = "Checking Account";
    
    public CheckingAccount(double initialBalance) {
        super(initialBalance);
    }
    
    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        // Checking accounts allow overdraft up to limit
        if (amount > balance + OVERDRAFT_LIMIT) {
            throw new InsufficientFundsException(
                String.format("Overdraft limit exceeded. Available: %.2f", balance + OVERDRAFT_LIMIT)
            );
        }
        balance -= amount;
        notifyObservers("Withdrawal of $" + amount + " completed. New balance: $" + balance);
    }
    
    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }
    
    @Override
    public String getDescription() {
        return "Checking Account [" + accountId + "] - Balance: $" + balance;
    }
}
