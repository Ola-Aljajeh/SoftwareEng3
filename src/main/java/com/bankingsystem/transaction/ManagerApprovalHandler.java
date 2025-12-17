package com.bankingsystem.transaction;

/**
 * ManagerApprovalHandler - Second handler in Chain of Responsibility
 * 
 * Approves transactions up to $10,000.
 * Rejects transactions above this limit.
 */
public class ManagerApprovalHandler extends ApprovalHandler {
    
    private static final double MANAGER_APPROVAL_LIMIT = 10000.0;
    
    @Override
    protected boolean canApprove(Transaction transaction) {
        return transaction.getAmount() <= MANAGER_APPROVAL_LIMIT;
    }
    
    @Override
    protected boolean shouldReject(Transaction transaction) {
        // Manager approval is the highest level in the basic chain
        // If it doesn't fit our limit, pass to next (Director) or reject
        return false;
    }
    
    @Override
    protected String getRejectionReason(Transaction transaction) {
        return String.format("Amount %.2f exceeds manager approval limit of %.2f",
            transaction.getAmount(), MANAGER_APPROVAL_LIMIT);
    }
    
    public String getHandlerName() {
        return "Manager Approval Handler";
    }
    
    public double getApprovalLimit() {
        return MANAGER_APPROVAL_LIMIT;
    }
}
