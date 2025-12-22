package com.bankingsystem.notification;

/**
 * InAppNotifier - Concrete Observer
 * 
 * Receives notifications and displays them within the application.
 */
public class InAppNotifier implements NotificationObserver {
    
    private final String observerId;
    private final String userId;
    
    public InAppNotifier(String userId) {
        this.observerId = "app-" + System.currentTimeMillis();
        this.userId = userId;
    }
    
    @Override
    public void update(String message) {
        // In a real system, this would push notification to user's dashboard/interface
        System.out.println("[IN-APP] User " + userId + " -> " + message);
    }
    
    @Override
    public String getObserverId() {
        return observerId;
    }
    
    @Override
    public String toString() {
        return "InAppNotifier{" + userId + "}";
    }
}
