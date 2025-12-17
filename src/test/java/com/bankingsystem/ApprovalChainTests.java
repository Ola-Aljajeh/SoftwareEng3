package com.bankingsystem;

import com.bankingsystem.transaction.*;
import com.bankingsystem.account.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Transaction module (Chain of Responsibility Pattern)
 */
@DisplayName("Transaction Module Tests - Chain of Responsibility Pattern")
public class ApprovalChainTests {
    
    private ApprovalHandler approvalChain;
    private Account account1;
    private Account account2;
    
    @BeforeEach
    public void setUp() {
        // Build the approval chain
        AutoApprovalHandler autoHandler = new AutoApprovalHandler();
        ManagerApprovalHandler managerHandler = new ManagerApprovalHandler();
        AdminApprovalHandler adminHandler = new AdminApprovalHandler();
        
        autoHandler.setNext(managerHandler);
        managerHandler.setNext(adminHandler);
        
        this.approvalChain = autoHandler;
        
        account1 = new SavingsAccount(50000.0);
        account2 = new SavingsAccount(50000.0);
    }
    
    @Test
    @DisplayName("Small transaction should be auto-approved")
    public void testSmallTransactionAutoApproved() {
        Transaction transaction = new Transaction(account1, account2, 500.0, Transaction.TransactionType.TRANSFER);
        
        boolean approved = approvalChain.handle(transaction);
        
        assertTrue(approved);
        assertEquals(ApprovalStatus.APPROVED, transaction.getStatus());
    }
    
    @Test
    @DisplayName("Small transaction at limit should be auto-approved")
    public void testTransactionAtAutoLimit() {
        Transaction transaction = new Transaction(account1, account2, 1000.0, Transaction.TransactionType.TRANSFER);
        
        boolean approved = approvalChain.handle(transaction);
        
        assertTrue(approved);
        assertEquals(ApprovalStatus.APPROVED, transaction.getStatus());
    }
    
    @Test
    @DisplayName("Medium transaction should require manager approval")
    public void testMediumTransactionManagerApproved() {
        Transaction transaction = new Transaction(account1, account2, 5000.0, Transaction.TransactionType.TRANSFER);
        
        boolean approved = approvalChain.handle(transaction);
        
        assertTrue(approved);
        assertEquals(ApprovalStatus.APPROVED, transaction.getStatus());
    }
    
    @Test
    @DisplayName("Medium transaction at limit should be approved")
    public void testTransactionAtManagerLimit() {
        Transaction transaction = new Transaction(account1, account2, 10000.0, Transaction.TransactionType.TRANSFER);
        
        boolean approved = approvalChain.handle(transaction);
        
        assertTrue(approved);
        assertEquals(ApprovalStatus.APPROVED, transaction.getStatus());
    }
    
    @Test
    @DisplayName("Large transaction should require admin approval")
    public void testLargeTransactionAdminApproved() {
        Transaction transaction = new Transaction(account1, account2, 50000.0, Transaction.TransactionType.TRANSFER);
        
        boolean approved = approvalChain.handle(transaction);
        
        assertTrue(approved);
        assertEquals(ApprovalStatus.APPROVED, transaction.getStatus());
    }
    
    @Test
    @DisplayName("Very large transaction should be admin-approved")
    public void testVeryLargeTransactionAdminApproved() {
        Transaction transaction = new Transaction(account1, account2, 1000000.0, Transaction.TransactionType.TRANSFER);
        
        boolean approved = approvalChain.handle(transaction);
        
        assertTrue(approved);
        assertEquals(ApprovalStatus.APPROVED, transaction.getStatus());
    }
    
    @Test
    @DisplayName("Transaction should capture details correctly")
    public void testTransactionDetails() {
        Transaction transaction = new Transaction(account1, account2, 500.0, Transaction.TransactionType.TRANSFER);
        
        assertEquals(account1, transaction.getFromAccount());
        assertEquals(account2, transaction.getToAccount());
        assertEquals(500.0, transaction.getAmount());
        assertEquals(Transaction.TransactionType.TRANSFER, transaction.getType());
        assertEquals(ApprovalStatus.PENDING, transaction.getStatus());
    }
    
    @Test
    @DisplayName("Rejection reason should be captured")
    public void testRejectionReason() {
        // Create a chain that will reject transactions above a certain amount
        ManagerApprovalHandler managerHandler = new ManagerApprovalHandler();
        AdminApprovalHandler adminHandler = new AdminApprovalHandler();
        managerHandler.setNext(adminHandler);
        
        Transaction transaction = new Transaction(account1, account2, 15000.0, Transaction.TransactionType.TRANSFER);
        
        boolean approved = managerHandler.handle(transaction);
        
        assertTrue(approved); // Admin approves it
        
        // Test a transaction that would be rejected by manager if no admin handler
        ManagerApprovalHandler managerOnly = new ManagerApprovalHandler();
        Transaction rejectedTx = new Transaction(account1, account2, 15000.0, Transaction.TransactionType.TRANSFER);
        
        boolean rejected = managerOnly.handle(rejectedTx);
        assertFalse(rejected);
        assertNotNull(rejectedTx.getRejectionReason());
    }
    
    @Test
    @DisplayName("Transaction type should be recorded correctly")
    public void testTransactionTypes() {
        Transaction transfer = new Transaction(account1, account2, 100.0, Transaction.TransactionType.TRANSFER);
        Transaction withdrawal = new Transaction(account1, account2, 100.0, Transaction.TransactionType.WITHDRAWAL);
        Transaction deposit = new Transaction(account1, account2, 100.0, Transaction.TransactionType.DEPOSIT);
        Transaction payment = new Transaction(account1, account2, 100.0, Transaction.TransactionType.PAYMENT);
        
        assertEquals(Transaction.TransactionType.TRANSFER, transfer.getType());
        assertEquals(Transaction.TransactionType.WITHDRAWAL, withdrawal.getType());
        assertEquals(Transaction.TransactionType.DEPOSIT, deposit.getType());
        assertEquals(Transaction.TransactionType.PAYMENT, payment.getType());
    }
    
    @Test
    @DisplayName("Approval status should transition correctly")
    public void testApprovalStatusTransition() {
        Transaction transaction = new Transaction(account1, account2, 500.0, Transaction.TransactionType.TRANSFER);
        
        assertEquals(ApprovalStatus.PENDING, transaction.getStatus());
        
        approvalChain.handle(transaction);
        
        assertEquals(ApprovalStatus.APPROVED, transaction.getStatus());
    }
}
