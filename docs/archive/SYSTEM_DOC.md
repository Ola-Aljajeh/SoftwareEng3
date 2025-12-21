# BankingSystem — System Documentation (SYSTEM_DOC.md)

> Comprehensive system documentation for the Advanced Banking System (Design Patterns Showcase).

---

## Table of Contents

1. Project Overview
2. Quick Start (Build & Run)
3. Architecture Summary
4. Modules & Responsibilities
5. Key Classes & Public API Reference
6. Usage Examples
7. Build, Test & CI
8. Configuration & Runtime
9. Extending the System (How-to Guides)
10. Troubleshooting & FAQs
11. Conventions & Notes
12. Glossary
13. Contribution & Maintenance

---

## 1. Project Overview ✅

**Purpose:** Educational, highly modular Java project demonstrating six classical design patterns: Composite, Chain of Responsibility, Strategy, Observer, Facade, and State. Focus is on pattern clarity, testability, and extensibility.

**Highlights:**
- Pure Java implementation (Java 17+), intentionally minimal external dependencies
- Well-separated modules (account, transaction, interest, notification, state, facade)
- Demo application: `com.bankingsystem.BankingSystemDemo`
- Build convenience script: `build.py` (recommended)

---

## 2. Quick Start (Build & Run) 🔧

Prerequisites:
- Java 17+
- (Optional) Maven 3.8+ if you want to run the full test suite
- Python (if using `build.py` convenience script)

Build & run (recommended):

- Build and run demo (recommended):

```bash
python build.py
```

- Direct javac compilation (manual):

```bash
javac -d target/classes -encoding UTF-8 src/main/java/com/bankingsystem/**/*.java
java -cp target/classes com.bankingsystem.BankingSystemDemo
```

Notes:
- The project intentionally removed external logging dependencies so it compiles with `javac` alone.
- If you want Maven-based testing, follow the suggestions in `BUILD_GUIDE.md` to fix SSL certificate issues or use offline mode.

---

## 3. Architecture Summary 🏗️

High-level structure:

- Facade: `BankFacade` — Single entry point exposing common operations (create accounts, transfer, set strategies, subscribe to notifications, apply interest)
- Account module (Composite): `Account` (interface), `BaseAccount`, `SavingsAccount`, `CheckingAccount`, `LoanAccount`, `InvestmentAccount`, `AccountGroup`
- Transaction module (Chain of Responsibility): `ApprovalHandler` chain: `AutoApprovalHandler` -> `ManagerApprovalHandler` -> `AdminApprovalHandler`
- Interest module (Strategy): `InterestStrategy` and concrete strategies (`SimpleInterestStrategy`, `CompoundInterestStrategy`, `PromotionalInterestStrategy`)
- Notification module (Observer): `NotificationObserver` and `EmailNotifier`, `SMSNotifier`, `InAppNotifier`
- State module (State): `AccountState` and concrete states (`ActiveState`, `FrozenState`, `SuspendedState`, `ClosedState`)

Refer to `ARCHITECTURE.md` for detailed ASCII diagrams, sequence diagrams, and rationale.

---

## 4. Modules & Responsibilities 🔍

### account (Composite)
- Responsibility: Represent accounts (leaf) and groups (composite) with uniform operations (deposit, withdraw, applyInterest, getBalance)
- Important classes: `Account`, `BaseAccount`, `AccountGroup`, `InsufficientFundsException`.

### transaction (Chain of Responsibility)
- Responsibility: Encapsulate transactions and determine approvals using a chain of handlers.
- Important classes: `Transaction`, `ApprovalHandler`, `AutoApprovalHandler`, `ManagerApprovalHandler`, `AdminApprovalHandler`, `ApprovalStatus`.

### interest (Strategy)
- Responsibility: Provide interchangeable interest calculation algorithms.
- Important classes: `InterestStrategy`, `SimpleInterestStrategy`, `CompoundInterestStrategy`, `PromotionalInterestStrategy`.

### notification (Observer)
- Responsibility: Publish account event notifications to multiple channels.
- Important classes: `NotificationObserver`, `EmailNotifier`, `SMSNotifier`, `InAppNotifier`.

### state (State)
- Responsibility: Manage account lifecycle behavior via state objects.
- Important classes: `AccountState`, `ActiveState`, `FrozenState`, `SuspendedState`, `ClosedState`.

### facade (Facade)
- Responsibility: Provide simplified API (BankFacade) to orchestrate the system and hide pattern complexity.
- Important class: `BankFacade`.

---

## 5. Key Classes & Public API Reference 📚

This is a concise API summary intended for developers integrating or extending the system.

### `BankFacade` (com.bankingsystem.facade.BankFacade)
- Purpose: Simplified entry point for clients.
- Constructor: `public BankFacade()` — builds the default approval chain.
- Key methods (high-level):
  - `Account createSavingsAccount(double initialBalance)`
  - `Account createCheckingAccount(double initialBalance)`
  - `Account createLoanAccount(double loanAmount, double interestRate)`
  - `Account createInvestmentAccount(double initialInvestment)`
  - `AccountGroup createAccountGroup(String groupName)`
  - `boolean transfer(Account from, Account to, double amount)` — Creates a `Transaction` and submits to approval chain; if approved executes withdraw/deposit.
  - `boolean deposit(Account account, double amount)`
  - `boolean withdraw(Account account, double amount)`
  - `void applyInterest(Account account)`
  - `void applyInterestToGroup(AccountGroup group)`
  - `void setInterestStrategy(Account account, InterestStrategy strategy)`
  - `void setInterestStrategyForGroup(AccountGroup group, InterestStrategy strategy)`
  - `void subscribeToNotifications(Account account, NotificationObserver observer)`
  - `void unsubscribeFromNotifications(Account account, NotificationObserver observer)`
  - `double getBalance(Account account)`
  - `String getAccountDescription(Account account)`

> Note: Use `BankFacade` for highest-level integration and presentation code; it shields clients from pattern details.

### `Account` (interface)
- Methods to implement: `getAccountId()`, `getBalance()`, `deposit(double)`, `withdraw(double) throws InsufficientFundsException`, `applyInterest()`, `setInterestStrategy(InterestStrategy)`, `getAccountType()`, `subscribe(NotificationObserver)`, `unsubscribe(NotificationObserver)`, `getDescription()`.

### `BaseAccount` (abstract)
- Implements common functionality: unique `accountId`, `balance` handling, `notifyObservers(message)`, interest delegation.
- Key behaviors: `deposit()` validates positive amount and notifies observers, `withdraw()` validates and may throw `InsufficientFundsException`.

### `AccountGroup` (composite)
- Composite that holds child `Account` objects.
- Composite operations: `getBalance()` (sum), `applyInterest()` (delegates), `setInterestStrategy()` (delegates), `subscribe()`/`unsubscribe()` (propagates to children), `getDescription()` (aggregate description).

### `Transaction` and `ApprovalHandler` chain
- `Transaction`: immutable id, from/to accounts, amount, type, status (PENDING/APPROVED/REJECTED) and timestamps.
- `ApprovalHandler`: abstract chain with `setNext(ApprovalHandler)` and `boolean handle(Transaction)`.
- Concrete handlers implement `canApprove()`, `shouldReject()`, and `getRejectionReason()`.
  - `AutoApprovalHandler`: approves ≤ $1,000
  - `ManagerApprovalHandler`: approves ≤ $10,000
  - `AdminApprovalHandler`: approves all

### `InterestStrategy` (interface)
- Methods: `double calculate(Account)` and `String getDescription()`.
- Implementations: `SimpleInterestStrategy`, `CompoundInterestStrategy`, `PromotionalInterestStrategy`.

### `NotificationObserver` (interface)
- Methods: `void update(String message)` and `String getObserverId()`.
- Implementations: `EmailNotifier`, `SMSNotifier`, `InAppNotifier` (print to stdout for demo).

### `AccountState` (interface) & States
- Encapsulate behavior differences for `withdraw`/`deposit` across states: `ActiveState`, `FrozenState`, `SuspendedState`, `ClosedState`.

---

## 6. Usage Examples (copy-ready) ✨

### Basic facade usage
```java
BankFacade bank = new BankFacade();
Account savings = bank.createSavingsAccount(1000);
Account checking = bank.createCheckingAccount(500);
bank.transfer(savings, checking, 200); // routed through approval chain
```

### Subscribe to notifications
```java
bank.subscribeToNotifications(savings, new EmailNotifier("user@example.com"));
bank.subscribeToNotifications(savings, new SMSNotifier("+1-555-0100"));

savings.deposit(100); // observers print notifications
```

### Apply interest strategy
```java
bank.setInterestStrategy(savings, new CompoundInterestStrategy(0.05, 12));
bank.applyInterest(savings);
```

### Account groups (composite + strategy)
```java
AccountGroup family = bank.createAccountGroup("Johnson Family");
family.addAccount(savings);
family.addAccount(checking);
bank.setInterestStrategyForGroup(family, new CompoundInterestStrategy(0.04, 4));
bank.applyInterestToGroup(family);
```

---

## 7. Build, Test & CI 🧪

### Build
- Recommended: `python build.py` — convenience script that compiles and runs demo.
- Manual compile:
  - `javac -d target/classes -encoding UTF-8 src/main/java/com/bankingsystem/**/*.java`
  - `java -cp target/classes com.bankingsystem.BankingSystemDemo`

### Tests
- Unit tests use **JUnit 5** and (where necessary) **Mockito** for mocking observers or state interactions.
- Run tests with Maven: `mvn test` (requires Maven and dependencies).
- If you encounter Maven SSL/certificate issues, see `BUILD_GUIDE.md` for workarounds (update Java cacerts or use Maven offline mode).

### CI Suggestions
- Simple CI pipeline steps: install JDK 17, run `python build.py` or run `javac` then `mvn test` if Maven is set up.
- If using Maven in CI, ensure Java keystore trusts your repository or use a secure mirror.

---

## 8. Configuration & Runtime ⚙️

- The project is intentionally minimal: there are no external configuration files for runtime behavior beyond standard code changes.
- `BankFacade` initializes a default approval chain; to change chain at startup, modify `BankFacade.buildApprovalChain()` or expose a constructor that accepts a pre-built chain.
- Logging: project removed SLF4J to remain dependency-free; notifications are printed to stdout in demo notifier implementations.

---

## 9. Extending the System — How-To Guides 🛠️

This section shows how to add typical extensions while keeping the code consistent with design principles.

### Add a new account type (leaf)
1. Create `class MyAccount extends BaseAccount`.
2. Implement `getAccountType()` and `getDescription()`.
3. Add creation helper to `BankFacade` (optional).

Example:
```java
public class SavingsWithRewards extends BaseAccount {
  public SavingsWithRewards(double initialBalance) { super(initialBalance); }
  public String getAccountType() { return "Savings (Rewards)"; }
  public String getDescription() { return "SavingsWithRewards..."; }
}
```

### Add a new interest strategy
1. Implement `InterestStrategy`.
2. Add tests to verify calculations.
3. Use `account.setInterestStrategy(new YourStrategy(...))`.

### Add a new approval handler
1. Extend `ApprovalHandler` and implement `canApprove()`, `shouldReject()` and `getRejectionReason()`.
2. Insert into chain in `BankFacade.buildApprovalChain()` or provide a chain builder API.

### Add a new notifier (channel)
1. Implement `NotificationObserver` with `update()` and `getObserverId()`.
2. Use `bank.subscribeToNotifications(account, new YourNotifier(...))`.

### Add a new account state
1. Implement `AccountState` and the state-specific behaviors.
2. Add state transitions using `account.setState(new YourState())` within your account implementations or via facade helper methods.

---

## 10. Troubleshooting & FAQs ❓

Q: Demo or compilation fails due to missing dependencies or certificate issues.
- A: Use `build.py` or compile with `javac` as the project is designed to build without Maven dependencies. If running tests with Maven, fix Java cacerts or use Maven offline mode (see `BUILD_GUIDE.md`).

Q: How to run tests without Maven?
- A: The unit tests are listed under `test/` and rely on JUnit/Mockito; using `mvn test` is the simplest route. Alternatively, install JUnit locally and run tests with a test runner, but Maven is strongly recommended for test dependency management.

Q: Notifications do not reach external systems (email/SMS).
- A: Notifier implementations in this repo are demos printing to stdout. For real delivery integrate with email/SMS providers and replace `update()` behavior.

Q: How to change approval thresholds?
- A: Update constants in handler classes (`AUTO_APPROVAL_LIMIT`, `MANAGER_APPROVAL_LIMIT`) or implement a config-driven handler.

---

## 11. Conventions & Notes 📝

- Naming conventions follow `com.bankingsystem.*`
- All code is Java 17 compatible; prefer `var` only when it improves readability
- Keep classes focused: one responsibility per class
- Write tests for every new strategy/handler/state/notifier/account type you add

---

## 12. Glossary 🔎

- Composite: pattern that treats single objects and composites uniformly
- Strategy: encapsulate interchangeable algorithms
- Observer: publish-subscribe mechanism for events
- Facade: simplified unified interface
- Chain of Responsibility: pass requests along a chain of handlers
- State: object encapsulating behavior based on state

---

## 13. Contribution & Maintenance 🤝

- Tests are required for new features and fixes
- Use small, focused PRs
- Document design motivations in PR description
- Keep `BankFacade` backward-compatible; prefer extension over modification

---

## Appendix — Important Files

- `ARCHITECTURE.md` (detailed diagrams & rationale)
- `BUILD_GUIDE.md` (build/run notes & troubleshooting)
- `README.md` (project overview & examples)
- `src/main/java/com/bankingsystem/` (all source packages)
- `test/` (unit & integration tests)

---

If you'd like, I can:
- Add a short reference table listing every public method with signatures and short descriptions
- Generate a `docs/` folder with HTML or markdown split into smaller topics
- Add further examples or integration tips (e.g., wiring into a web service)

---

Generated: 2025-12-17

---

*End of `SYSTEM_DOC.md`*