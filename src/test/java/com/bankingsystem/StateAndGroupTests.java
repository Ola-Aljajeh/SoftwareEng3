package com.bankingsystem;

import com.bankingsystem.account.Account;
import com.bankingsystem.account.AccountGroup;
import com.bankingsystem.account.SavingsAccount;
import com.bankingsystem.account.InsufficientFundsException;
import com.bankingsystem.state.ActiveState;
import com.bankingsystem.state.ClosedState;
import com.bankingsystem.state.FrozenState;
import com.bankingsystem.state.SuspendedState;
import com.bankingsystem.notification.NotificationObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("State and AccountGroup tests")
public class StateAndGroupTests {

    static class TestObserver implements NotificationObserver {
        int count = 0;
        String lastMessage = null;
        @Override
        public void update(String message) {
            count++;
            lastMessage = message;
        }
        @Override
        public String getObserverId() { return "state-test"; }
    }

    @Test
    @DisplayName("ActiveState: withdraw negative throws")
    public void activeWithdrawNegativeThrows() {
        Account a = new SavingsAccount(100.0);
        ActiveState s = new ActiveState();
        assertThrows(IllegalArgumentException.class, () -> s.withdraw(a, -10.0));
    }

    @Test
    @DisplayName("ActiveState: withdraw insufficient funds throws")
    public void activeWithdrawInsufficientThrows() {
        Account a = new SavingsAccount(100.0);
        ActiveState s = new ActiveState();
        assertThrows(InsufficientFundsException.class, () -> s.withdraw(a, 200.0));
    }

    @Test
    @DisplayName("ActiveState: deposit negative throws")
    public void activeDepositNegativeThrows() {
        Account a = new SavingsAccount(100.0);
        ActiveState s = new ActiveState();
        assertThrows(IllegalArgumentException.class, () -> s.deposit(a, -5.0));
    }

    @Test
    @DisplayName("ClosedState: operations are forbidden")
    public void closedStateOperationsForbidden() {
        Account a = new SavingsAccount(100.0);
        ClosedState s = new ClosedState();
        assertThrows(IllegalStateException.class, () -> s.withdraw(a, 10.0));
        assertThrows(IllegalStateException.class, () -> s.deposit(a, 10.0));
        assertEquals("Closed", s.getStateName());
    }

    @Test
    @DisplayName("FrozenState: withdraw forbidden; deposit validation")
    public void frozenStateBehavior() {
        Account a = new SavingsAccount(100.0);
        FrozenState s = new FrozenState();
        assertThrows(IllegalStateException.class, () -> s.withdraw(a, 10.0));
        assertEquals("Frozen", s.getStateName());
        assertThrows(IllegalArgumentException.class, () -> s.deposit(a, -3.0));
    }

    @Test
    @DisplayName("SuspendedState: all operations forbidden")
    public void suspendedStateBehavior() {
        Account a = new SavingsAccount(100.0);
        SuspendedState s = new SuspendedState();
        assertThrows(IllegalStateException.class, () -> s.withdraw(a, 1.0));
        assertThrows(IllegalStateException.class, () -> s.deposit(a, 1.0));
        assertEquals("Suspended", s.getStateName());
    }

    @Test
    @DisplayName("AccountGroup: empty group operations throw")
    public void accountGroupEmptyThrows() {
        AccountGroup g = new AccountGroup("G");
        assertThrows(IllegalStateException.class, () -> g.deposit(10.0));
        assertThrows(IllegalStateException.class, () -> g.withdraw(10.0));
    }

    @Test
    @DisplayName("AccountGroup: deposit/withdraw delegate and notify observers")
    public void accountGroupDepositWithdrawAndNotify() throws InsufficientFundsException {
        AccountGroup g = new AccountGroup("Family");
        SavingsAccount a = new SavingsAccount(100.0);
        g.addAccount(a);
        TestObserver t = new TestObserver();
        g.subscribe(t);
        g.deposit(50.0);
        assertEquals(150.0, a.getBalance());
        // group subscribes the observer to both the group and child accounts, so two notifications are expected
        assertEquals(2, t.count);
        assertNotNull(t.lastMessage);

        g.withdraw(30.0);
        assertEquals(120.0, a.getBalance());
        assertEquals(4, t.count);
    }

    @Test
    @DisplayName("AccountGroup: getDescription includes child descriptions")
    public void accountGroupDescriptionIncludesChildren() {
        AccountGroup g = new AccountGroup("Family");
        SavingsAccount a = new SavingsAccount(200.0);
        g.addAccount(a);
        String desc = g.getDescription();
        assertTrue(desc.contains("Account Group [Family]"));
        assertTrue(desc.contains("Total Balance"));
        assertTrue(desc.contains(a.getDescription()));
    }
}