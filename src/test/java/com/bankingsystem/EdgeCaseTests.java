package com.bankingsystem;

import com.bankingsystem.account.*;
import com.bankingsystem.notification.NotificationObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Edge Case Tests for Account behavior")
public class EdgeCaseTests {

    static class TestObserver implements NotificationObserver {
        int count = 0;
        String lastMessage = null;
        @Override
        public void update(String message) {
            count++;
            lastMessage = message;
        }
        @Override
        public String getObserverId() { return "test-observer"; }
    }

    @Test
    @DisplayName("Withdraw more than balance throws InsufficientFundsException")
    public void testWithdrawInsufficientFundsThrows() {
        Account a = new SavingsAccount(500.0);
        assertThrows(InsufficientFundsException.class, () -> a.withdraw(1000.0));
    }

    @Test
    @DisplayName("Deposit negative amount throws IllegalArgumentException")
    public void testDepositNegativeThrows() {
        Account a = new SavingsAccount(500.0);
        assertThrows(IllegalArgumentException.class, () -> a.deposit(-50.0));
    }

    @Test
    @DisplayName("Withdraw negative or zero throws IllegalArgumentException")
    public void testWithdrawNegativeThrows() {
        Account a = new SavingsAccount(500.0);
        assertThrows(IllegalArgumentException.class, () -> a.withdraw(0));
        assertThrows(IllegalArgumentException.class, () -> a.withdraw(-10.0));
    }

    @Test
    @DisplayName("SavingsAccount enforces minimum initial balance")
    public void testSavingsMinimumBalanceThrows() {
        assertThrows(IllegalArgumentException.class, () -> new SavingsAccount(50.0));
    }

    @Test
    @DisplayName("applyInterest with no strategy leaves balance unchanged")
    public void testApplyInterestNoStrategy() {
        SavingsAccount a = new SavingsAccount(500.0);
        double before = a.getBalance();
        a.applyInterest();
        assertEquals(before, a.getBalance());
    }

    @Test
    @DisplayName("Subscribing the same observer twice does not duplicate notifications")
    public void testSubscribeDuplicateObserver() throws InsufficientFundsException {
        SavingsAccount a = new SavingsAccount(500.0);
        TestObserver t = new TestObserver();
        a.subscribe(t);
        a.subscribe(t); // duplicate subscribe should be ignored
        a.deposit(100.0);
        assertEquals(1, t.count);
        assertNotNull(t.lastMessage);
    }
}
