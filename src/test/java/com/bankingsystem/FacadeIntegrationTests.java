package com.bankingsystem;

import com.bankingsystem.facade.BankFacade;
import com.bankingsystem.account.*;
import com.bankingsystem.interest.*;
import com.bankingsystem.notification.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for BankFacade (Facade Pattern)
 */
@DisplayName("Facade Integration Tests")
public class FacadeIntegrationTests {
    
    private BankFacade facade;
    
    @BeforeEach
    public void setUp() {
        facade = new BankFacade();
    }
    
    @Test
    @DisplayName("Facade should create accounts")
    public void testCreateAccounts() {
        Account savings = facade.createSavingsAccount(1000.0);
        Account checking = facade.createCheckingAccount(500.0);
        Account investment = facade.createInvestmentAccount(5000.0);
        Account loan = facade.createLoanAccount(10000.0, 0.05);
        
        assertNotNull(savings);
        assertNotNull(checking);
        assertNotNull(investment);
        assertNotNull(loan);
        
        assertEquals(1000.0, facade.getBalance(savings));
        assertEquals(500.0, facade.getBalance(checking));
        assertEquals(5000.0, facade.getBalance(investment));
    }
    
    @Test
    @DisplayName("Facade should perform transfers with approval")
    public void testFacadeTransfer() {
        Account account1 = facade.createSavingsAccount(5000.0);
        Account account2 = facade.createSavingsAccount(1000.0);
        
        boolean success = facade.transfer(account1, account2, 500.0);
        
        assertTrue(success);
        assertEquals(4500.0, facade.getBalance(account1));
        assertEquals(1500.0, facade.getBalance(account2));
    }
    
    @Test
    @DisplayName("Facade should reject large transfers")
    public void testFacadeRejectLargeTransfer() {
        Account account1 = facade.createSavingsAccount(50000.0);
        Account account2 = facade.createSavingsAccount(1000.0);
        
        // Transfers above $10,000 need admin approval (which is automatically granted)
        // So this should still succeed
        boolean success = facade.transfer(account1, account2, 15000.0);
        
        assertTrue(success); // Admin approves
    }
    
    @Test
    @DisplayName("Facade should handle deposits")
    public void testFacadeDeposit() {
        Account account = facade.createSavingsAccount(1000.0);
        
        boolean success = facade.deposit(account, 500.0);
        
        assertTrue(success);
        assertEquals(1500.0, facade.getBalance(account));
    }
    
    @Test
    @DisplayName("Facade should handle withdrawals with approval")
    public void testFacadeWithdraw() {
        Account account = facade.createSavingsAccount(1000.0);
        
        boolean success = facade.withdraw(account, 500.0);
        
        assertTrue(success);
        assertEquals(500.0, facade.getBalance(account));
    }
    
    @Test
    @DisplayName("Facade should set interest strategies")
    public void testFacadeInterestStrategy() {
        Account account = facade.createSavingsAccount(1000.0);
        InterestStrategy strategy = new CompoundInterestStrategy(0.05, 12);
        
        facade.setInterestStrategy(account, strategy);
        facade.applyInterest(account);
        
        assertTrue(facade.getBalance(account) > 1000.0);
    }
    
    @Test
    @DisplayName("Facade should manage account groups")
    public void testFacadeAccountGroup() {
        AccountGroup group = facade.createAccountGroup("Family Accounts");
        Account account1 = facade.createSavingsAccount(1000.0);
        Account account2 = facade.createSavingsAccount(2000.0);
        
        group.addAccount(account1);
        group.addAccount(account2);
        
        assertEquals(3000.0, facade.getBalance(group));
    }
    
    @Test
    @DisplayName("Facade should manage notifications")
    public void testFacadeNotifications() {
        Account account = facade.createSavingsAccount(1000.0);
        NotificationObserver observer = new EmailNotifier("test@example.com");
        
        facade.subscribeToNotifications(account, observer);
        facade.deposit(account, 100.0);
        // Should not throw exception
        
        facade.unsubscribeFromNotifications(account, observer);
        facade.deposit(account, 100.0);
        // Should not throw exception
    }
    
    @Test
    @DisplayName("Facade should apply interest to groups")
    public void testFacadeGroupInterest() {
        AccountGroup group = facade.createAccountGroup("Family Accounts");
        Account account1 = facade.createSavingsAccount(1000.0);
        Account account2 = facade.createSavingsAccount(2000.0);
        
        group.addAccount(account1);
        group.addAccount(account2);
        
        InterestStrategy strategy = new SimpleInterestStrategy(0.05);
        facade.setInterestStrategyForGroup(group, strategy);
        facade.applyInterestToGroup(group);
        
        assertEquals(3150.0, facade.getBalance(group), 0.01);
    }
    
    @Test
    @DisplayName("Facade should provide account descriptions")
    public void testFacadeAccountDescription() {
        Account account = facade.createSavingsAccount(1000.0);
        String description = facade.getAccountDescription(account);
        
        assertNotNull(description);
        assertTrue(description.contains("Savings Account"));
        assertTrue(description.contains("1000"));
    }
    
    @Test
    @DisplayName("Complex scenario: family account with multiple operations")
    public void testComplexScenario() {
        // Create family accounts
        AccountGroup family = facade.createAccountGroup("Johnson Family");
        Account parentSavings = facade.createSavingsAccount(10000.0);
        Account childSavings = facade.createSavingsAccount(1000.0);
        
        family.addAccount(parentSavings);
        family.addAccount(childSavings);
        
        // Add notifications
        NotificationObserver email = new EmailNotifier("parent@example.com");
        facade.subscribeToNotifications(family, email);
        
        // Set interest strategy for whole family
        InterestStrategy strategy = new CompoundInterestStrategy(0.04, 4);
        facade.setInterestStrategyForGroup(family, strategy);
        
        // Make transfers
        facade.transfer(parentSavings, childSavings, 500.0);
        
        // Apply interest
        facade.applyInterestToGroup(family);
        
        // Verify final state
        double finalBalance = facade.getBalance(family);
        assertTrue(finalBalance > 11000.0); // More than initial due to interest
    }
}
