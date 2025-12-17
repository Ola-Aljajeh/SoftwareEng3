package com.bankingsystem.transaction;

/**
 * ApprovalStatus - Enumeration for transaction approval states
 */
public enum ApprovalStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    ApprovalStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
