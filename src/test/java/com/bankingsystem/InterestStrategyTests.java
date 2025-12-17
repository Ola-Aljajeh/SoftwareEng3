package com.bankingsystem;

import com.bankingsystem.interest.*;
import com.bankingsystem.account.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Interest module (Strategy Pattern)
 */
@DisplayName("Interest Module Tests - Strategy Pattern")
public class InterestStrategyTests {
    
    private Account account;
    
    @BeforeEach
    public void setUp() {
        account = new SavingsAccount(1000.0);
    }
    
    @Test
    @DisplayName("Simple interest calculation")
    public void testSimpleInterest() {
        SimpleInterestStrategy strategy = new SimpleInterestStrategy(0.05);
        double interest = strategy.calculate(account);
        
        // 1000 * 0.05 = 50
        assertEquals(50.0, interest, 0.01);
    }
    
    @Test
    @DisplayName("Compound interest calculation (monthly)")
    public void testCompoundInterestMonthly() {
        CompoundInterestStrategy strategy = new CompoundInterestStrategy(0.05, 12);
        double interest = strategy.calculate(account);
        
        // With monthly compounding, compound interest should be higher than simple interest
        double simpleInterest = 1000 * 0.05;
        assertTrue(interest > simpleInterest);
    }
    
    @Test
    @DisplayName("Compound interest with annual compounding")
    public void testCompoundInterestAnnual() {
        CompoundInterestStrategy strategy = new CompoundInterestStrategy(0.05, 1);
        double interest = strategy.calculate(account);
        
        // Annual compounding is equivalent to simple interest for 1 year
        double simpleInterest = 1000 * 0.05;
        assertEquals(simpleInterest, interest, 0.01);
    }
    
    @Test
    @DisplayName("Promotional interest with qualifying balance")
    public void testPromotionalInterestQualified() {
        PromotionalInterestStrategy strategy = new PromotionalInterestStrategy(0.10, 0.02, 500.0);
        double interest = strategy.calculate(account);
        
        // Account balance of 1000 qualifies for promotional rate of 10%
        assertEquals(100.0, interest, 0.01);
    }
    
    @Test
    @DisplayName("Promotional interest with non-qualifying balance")
    public void testPromotionalInterestNonQualified() {
        Account lowBalanceAccount = new SavingsAccount(300.0);
        PromotionalInterestStrategy strategy = new PromotionalInterestStrategy(0.10, 0.02, 500.0);
        double interest = strategy.calculate(lowBalanceAccount);
        
        // Account balance of 300 doesn't qualify, uses standard rate of 2%
        assertEquals(6.0, interest, 0.01);
    }
    
    @Test
    @DisplayName("Interest strategy should be runtime switchable")
    public void testRuntimeStrategySwitch() throws InsufficientFundsException {
        SimpleInterestStrategy simpleStrategy = new SimpleInterestStrategy(0.05);
        account.setInterestStrategy(simpleStrategy);
        account.applyInterest();
        
        double balanceAfterSimple = account.getBalance();
        assertEquals(1050.0, balanceAfterSimple, 0.01);
        
        // Switch to compound strategy
        CompoundInterestStrategy compoundStrategy = new CompoundInterestStrategy(0.05, 12);
        account.setInterestStrategy(compoundStrategy);
        account.applyInterest();
        
        // Balance should increase more with compound interest
        double balanceAfterCompound = account.getBalance();
        assertTrue(balanceAfterCompound > balanceAfterSimple);
    }
    
    @Test
    @DisplayName("Invalid interest rate should throw exception")
    public void testInvalidInterestRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SimpleInterestStrategy(1.5); // Rate > 1
        });
    }
    
    @Test
    @DisplayName("Invalid compounding periods should throw exception")
    public void testInvalidCompoundingPeriods() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CompoundInterestStrategy(0.05, 0); // Non-positive periods
        });
    }
    
    @Test
    @DisplayName("Strategy description is informative")
    public void testStrategyDescription() {
        SimpleInterestStrategy strategy = new SimpleInterestStrategy(0.05);
        assertTrue(strategy.getDescription().contains("Simple Interest"));
        assertTrue(strategy.getDescription().contains("5.00"));
    }
    
    @Test
    @DisplayName("Zero interest rate should work")
    public void testZeroInterestRate() {
        SimpleInterestStrategy strategy = new SimpleInterestStrategy(0.0);
        double interest = strategy.calculate(account);
        assertEquals(0.0, interest);
    }
}
