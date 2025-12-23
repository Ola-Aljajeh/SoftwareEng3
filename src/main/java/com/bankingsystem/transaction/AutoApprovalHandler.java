package com.bankingsystem.transaction;

/**
 * AutoApprovalHandler - First handler in Chain of Responsibility
 * 
 * Automatically approves transactions up to $1,000.
 */
public class AutoApprovalHandler extends ApprovalHandler {
    
    private static final double AUTO_APPROVAL_LIMIT = 1000.0;
    
    @Override
    protected boolean canApprove(Transaction transaction) {
        return transaction.getAmount() <= AUTO_APPROVAL_LIMIT;
    }
    
    @Override
    protected boolean shouldReject(Transaction transaction) {
        return false; // Auto approval doesn't reject, it passes to next handler
    }
    
    @Override
    protected String getRejectionReason(Transaction transaction) {
        return null;
    }
    
    public String getHandlerName() {
        return "Auto Approval Handler";
    }
    
    public double getApprovalLimit() {
        return AUTO_APPROVAL_LIMIT;
    }
}
