package com.bankingsystem.notification;

/**
 * NotificationObserver - Observer in Observer Pattern
 * 
 * Defines the contract for objects that want to be notified of account events.
 */
public interface NotificationObserver {
    
    /**
     * Called when an observed account experiences an event
     * 
     * @param message the notification message
     */
    void update(String message);
    
    /**
     * Get the observer's identifier
     */
    String getObserverId();
}
