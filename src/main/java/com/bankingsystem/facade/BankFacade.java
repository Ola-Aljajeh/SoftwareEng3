package com.bankingsystem.facade;

import com.bankingsystem.account.*;
import com.bankingsystem.interest.InterestStrategy;
import com.bankingsystem.transaction.*;
import com.bankingsystem.notification.NotificationObserver;
/**
 * BankFacade - Facade Pattern
 * 
 * Provides a simplified, unified interface to the complex banking system.
 * Hides the complexity of dealing with multiple patterns and modules.
 * 
 * Clients interact with this facade instead of directly managing accounts,
 * transactions, approvals, etc.
 */
public class BankFacade {
    
    private final ApprovalHandler approvalChain;
    
    public BankFacade() {
        // Build the approval chain during initialization
        this.approvalChain = buildApprovalChain();
    }
    
    /**
     * Build the chain of responsibility for transaction approval
     */
    private ApprovalHandler buildApprovalChain() {
        AutoApprovalHandler autoHandler = new AutoApprovalHandler();
        ManagerApprovalHandler managerHandler = new ManagerApprovalHandler();
        AdminApprovalHandler adminHandler = new AdminApprovalHandler();
        
        autoHandler.setNext(managerHandler);
        managerHandler.setNext(adminHandler);
        return autoHandler;
    }
    
    /**
     * Create a new savings account
     */
    public Account createSavingsAccount(double initialBalance) {
        return new SavingsAccount(initialBalance);
    }
    
    /**
     * Create a new checking account
     */
    public Account createCheckingAccount(double initialBalance) {
        return new CheckingAccount(initialBalance);
    }
    
    /**
     * Create a new loan account
     */
    public Account createLoanAccount(double loanAmount, double interestRate) {
        return new LoanAccount(loanAmount, interestRate);
    }
    
    /**
     * Create a new investment account
     */
    public Account createInvestmentAccount(double initialInvestment) {
        return new InvestmentAccount(initialInvestment);
    }
    
    /**
     * Create an account group (composite)
     */
    public AccountGroup createAccountGroup(String groupName) {
        return new AccountGroup(groupName);
    }
    
    /**
     * Perform a transfer between two accounts
     */
    public boolean transfer(Account fromAccount, Account toAccount, double amount) {
        
        // Create transaction
        Transaction transaction = new Transaction(fromAccount, toAccount, amount, Transaction.TransactionType.TRANSFER);
        
        // Submit for approval
        boolean approved = approvalChain.handle(transaction);
        
        if (approved) {
            try {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
                return true;
            } catch (InsufficientFundsException e) {
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * Deposit to an account with approval
     */
    public boolean deposit(Account account, double amount) {
        try {
            account.deposit(amount);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Withdraw from an account with approval
     */
    public boolean withdraw(Account account, double amount) {
        
        // Create transaction for withdrawal
        Transaction transaction = new Transaction(account, account, amount, Transaction.TransactionType.WITHDRAWAL);
        
        // Submit for approval
        boolean approved = approvalChain.handle(transaction);
        
        if (approved) {
            try {
                account.withdraw(amount);
                return true;
            } catch (InsufficientFundsException e) {
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * Apply interest to an account
     */
    public void applyInterest(Account account) {
        account.applyInterest();
    }
    
    /**
     * Apply interest to all accounts in a group
     */
    public void applyInterestToGroup(AccountGroup group) {
        group.applyInterest();
    }
    
    /**
     * Set interest strategy for an account
     */
    public void setInterestStrategy(Account account, InterestStrategy strategy) {
        account.setInterestStrategy(strategy);
    }
    
    /**
     * Set interest strategy for all accounts in a group
     */
    public void setInterestStrategyForGroup(AccountGroup group, InterestStrategy strategy) {
        group.setInterestStrategy(strategy);
    }
    
    /**
     * Subscribe to account notifications
     */
    public void subscribeToNotifications(Account account, NotificationObserver observer) {
        account.subscribe(observer);
    }
    
    /**
     * Unsubscribe from account notifications
     */
    public void unsubscribeFromNotifications(Account account, NotificationObserver observer) {
        account.unsubscribe(observer);
    }
    
    /**
     * Get account balance
     */
    public double getBalance(Account account) {
        return account.getBalance();
    }
    
    /**
     * Get account description
     */
    public String getAccountDescription(Account account) {
        return account.getDescription();
    }
}
