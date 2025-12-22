package com.bankingsystem.account;

import com.bankingsystem.interest.InterestStrategy;
import com.bankingsystem.notification.NotificationObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AccountGroup - Composite node in Composite Pattern
 * 
 * Treats a collection of accounts as a single account.
 * Enables hierarchical composition: family accounts, business portfolios, etc.
 * 
 * Operations on the group are delegated to all child accounts recursively.
 */
public class AccountGroup implements Account {
    
    private final String groupId;
    private final String groupName;
    private final List<Account> accounts;
    private final List<NotificationObserver> observers;
    
    public AccountGroup(String groupName) {
        this.groupId = UUID.randomUUID().toString();
        this.groupName = groupName;
        this.accounts = new ArrayList<>();
        this.observers = new ArrayList<>();
    }
    
    /**
     * Add an account to the group
     */
    public void addAccount(Account account) {
        accounts.add(account);
    }
    
    /**
     * Remove an account from the group
     */
    public void removeAccount(Account account) {
        accounts.remove(account);
    }
    
    /**
     * Get all accounts in the group
     */
    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }
    
    @Override
    public String getAccountId() {
        return groupId;
    }
    
    @Override
    public String getAccountType() {
        return "Account Group";
    }
    
    /**
     * Get combined balance of all accounts in the group
     * Composite operation: delegates to all children
     */
    @Override
    public double getBalance() {
        return accounts.stream()
            .mapToDouble(Account::getBalance)
            .sum();
    }
    
    /**
     * Deposit to primary account in group (typically first account)
     */
    @Override
    public void deposit(double amount) {
        if (accounts.isEmpty()) {
            throw new IllegalStateException("No accounts in group");
        }
        accounts.get(0).deposit(amount);
        notifyObservers("Group deposit of $" + amount + " to primary account");
    }
    
    /**
     * Withdraw from primary account in group
     */
    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (accounts.isEmpty()) {
            throw new IllegalStateException("No accounts in group");
        }
        accounts.get(0).withdraw(amount);
        notifyObservers("Group withdrawal of $" + amount + " from primary account");
    }
    
    /**
     * Apply interest to all accounts in the group
     * Composite operation: delegates to all children
     */
    @Override
    public void applyInterest() {
        for (Account account : accounts) {
            account.applyInterest();
        }
        notifyObservers("Interest applied to all " + accounts.size() + " accounts in group");
    }
    
    /**
     * Set interest strategy for all accounts in the group
     * Composite operation: delegates to all children
     */
    @Override
    public void setInterestStrategy(InterestStrategy strategy) {
        for (Account account : accounts) {
            account.setInterestStrategy(strategy);
        }
    }
    
    @Override
    public void subscribe(NotificationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            // Subscribe to all child accounts as well
            for (Account account : accounts) {
                account.subscribe(observer);
            }
        }
    }
    
    @Override
    public void unsubscribe(NotificationObserver observer) {
        observers.remove(observer);
        for (Account account : accounts) {
            account.unsubscribe(observer);
        }
    }
    
    private void notifyObservers(String message) {
        for (NotificationObserver observer : observers) {
            observer.update("Account Group (" + groupName + "): " + message);
        }
    }
    
    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account Group [").append(groupName).append("] - ").append(accounts.size()).append(" accounts\n");
        sb.append("Total Balance: $").append(getBalance()).append("\n");
        for (Account account : accounts) {
            sb.append("  - ").append(account.getDescription()).append("\n");
        }
        return sb.toString();
    }
}
