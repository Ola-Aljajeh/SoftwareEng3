package com.bankingsystem.account;

import com.bankingsystem.interest.InterestStrategy;
import com.bankingsystem.notification.NotificationObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * BaseAccount - Abstract implementation for leaf account types
 * 
 * Provides common functionality for all concrete account implementations:
 * - Balance management
 * - Notification observer pattern
 * - Interest strategy delegation
 */
public abstract class BaseAccount implements Account {
    
    protected final String accountId;
    protected double balance;
    protected InterestStrategy interestStrategy;
    protected List<NotificationObserver> observers;
    
    public BaseAccount(double initialBalance) {
        this.accountId = UUID.randomUUID().toString();
        this.balance = initialBalance;
        this.observers = new ArrayList<>();
    }
    
    @Override
    public String getAccountId() {
        return accountId;
    }
    
    @Override
    public double getBalance() {
        return balance;
    }
    
    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
        notifyObservers("Deposit of $" + amount + " completed. New balance: $" + balance);
    }
    
    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                String.format("Insufficient funds. Balance: %.2f, Requested: %.2f", balance, amount)
            );
        }
        balance -= amount;
        notifyObservers("Withdrawal of $" + amount + " completed. New balance: $" + balance);
    }
    
    @Override
    public void applyInterest() {
        if (interestStrategy != null) {
            double interest = interestStrategy.calculate(this);
            balance += interest;
            notifyObservers("Interest of $" + interest + " applied. New balance: $" + balance);
        }
    }
    
    @Override
    public void setInterestStrategy(InterestStrategy strategy) {
        this.interestStrategy = strategy;
    }
    
    @Override
    public void subscribe(NotificationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    @Override
    public void unsubscribe(NotificationObserver observer) {
        observers.remove(observer);
    }
    
    protected void notifyObservers(String message) {
        for (NotificationObserver observer : observers) {
            observer.update(getAccountType() + " (" + accountId + "): " + message);
        }
    }
}
