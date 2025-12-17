package com.bankingsystem.transaction;

/**
 * AdminApprovalHandler - Final handler in Chain of Responsibility
 * 
 * Approves all transactions (no limit).
 * This is the ultimate authority in the approval chain.
 */
public class AdminApprovalHandler extends ApprovalHandler {
    
    private static final double UNLIMITED = Double.MAX_VALUE;
    
    @Override
    protected boolean canApprove(Transaction transaction) {
        return true; // Admin approves everything
    }
    
    @Override
    protected boolean shouldReject(Transaction transaction) {
        return false;
    }
    
    @Override
    protected String getRejectionReason(Transaction transaction) {
        return null;
    }
    
    public String getHandlerName() {
        return "Admin Approval Handler";
    }
    
    public double getApprovalLimit() {
        return UNLIMITED;
    }
}
