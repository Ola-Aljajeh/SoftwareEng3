package com.bankingsystem.interest;

import com.bankingsystem.account.Account;
/**
 * PromotionalInterestStrategy - Concrete Strategy
 * 
 * Offers higher interest rates for qualified accounts (e.g., newly opened accounts)
 * as a promotional incentive.
 */
public class PromotionalInterestStrategy implements InterestStrategy {
    
    private final double promotionalRate;
    private final double standardRate;
    private final double qualifyingBalance;
    
    /**
     * Create a promotional interest strategy
     * 
     * @param promotionalRate higher rate for qualified accounts
     * @param standardRate standard rate for non-qualified
     * @param qualifyingBalance minimum balance to qualify for promotional rate
     */
    public PromotionalInterestStrategy(double promotionalRate, double standardRate, double qualifyingBalance) {
        this.promotionalRate = promotionalRate;
        this.standardRate = standardRate;
        this.qualifyingBalance = qualifyingBalance;
    }
    
    @Override
    public double calculate(Account account) {
        double balance = account.getBalance();
        double rateToApply = balance >= qualifyingBalance ? promotionalRate : standardRate;
        double interest = balance * rateToApply;
        return interest;
    }
    
    @Override
    public String getDescription() {
        return String.format(
            "Promotional Interest - Promo Rate: %.2f%% (min balance: $%.2f), Standard: %.2f%%",
            promotionalRate * 100, qualifyingBalance, standardRate * 100
        );
    }
}
