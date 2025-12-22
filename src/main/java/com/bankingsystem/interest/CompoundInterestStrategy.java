package com.bankingsystem.interest;

import com.bankingsystem.account.Account;
/**
 * CompoundInterestStrategy - Concrete Strategy
 * 
 * Calculates interest using compound interest formula: P * (1 + r/n)^(n*t) - P
 * Where P is principal, r is annual rate, n is compounding periods per year, t is time in years
 */
public class CompoundInterestStrategy implements InterestStrategy {
    
    private final double annualRate;
    private final int compoundingPeriodsPerYear;
    
    /**
     * Create a compound interest strategy
     * 
     * @param annualRate the annual interest rate (as decimal, e.g., 0.05 for 5%)
     * @param compoundingPeriodsPerYear how many times per year interest compounds (e.g., 12 for monthly)
     */
    public CompoundInterestStrategy(double annualRate, int compoundingPeriodsPerYear) {
        if (annualRate < 0 || annualRate > 1) {
            throw new IllegalArgumentException("Annual rate must be between 0 and 1");
        }
        if (compoundingPeriodsPerYear <= 0) {
            throw new IllegalArgumentException("Compounding periods must be positive");
        }
        this.annualRate = annualRate;
        this.compoundingPeriodsPerYear = compoundingPeriodsPerYear;
    }
    
    @Override
    public double calculate(Account account) {
        double balance = account.getBalance();
        
        // For 1 year period: P * (1 + r/n)^n - P
        double periodRate = annualRate / compoundingPeriodsPerYear;
        double compoundedValue = balance * Math.pow(1 + periodRate, compoundingPeriodsPerYear);
        double interest = compoundedValue - balance;
        return interest;
    }
    
    @Override
    public String getDescription() {
        return String.format("Compound Interest Strategy - Annual Rate: %.2f%%, Compounds: %d times/year",
            annualRate * 100, compoundingPeriodsPerYear);
    }
}
