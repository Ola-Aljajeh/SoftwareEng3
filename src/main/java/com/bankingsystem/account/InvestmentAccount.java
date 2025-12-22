package com.bankingsystem.account;

/**
 * InvestmentAccount - Leaf node in Composite Pattern
 * 
 * Represents an investment account where funds can grow through investment returns.
 */
public class InvestmentAccount extends BaseAccount {
    
    private static final double MINIMUM_INVESTMENT = 1000.0;
    private static final String ACCOUNT_TYPE = "Investment Account";
    
    public InvestmentAccount(double initialInvestment) {
        super(initialInvestment);
        if (initialInvestment < MINIMUM_INVESTMENT) {
            throw new IllegalArgumentException("Minimum investment is $" + MINIMUM_INVESTMENT);
        }
    }
    
    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }
    
    @Override
    public String getDescription() {
        return "Investment Account [" + accountId + "] - Balance: $" + balance;
    }
}
