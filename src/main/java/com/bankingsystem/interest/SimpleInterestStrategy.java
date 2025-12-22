package com.bankingsystem.interest;

import com.bankingsystem.account.Account;
/**
 * SimpleInterestStrategy - Concrete Strategy
 * 
 * Calculates interest using the simple interest formula: P * R * T
 * Where P is principal, R is annual interest rate, T is time period (assumed 1 year)
 */
public class SimpleInterestStrategy implements InterestStrategy {
    
    private final double annualRate;
    
    /**
     * Create a simple interest strategy with the given annual rate
     * 
     * @param annualRate the annual interest rate (as decimal, e.g., 0.05 for 5%)
     */
    public SimpleInterestStrategy(double annualRate) {
        if (annualRate < 0 || annualRate > 1) {
            throw new IllegalArgumentException("Annual rate must be between 0 and 1");
        }
        this.annualRate = annualRate;
    }
    
    @Override
    public double calculate(Account account) {
        double balance = account.getBalance();
        double interest = balance * annualRate;
        return interest;
    }
    
    @Override
    public String getDescription() {
        return String.format("Simple Interest Strategy - Annual Rate: %.2f%%", annualRate * 100);
    }
}
