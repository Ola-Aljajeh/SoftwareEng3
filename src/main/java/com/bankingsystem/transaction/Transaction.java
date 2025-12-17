package com.bankingsystem.transaction;

import com.bankingsystem.account.Account;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction - Represents a financial transaction
 * 
 * Encapsulates transaction details and requires approval through Chain of Responsibility.
 */
public class Transaction {
    
    public enum TransactionType {
        TRANSFER, WITHDRAWAL, DEPOSIT, PAYMENT
    }
    
    private final String transactionId;
    private final Account fromAccount;
    private final Account toAccount;
    private final double amount;
    private final TransactionType type;
    private final LocalDateTime timestamp;
    private ApprovalStatus status;
    private String rejectionReason;
    
    public Transaction(Account fromAccount, Account toAccount, double amount, TransactionType type) {
        this.transactionId = UUID.randomUUID().toString();
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.status = ApprovalStatus.PENDING;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public Account getFromAccount() {
        return fromAccount;
    }
    
    public Account getToAccount() {
        return toAccount;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public ApprovalStatus getStatus() {
        return status;
    }
    
    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String reason) {
        this.rejectionReason = reason;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Transaction[ID: %s, Type: %s, Amount: %.2f, Status: %s, Time: %s]",
            transactionId, type, amount, status, timestamp
        );
    }
}
