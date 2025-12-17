package com.bankingsystem;

import com.bankingsystem.account.*;
import com.bankingsystem.interest.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Account module (Composite Pattern)
 */
@DisplayName("Account Module Tests - Composite Pattern")
public class AccountTests {
    
    private SavingsAccount savingsAccount;
    private CheckingAccount checkingAccount;
    private InvestmentAccount investmentAccount;
    private LoanAccount loanAccount;
    
    @BeforeEach
    public void setUp() {
        savingsAccount = new SavingsAccount(1000.0);
        checkingAccount = new CheckingAccount(500.0);
        investmentAccount = new InvestmentAccount(5000.0);
        loanAccount = new LoanAccount(10000.0, 0.05);
    }
    
    @Test
    @DisplayName("Deposit should increase balance")
    public void testDeposit() {
        double initialBalance = savingsAccount.getBalance();
        savingsAccount.deposit(100.0);
        assertEquals(initialBalance + 100.0, savingsAccount.getBalance());
    }
    
    @Test
    @DisplayName("Withdraw should decrease balance")
    public void testWithdraw() throws InsufficientFundsException {
        double initialBalance = savingsAccount.getBalance();
        savingsAccount.withdraw(100.0);
        assertEquals(initialBalance - 100.0, savingsAccount.getBalance());
    }
    
    @Test
    @DisplayName("Withdraw with insufficient funds should throw exception")
    public void testWithdrawInsufficientFunds() {
        assertThrows(InsufficientFundsException.class, () -> {
            savingsAccount.withdraw(2000.0);
        });
    }
    
    @Test
    @DisplayName("Checking account should allow overdraft")
    public void testCheckingAccountOverdraft() throws InsufficientFundsException {
        // Checking account has overdraft limit of $500
        checkingAccount.withdraw(900.0); // Should succeed
        assertEquals(-400.0, checkingAccount.getBalance());
    }
    
    @Test
    @DisplayName("Checking account should reject excessive overdraft")
    public void testCheckingAccountExcessiveOverdraft() {
        assertThrows(InsufficientFundsException.class, () -> {
            checkingAccount.withdraw(1100.0); // Exceeds overdraft limit
        });
    }
    
    @Test
    @DisplayName("Loan account deposit should reduce debt")
    public void testLoanAccountPayment() {
        double initialDebt = loanAccount.getRemainingDebt();
        loanAccount.deposit(1000.0);
        assertEquals(initialDebt - 1000.0, loanAccount.getRemainingDebt());
    }
    
    @Test
    @DisplayName("Loan account should not allow withdrawals")
    public void testLoanAccountNoWithdrawal() {
        assertThrows(UnsupportedOperationException.class, () -> {
            loanAccount.withdraw(100.0);
        });
    }
    
    @Test
    @DisplayName("Account group should calculate combined balance")
    public void testAccountGroupBalance() {
        AccountGroup group = new AccountGroup("Family Accounts");
        group.addAccount(savingsAccount);
        group.addAccount(checkingAccount);
        
        double expectedBalance = savingsAccount.getBalance() + checkingAccount.getBalance();
        assertEquals(expectedBalance, group.getBalance());
    }
    
    @Test
    @DisplayName("Account group should apply interest to all accounts")
    public void testAccountGroupApplyInterest() {
        AccountGroup group = new AccountGroup("Family Accounts");
        group.addAccount(savingsAccount);
        group.addAccount(checkingAccount);
        
        SimpleInterestStrategy strategy = new SimpleInterestStrategy(0.05);
        group.setInterestStrategy(strategy);
        
        double balanceBeforeInterest = group.getBalance();
        group.applyInterest();
        
        // With 5% interest on total balance
        double expectedBalance = balanceBeforeInterest * 1.05;
        assertEquals(expectedBalance, group.getBalance(), 0.01);
    }
    
    @Test
    @DisplayName("Nested account groups should work")
    public void testNestedAccountGroups() {
        AccountGroup familyGroup = new AccountGroup("Family");
        familyGroup.addAccount(savingsAccount);
        familyGroup.addAccount(checkingAccount);
        
        AccountGroup parentGroup = new AccountGroup("Parent Group");
        parentGroup.addAccount(investmentAccount);
        parentGroup.addAccount(familyGroup);
        
        double expectedBalance = savingsAccount.getBalance() + checkingAccount.getBalance() + investmentAccount.getBalance();
        assertEquals(expectedBalance, parentGroup.getBalance());
    }
    
    @Test
    @DisplayName("Invalid deposit should throw exception")
    public void testInvalidDeposit() {
        assertThrows(IllegalArgumentException.class, () -> {
            savingsAccount.deposit(-100.0);
        });
    }
    
    @Test
    @DisplayName("Invalid withdrawal should throw exception")
    public void testInvalidWithdrawal() {
        assertThrows(IllegalArgumentException.class, () -> {
            savingsAccount.withdraw(-100.0);
        });
    }
    
    @Test
    @DisplayName("Savings account minimum balance validation")
    public void testSavingsAccountMinimumBalance() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SavingsAccount(50.0); // Below minimum of $100
        });
    }
    
    @Test
    @DisplayName("Investment account minimum balance validation")
    public void testInvestmentAccountMinimumBalance() {
        assertThrows(IllegalArgumentException.class, () -> {
            new InvestmentAccount(500.0); // Below minimum of $1000
        });
    }
}
