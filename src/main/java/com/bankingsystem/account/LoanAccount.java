package com.bankingsystem.account;

/**
 * LoanAccount - Leaf node in Composite Pattern
 * 
 * Represents a loan account where balance is typically negative (amount owed).
 */
public class LoanAccount extends BaseAccount {
    
    private static final String ACCOUNT_TYPE = "Loan Account";
    private double loanAmount;
    private double interestRate;
    
    public LoanAccount(double loanAmount, double interestRate) {
        super(-loanAmount); // Negative balance represents debt
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
    }
    
    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        balance += amount; // Reduces debt (makes balance less negative)
        notifyObservers("Loan payment of $" + amount + " received. Remaining debt: $" + Math.abs(balance));
    }
    
    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        throw new UnsupportedOperationException("Cannot withdraw from loan account");
    }
    
    public double getRemainingDebt() {
        return Math.abs(balance);
    }
    
    public double getInterestRate() {
        return interestRate;
    }
    
    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }
    
    @Override
    public String getDescription() {
        return "Loan Account [" + accountId + "] - Remaining Debt: $" + getRemainingDebt();
    }
}
