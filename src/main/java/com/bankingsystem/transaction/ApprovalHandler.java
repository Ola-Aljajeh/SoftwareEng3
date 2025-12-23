package com.bankingsystem.transaction;

/**
 * ApprovalHandler - Abstract handler in Chain of Responsibility Pattern
 * 
 * Defines the approval chain for transactions based on amount thresholds.
 * Each handler decides whether to approve, reject, or pass to the next handler.
 */
public abstract class ApprovalHandler {
    
    protected ApprovalHandler nextHandler;
    
    /**
     * Set the next handler in the chain
     */
    public void setNext(ApprovalHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    
    /**
     * Handle the transaction approval
     * 
     * @param transaction the transaction to approve
     * @return true if approved, false if rejected
     */
    public boolean handle(Transaction transaction) {
        if (canApprove(transaction)) {
            transaction.setStatus(ApprovalStatus.APPROVED);
            return true;
        } else if (shouldReject(transaction)) {
            transaction.setStatus(ApprovalStatus.REJECTED);
            transaction.setRejectionReason(getRejectionReason(transaction));
            return false;
        } else {
            // Pass to next handler
            if (nextHandler != null) {
                return nextHandler.handle(transaction);
            } else {
                transaction.setStatus(ApprovalStatus.REJECTED);
                transaction.setRejectionReason("Transaction exceeds maximum approval limit");
                return false;
            }
        }
    }
    
    /**
     * Determine if this handler can approve the transaction
     */
    protected abstract boolean canApprove(Transaction transaction);
    
    /**
     * Determine if this handler should reject the transaction
     */
    protected abstract boolean shouldReject(Transaction transaction);
    
    /**
     * Get the rejection reason
     */
    protected abstract String getRejectionReason(Transaction transaction);
}
