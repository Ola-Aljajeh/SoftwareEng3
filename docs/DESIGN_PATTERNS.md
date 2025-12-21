# Design Patterns — Implementation Guide

This document explains each design pattern used in the BankingSystem project, how it was implemented here, the files & classes involved, where tests live, and short notes on how to extend or reuse the code.

---

## Overview
The project demonstrates six classical design patterns:
- Composite
- Chain of Responsibility
- Strategy
- Observer
- Facade
- State

Each section below contains:
- A short pattern intent summary
- Roles mapped to concrete classes in this project
- Implementation notes and design choices
- Tests that verify the behavior
- Quick extension tips

---

## Composite (Account hierarchy)
**Intent:** Compose objects into tree structures to represent part–whole hierarchies and let clients treat individual objects and compositions uniformly.

**Project roles & files:**
- Component: `Account` (interface) — `src/main/java/com/bankingsystem/account/Account.java`
- Leaf implementations: `BaseAccount`, `SavingsAccount`, `CheckingAccount`, `LoanAccount`, `InvestmentAccount` — `src/main/java/com/bankingsystem/account/*.java`
- Composite: `AccountGroup` — `src/main/java/com/bankingsystem/account/AccountGroup.java`

**How it's implemented:**
- `Account` declares operations common to all accounts (e.g., `deposit`, `withdraw`, `getBalance`, `applyInterest`, `subscribe/unsubscribe` for observers).
- `AccountGroup` holds a collection of `Account` instances and implements the same `Account` interface; operations on the group are forwarded to children (e.g., `deposit`, `applyInterest`) and can aggregate information (e.g., total balance).
- The composite enables treating single accounts and account groups uniformly — the facade and demos rely on this.

**Tests:** `src/test/java/com/bankingsystem/AccountTests.java`

**Extension tips:** Add decorators (e.g., `FeeDecorator`) or additional composite behaviors (group-level transfer rules) by implementing `Account` and delegating to an inner instance.

---

## Chain of Responsibility (Transaction approvals)
**Intent:** Pass a request along a chain of handlers; each handler can either process the request or forward it to the next handler.

**Project roles & files:**
- Handler base: `ApprovalHandler` — `src/main/java/com/bankingsystem/transaction/ApprovalHandler.java`
- Concrete handlers: `AutoApprovalHandler`, `AdminApprovalHandler`, `ManagerApprovalHandler` (as applicable) — `src/main/java/com/bankingsystem/transaction/*.java`
- Request object: `Transaction` & `ApprovalStatus` enum — `src/main/java/com/bankingsystem/transaction/Transaction.java`, `ApprovalStatus.java`

**How it's implemented:**
- Each `ApprovalHandler` implements logic to accept, reject, or escalate a `Transaction` by returning an `ApprovalStatus` or passing it to `next` handler.
- Handlers are composed at runtime (typically in the `BankFacade` or bootstrap code) to form the approval chain for transfers.

**Tests:** `src/test/java/com/bankingsystem/ApprovalChainTests.java`

**Extension tips:** Add new checks (AML, fraud scoring) by implementing new handlers and inserting them into the chain (no changes to existing handlers required).

---

## Strategy (Interest calculation)
**Intent:** Define a family of algorithms (strategies), encapsulate each one, and make them interchangeable.

**Project roles & files:**
- Strategy interface: `InterestStrategy` — `src/main/java/com/bankingsystem/interest/InterestStrategy.java`
- Concrete strategies: `SimpleInterestStrategy`, `CompoundInterestStrategy`, `PromotionalInterestStrategy` — `src/main/java/com/bankingsystem/interest/*.java`

**How it's implemented:**
- Interest logic is encapsulated in concrete `InterestStrategy` implementations.
- Accounts or the facade use strategy instances to compute interest; strategies are injectable enabling runtime changes to interest calculations.

**Tests:** `src/test/java/com/bankingsystem/InterestStrategyTests.java`

**Extension tips:** Add `TieredInterestStrategy` or `MarketLinkedStrategy` by implementing `InterestStrategy` and using dependency injection or factory methods to choose the right strategy.

---

## Observer (Notifications)
**Intent:** Define a one-to-many dependency such that when one object changes state, all its dependents are notified and updated automatically.

**Project roles & files:**
- Observer contract: `NotificationObserver` — `src/main/java/com/bankingsystem/notification/NotificationObserver.java`
- Concrete observers (notifiers): `EmailNotifier`, `SMSNotifier`, `InAppNotifier` — `src/main/java/com/bankingsystem/notification/*.java`
- Subjects: `Account` implementations provide `subscribe` / `unsubscribe` and `notifyObservers` behaviors.

**How it's implemented:**
- Accounts maintain a list of observers; when a state-change (deposit, withdrawal, interest applied, transfer) occurs, a message is sent to each observer.
- Demo and tests verify that messages (formatted strings) are generated; in the example implementations, notifiers print to stdout to keep the demo environment dependency-free.

**Tests:** `src/test/java/com/bankingsystem/NotificationObserverTests.java` (verifies observers receive expected notifications)

**Extension tips:** Replace the demo print-based notifiers with real adapters (SMTP client, SMS provider) behind the same `NotificationObserver` interface.

---

## Facade (BankFacade)
**Intent:** Provide a unified, higher-level interface to a set of interfaces in a subsystem. The facade simplifies usage for clients and demos.

**Project roles & files:**
- Facade: `BankFacade` — `src/main/java/com/bankingsystem/facade/BankFacade.java`
- Uses other subsystems: account management, transactions/approval chain, interest strategies, and notification registration.

**How it's implemented:**
- `BankFacade` exposes easy-to-use methods for common operations: create accounts, perform transfers (submitting transactions to approval chain), apply interest across accounts, and register notification observers.
- The GUI (`BankingSystemGUI.java`) and demo (`BankingSystemDemo.java`) use `BankFacade` exclusively, keeping UI code simple and decoupled from core behavior.

**Tests / Integration:** `src/test/java/com/bankingsystem/FacadeIntegrationTests.java` verifies the end-to-end behavior of facade operations.

**Extension tips:** Keep the facade thin; avoid adding heavy logic — delegate to subsystems to keep responsibilities separate.

---

## State (Account states)
**Intent:** Allow an object to alter its behavior when its internal state changes. The object will appear to change its class.

**Project roles & files:**
- State interface: `AccountState` — `src/main/java/com/bankingsystem/state/AccountState.java`
- Concrete states: `ActiveState`, `FrozenState`, `SuspendedState`, `ClosedState` — `src/main/java/com/bankingsystem/state/*.java`
- Accounts delegate state-sensitive operations to the current `AccountState`.

**How it's implemented:**
- `Account` implementations hold an `AccountState` reference and forward operations (e.g., `withdraw`, `deposit`, `transfer`) to the state instance which enforces the allowed behavior for that state.
- Example behaviors: a `FrozenState` may reject `withdraw` requests (or queue them), while an `ActiveState` permits normal operations.

**Tests:** `src/test/java/com/bankingsystem/StatePatternTests.java`

**Extension tips:** Add state transition rules or a state manager object to centralize transitions if you need richer workflows (e.g., timed unfreeze, manual review transitions).

---

## Testing, Demos, and Examples
- Integration tests and unit tests live in `src/test/java/com/bankingsystem/` (e.g., `FacadeIntegrationTests`, `ApprovalChainTests`).
- Demo programs are `BankingSystemDemo.java` and `BankingSystemGUI.java` (see top-level `run-demo.bat` and `run-gui.bat`).
- CI runs the full test suite and uploads `target/surefire-reports/` as the `test-results` artifact.

---

## Design notes & rationale
- Patterns are chosen to showcase separation of concerns, testability, and extensibility for an educational, production-quality demo.
- The code favors explicit interfaces, small classes, and composition over inheritance where appropriate.
- Notifiers use simple stdout implementations in the demo to avoid external dependencies; swapping to real services is straightforward.

---

## Where to find the code & tests
- Source: `src/main/java/com/bankingsystem/`
- Tests: `src/test/java/com/bankingsystem/`
- Documentation/overview: `COMPLETE_PROJECT_DOCUMENTATION.md`, `docs/REQUIREMENTS.md`

---

## Want more?
If you'd like, I can:
- Add sequence diagrams for each pattern in `diagrams/` and link them from this doc, or
- Add snippet-based examples (minimal code) directly into this file for each pattern, or
- Add a short section that ties specific CI artifacts (e.g., which test report file verifies each FR) for automated traceability.

---

*Generated and maintained by the repository maintainers.*
