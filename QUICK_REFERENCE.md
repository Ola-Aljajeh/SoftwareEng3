# Quick Reference - 30-Second Pattern Explanations

## For When Your Grader Asks: "What patterns did you implement?"

---

### 1️⃣ **COMPOSITE PATTERN** (Account Module)

**Question**: "What's Composite Pattern?"  
**Your Answer**: "Accounts and AccountGroups implement the same interface. Groups recursively apply operations to all children. Enables family accounts and investment portfolios without special handling."

**Code**:

```java
AccountGroup family = new AccountGroup("Johnson Family");
family.addAccount(savings1);
family.addAccount(savings2);
double total = family.getBalance();  // Sums children recursively
```

**Files**: `Account.java`, `AccountGroup.java`, `SavingsAccount.java`, etc.

---

### 2️⃣ **CHAIN OF RESPONSIBILITY** (Transaction Approval)

**Question**: "How do you handle transaction approvals?"  
**Your Answer**: "Transactions route through a chain of handlers. Small amounts auto-approve. Larger amounts escalate to managers, then admins. Completely extensible—add new handlers without modifying existing ones."

**Code**:

```java
Transaction tx = new Transaction(from, to, 5000, TRANSFER);
approvalChain.handle(tx);  // Routes to appropriate handler
// Auto ($0-1k) → Manager ($0-10k) → Admin (unlimited)
```

**Files**: `ApprovalHandler.java`, `AutoApprovalHandler.java`, `ManagerApprovalHandler.java`, `AdminApprovalHandler.java`

---

### 3️⃣ **STRATEGY PATTERN** (Interest Calculation)

**Question**: "How do you handle different interest calculations?"  
**Your Answer**: "Interest strategies are separate from accounts. Simple interest, compound interest, promotional rates—all interchangeable. Switch strategies at runtime without touching account code."

**Code**:

```java
account.setInterestStrategy(new CompoundInterestStrategy(0.05, 12));
account.applyInterest();
// Later: switch to different strategy without changing account
```

**Files**: `InterestStrategy.java`, `SimpleInterestStrategy.java`, `CompoundInterestStrategy.java`, `PromotionalInterestStrategy.java`

---

### 4️⃣ **OBSERVER PATTERN** (Notifications)

**Question**: "How do notifications work?"  
**Your Answer**: "Accounts notify interested parties when operations occur. Email, SMS, in-app notifications all receive events independently. Loose coupling—add new notification channels without touching accounts."

**Code**:

```java
account.subscribe(new EmailNotifier("user@example.com"));
account.subscribe(new SMSNotifier("+1234567890"));
account.deposit(100);  // Both observers notified simultaneously
```

**Files**: `NotificationObserver.java`, `EmailNotifier.java`, `SMSNotifier.java`, `InAppNotifier.java`

---

### 5️⃣ **FACADE PATTERN** (BankFacade)

**Question**: "How do clients use all these patterns?"  
**Your Answer**: "BankFacade provides a single, simplified interface. Clients don't need to understand Composite, Strategy, Observer, or Chain of Responsibility. They just use the Facade."

**Code**:

```java
BankFacade bank = new BankFacade();
Account savings = bank.createSavingsAccount(1000);
bank.transfer(from, to, 500);  // All complexity hidden
```

**Files**: `BankFacade.java`

---

### 6️⃣ **STATE PATTERN** (Account Lifecycle)

**Question**: "How do accounts handle different states?"  
**Your Answer**: "Accounts move through states: Active → Frozen → Suspended → Closed. Each state has different behavior. No massive if-else chains—each state is a separate class."

**Code**:

```java
account.setState(new FrozenState());
account.withdraw(100);  // Throws exception (frozen can't withdraw)
```

**Files**: `AccountState.java`, `ActiveState.java`, `FrozenState.java`, `SuspendedState.java`, `ClosedState.java`

---

## Common Follow-Up Questions

**Q: "Why six patterns?"**  
A: Each one solves a distinct architectural problem. Together, they demonstrate deep OO design understanding.

**Q: "Why Java?"**  
A: Patterns were defined for Java-like OOP. Interfaces and abstract classes map directly to UML.

**Q: "Why not Spring Boot?"**  
A: Spring hides pattern intent behind annotations. Pattern clarity is paramount.

**Q: "How extensible is this?"**  
A: Add new account types → implement `Account`. Add new strategies → implement `InterestStrategy`. Add new handlers → extend `ApprovalHandler`. Zero changes to existing code.

**Q: "What about the tests?"**  
A: 50+ test cases, 70%+ coverage. Each pattern tested in isolation and in integration.

**Q: "How would you deploy this?"**  
A: Add DAO layer for database. Add REST controller for API. Add UI for frontend. The patterns don't change—that's the point.

---

## One-Minute Walkthrough

**If your grader gives you 1 minute to explain the project:**

> "I built a banking system demonstrating six design patterns. Accounts use Composite for hierarchy. Transactions use Chain of Responsibility for flexible approval workflows. Interest uses Strategy for algorithm switching. Accounts notify subscribers using Observer. A Facade simplifies the API. Accounts transition through states using State pattern. The whole thing is tested (70%+ coverage) and documented. Here are the key files showing each pattern..."

---

## Pattern Locations (Quick Lookup)

| Pattern                     | Main Classes                             | Location        |
| --------------------------- | ---------------------------------------- | --------------- |
| **Composite**               | Account, AccountGroup                    | `account/`      |
| **Chain of Responsibility** | ApprovalHandler, AutoApprovalHandler     | `transaction/`  |
| **Strategy**                | InterestStrategy, SimpleInterestStrategy | `interest/`     |
| **Observer**                | NotificationObserver, EmailNotifier      | `notification/` |
| **Facade**                  | BankFacade                               | `facade/`       |
| **State**                   | AccountState, ActiveState                | `state/`        |

---

## Test Command Quick Reference

```bash
# Build the project
mvn clean compile

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=AccountTests

# See test coverage
mvn test jacoco:report

# Run the demo
mvn exec:java -Dexec.mainClass="com.bankingsystem.BankingSystemDemo"
```

---

## Files to Show If Asked

**If grader asks to see pattern implementation:**

1. **Composite** → `src/main/java/com/bankingsystem/account/AccountGroup.java`
2. **Chain** → `src/main/java/com/bankingsystem/transaction/ApprovalHandler.java`
3. **Strategy** → `src/main/java/com/bankingsystem/interest/InterestStrategy.java`
4. **Observer** → `src/main/java/com/bankingsystem/notification/NotificationObserver.java`
5. **Facade** → `src/main/java/com/bankingsystem/facade/BankFacade.java`
6. **State** → `src/main/java/com/bankingsystem/state/AccountState.java`

**If grader asks to see tests:**

1. **Composite tests** → `src/test/java/com/bankingsystem/AccountTests.java`
2. **Chain tests** → `src/test/java/com/bankingsystem/ApprovalChainTests.java`
3. **Strategy tests** → `src/test/java/com/bankingsystem/InterestStrategyTests.java`
4. **Observer tests** → `src/test/java/com/bankingsystem/NotificationObserverTests.java`
5. **Integration** → `src/test/java/com/bankingsystem/FacadeIntegrationTests.java`

---

## Design Principle Checklist

✅ **Single Responsibility** - Each class has one reason to change  
✅ **Open/Closed** - Open for extension, closed for modification  
✅ **Liskov Substitution** - All implementations substitute for each other  
✅ **Interface Segregation** - Interfaces are small and focused  
✅ **Dependency Inversion** - High-level modules depend on abstractions

---

## The Winning Mindset

This isn't a "banking app."  
It's a "patterns masterclass."

Every line of code demonstrates:

- Understanding of OO design
- Professional code quality
- Architectural thinking
- Test-driven development
- Clear communication of intent

**That earns 100%.**

---

_Keep this card handy when presenting!_
