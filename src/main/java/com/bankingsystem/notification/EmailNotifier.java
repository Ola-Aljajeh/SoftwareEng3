package com.bankingsystem.notification;

/**
 * EmailNotifier - Concrete Observer
 * 
 * Receives notifications and sends them via email.
 */
public class EmailNotifier implements NotificationObserver {
    
    private final String observerId;
    private final String emailAddress;
    
    public EmailNotifier(String emailAddress) {
        this.observerId = "email-" + System.currentTimeMillis();
        this.emailAddress = emailAddress;
    }
    
    @Override
    public void update(String message) {
        // In a real system, this would send an actual email
        System.out.println("[EMAIL] " + emailAddress + " -> " + message);
    }
    
    @Override
    public String getObserverId() {
        return observerId;
    }
    
    @Override
    public String toString() {
        return "EmailNotifier{" + emailAddress + "}";
    }
}
