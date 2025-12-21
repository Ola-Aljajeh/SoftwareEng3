# Design Rationale — Advanced Banking System

## Overview
This document explains why specific design patterns were chosen, the trade-offs considered, and how each pattern addresses the project’s functional and non‑functional requirements.

## Why these patterns (justification)
- **Composite (Account / AccountGroup)**: Accounts naturally form part–whole hierarchies (single accounts and groups). Composite lets clients treat an account group the same as a single account, simplifying facade and demo code and enabling group-level operations (e.g., apply interest to all members).
- **Chain of Responsibility (Approval chain)**: Transfer approvals depend on configurable, sequential checks (auto, manager, admin). The chain pattern models this flow cleanly and makes it easy to add new checks (AML, fraud) without changing existing handlers.
- **Strategy (Interest calculation)**: Interest policies vary (simple, compound, promotional). Encapsulating each algorithm in a Strategy allows swapping or adding new strategies at runtime or via configuration.
- **Observer (Notifications)**: Accounts must notify multiple channels (Email, SMS, In‑App). Observer decouples account logic from notification dispatch and supports many observers with minimal coupling.
- **Facade (BankFacade)**: The system exposes many subsystems (accounts, approval chain, interest, notifications). A facade simplifies usage for UI and demos and centralizes orchestration while keeping subsystems testable.
- **State (AccountState variants)**: Account behavior differs by state (Active, Frozen, Suspended, Closed). The State pattern isolates state-specific rules and makes transitions and auditing straightforward.

## Trade-offs and alternatives evaluated
- Composite vs explicit collection operations: Composite hides tree traversal and reduces client code, but it can mask group-specific constraints (e.g., permissions or rate-limiting). We accept this trade-off because the demo prioritizes simplicity and clarity; group-level constraints can be implemented in AccountGroup when needed.
- Chain of Responsibility vs centralized policy engine: A centralized policy engine offers global visibility and easier cross-cutting rules, but it adds complexity and setup overhead. Chain enables incremental addition of handlers and maps well to human approval workflows used in banking demos.
- Strategy vs configuration-based formulas: Hardcoded strategies ensure testability and clear examples. A configuration-driven expression system increases flexibility but would complicate the project and reduce readability for an educational codebase.
- Observer vs event-bus / message queue: An event bus or queue better suits scalable production systems. The Observer pattern keeps the implementation lightweight (stdout demo notifiers) that is appropriate for the educational scope and for deterministic tests.
- State vs conditional logic inside Account: Using State objects removes sprawling if/else blocks and keeps behavior testable per state. It slightly increases class count but vastly improves clarity and extensibility.

## How patterns address requirements
- FR: Account ops & grouping → handled by **Composite** and `Account` interface (uniform API supports deposit/withdraw/applyInterest). Tests: `AccountTests`.
- FR: Transfers & approvals → **Chain of Responsibility** (Approval handlers) implements progressive checks and clear escalation paths. Tests: `ApprovalChainTests`.
- FR: Interest policies → **Strategy** allows different algorithms to be chosen per account or scenario. Tests: `InterestStrategyTests`.
- FR: Notifications → **Observer** decouples message delivery from account logic; notifiers can be swapped or extended. Tests: `NotificationObserverTests`.
- FR: State-dependent behavior → **State** enforces per-state rules and transitions, preventing invalid operations (e.g., withdraw from Closed). Tests: `StatePatternTests`.
- NFRs: Maintainability & extensibility are addressed by small interfaces and single-responsibility classes (patterns encourage decomposition). Testability is improved because patterns localize behavior (e.g., testing strategies or handlers independently). CI automation ensures compatibility with Java 21 and regressions are caught early.

## Conclusion
These patterns were selected for clarity, pedagogical value, and their close fit to domain concepts (accounts, approvals, interest, notifications, state). The design favors explicit, well-tested components over maximal flexibility—an intentional trade-off to keep the project maintainable, extensible, and easy to reason about in an educational/demo context.
