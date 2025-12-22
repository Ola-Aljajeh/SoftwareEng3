package com.bankingsystem.notification;

/**
 * SMSNotifier - Concrete Observer
 * 
 * Receives notifications and sends them via SMS.
 */
public class SMSNotifier implements NotificationObserver {
    
    private final String observerId;
    private final String phoneNumber;
    
    public SMSNotifier(String phoneNumber) {
        this.observerId = "sms-" + System.currentTimeMillis();
        this.phoneNumber = phoneNumber;
    }
    
    @Override
    public void update(String message) {
        // In a real system, this would send an actual SMS
        System.out.println("[SMS] " + phoneNumber + " -> " + message);
    }
    
    @Override
    public String getObserverId() {
        return observerId;
    }
    
    @Override
    public String toString() {
        return "SMSNotifier{" + phoneNumber + "}";
    }
}
