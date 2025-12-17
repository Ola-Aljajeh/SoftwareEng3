package com.bankingsystem;

import com.bankingsystem.notification.*;
import com.bankingsystem.account.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * Unit tests for Notification module (Observer Pattern)
 */
@DisplayName("Notification Module Tests - Observer Pattern")
public class NotificationObserverTests {
    
    @Mock
    private NotificationObserver mockObserver;
    
    private Account account;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new SavingsAccount(1000.0);
    }
    
    @Test
    @DisplayName("Deposit should notify observers")
    public void testDepositNotification() {
        account.subscribe(mockObserver);
        account.deposit(100.0);
        
        verify(mockObserver).update(contains("Deposit"));
    }
    
    @Test
    @DisplayName("Withdrawal should notify observers")
    public void testWithdrawalNotification() throws InsufficientFundsException {
        account.subscribe(mockObserver);
        account.withdraw(100.0);
        
        verify(mockObserver).update(contains("Withdrawal"));
    }
    
    @Test
    @DisplayName("Multiple observers should all be notified")
    public void testMultipleObservers() {
        NotificationObserver observer1 = mock(NotificationObserver.class);
        NotificationObserver observer2 = mock(NotificationObserver.class);
        NotificationObserver observer3 = mock(NotificationObserver.class);
        
        account.subscribe(observer1);
        account.subscribe(observer2);
        account.subscribe(observer3);
        
        account.deposit(100.0);
        
        verify(observer1).update(contains("Deposit"));
        verify(observer2).update(contains("Deposit"));
        verify(observer3).update(contains("Deposit"));
    }
    
    @Test
    @DisplayName("Unsubscribed observer should not be notified")
    public void testUnsubscribe() {
        account.subscribe(mockObserver);
        account.unsubscribe(mockObserver);
        account.deposit(100.0);
        
        verify(mockObserver, never()).update(anyString());
    }
    
    @Test
    @DisplayName("Interest application should notify observers")
    public void testInterestNotification() {
        account.subscribe(mockObserver);
        account.setInterestStrategy(new com.bankingsystem.interest.SimpleInterestStrategy(0.05));
        account.applyInterest();
        
        verify(mockObserver).update(contains("Interest"));
    }
    
    @Test
    @DisplayName("EmailNotifier should be functional")
    public void testEmailNotifier() {
        NotificationObserver emailNotifier = new EmailNotifier("test@example.com");
        account.subscribe(emailNotifier);
        
        // Should not throw exception
        account.deposit(100.0);
    }
    
    @Test
    @DisplayName("SMSNotifier should be functional")
    public void testSMSNotifier() {
        NotificationObserver smsNotifier = new SMSNotifier("+1234567890");
        account.subscribe(smsNotifier);
        
        // Should not throw exception
        account.deposit(100.0);
    }
    
    @Test
    @DisplayName("InAppNotifier should be functional")
    public void testInAppNotifier() {
        NotificationObserver inAppNotifier = new InAppNotifier("user123");
        account.subscribe(inAppNotifier);
        
        // Should not throw exception
        account.deposit(100.0);
    }
    
    @Test
    @DisplayName("Observer duplicate subscriptions should be prevented")
    public void testDuplicateSubscription() {
        account.subscribe(mockObserver);
        account.subscribe(mockObserver); // Subscribe again
        account.deposit(100.0);
        
        // Observer should only be called once, not twice
        verify(mockObserver, times(1)).update(anyString());
    }
    
    @Test
    @DisplayName("Account group notifications should work")
    public void testAccountGroupNotifications() {
        NotificationObserver observer = mock(NotificationObserver.class);
        
        Account account1 = new SavingsAccount(500.0);
        Account account2 = new CheckingAccount(300.0);
        
        AccountGroup group = new AccountGroup("Test Group");
        group.addAccount(account1);
        group.addAccount(account2);
        group.subscribe(observer);
        
        group.deposit(100.0);
        
        verify(observer).update(contains("Group deposit"));
    }
}
