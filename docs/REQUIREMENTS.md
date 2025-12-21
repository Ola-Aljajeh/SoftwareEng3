# Requirements — BankingSystem

This document lists the Functional Requirements (FR) and Non‑Functional Requirements (NFR) for the project and maps each requirement to implementation files and test classes (traceability matrix).

---

## Quick summary ✅

- Java target: **Java 21 (LTS)**
- Build: **Maven** (3.8+)
- Tests: **JUnit 5 + Mockito** (run via Maven Surefire)
- CI: **GitHub Actions** (file: `.github/workflows/ci.yml`)

---

## Functional Requirements (FR)

| ID    | Requirement                                                                   | Implemented in                                                                                                                   | Tests                                                            |
| ----- | ----------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------- |
| FR-01 | Account management (create/query/manage accounts & groups)                    | `src/main/java/com/bankingsystem/account/` (`Account`, `AccountGroup`, `BaseAccount`, concrete accounts)                         | `src/test/java/com/bankingsystem/AccountTests.java`              |
| FR-02 | Deposits & Withdrawals (balance updates, insuff. funds handling)              | account classes (`deposit`, `withdraw`)                                                                                          | `AccountTests`                                                   |
| FR-03 | Transfers using approval chain (auto, manager, admin handlers)                | `src/main/java/com/bankingsystem/transaction/` (`ApprovalHandler`, `AutoApprovalHandler`, `AdminApprovalHandler`, `Transaction`) | `src/test/java/com/bankingsystem/ApprovalChainTests.java`        |
| FR-04 | Interest calculation via pluggable strategies (simple, compound, promotional) | `src/main/java/com/bankingsystem/interest/` (`InterestStrategy*`)                                                                | `src/test/java/com/bankingsystem/InterestStrategyTests.java`     |
| FR-05 | Notification delivery (Email, SMS, In‑App observers)                          | `src/main/java/com/bankingsystem/notification/` (`EmailNotifier`, `SMSNotifier`, `InAppNotifier`)                                | `src/test/java/com/bankingsystem/NotificationObserverTests.java` |
| FR-06 | Account state machine behavior (Active, Frozen, Suspended, Closed)            | `src/main/java/com/bankingsystem/state/`                                                                                         | `src/test/java/com/bankingsystem/StatePatternTests.java`         |
| FR-07 | Facade API for integration & demos (`BankFacade`)                             | `src/main/java/com/bankingsystem/facade/BankFacade.java`                                                                         | `src/test/java/com/bankingsystem/FacadeIntegrationTests.java`    |
| FR-08 | Demo (console & GUI) run scripts                                              | `run-demo.bat`, `run-gui.bat`; `BankingSystemDemo.java`, `BankingSystemGUI.java`                                                 | Manual / demo-oriented tests (see README / demo instructions)    |

---

## Non-Functional Requirements (NFR)

- **NFR-01 Java version** — Target Java LTS (Java **21**). Confirmed in `pom.xml` compiler `<release>21</release>` and in CI (`.github/workflows/ci.yml`).
- **NFR-02 Build reproducibility** — Build with Maven (3.8+): `mvn -DskipTests=false package` (CI runs the same command).
- **NFR-03 CI & automation** — GitHub Actions runs build + tests; workflow file: `.github/workflows/ci.yml`.
- **NFR-04 Test coverage** — Coverage target **>= 70%** (enforced by JaCoCo `jacoco:check`). The CI run will fail if coverage falls below the threshold. Note: demo and GUI classes (`BankingSystemDemo`, `BankingSystemGUI`) are excluded from the coverage bundle to focus the gate on testable logic classes.
- **NFR-05 Testing quality** — Both unit and integration tests are included and executed in CI (integration-style tests are part of Surefire test phase).
- **NFR-06 Minimal dependencies** — Keep project lightweight (plain Java + Maven); heavy frameworks avoided. See `pom.xml` for dependency list.
- **NFR-07 Portability** — Windows helper scripts and CI Ubuntu runs ensure portability across platforms.
- **NFR-08 Documentation** — Single canonical documentation file: `COMPLETE_PROJECT_DOCUMENTATION.md` (previous docs archived under `docs/archive/`).
- **NFR-09 Artifact availability** — CI uploads `test-results` artifact (Surefire reports) reachable from workflow run.
- **NFR-10 Extensibility** — Modular design (patterns & interfaces) to allow addition of new account types, interest strategies, and approval handlers.

---

## Traceability matrix (compact)

- FR-01 → `src/main/java/com/bankingsystem/account/` → `AccountTests`
- FR-02 → `src/main/java/com/bankingsystem/account/*` → `AccountTests`
- FR-03 → `src/main/java/com/bankingsystem/transaction/` → `ApprovalChainTests`
- FR-04 → `src/main/java/com/bankingsystem/interest/` → `InterestStrategyTests`
- FR-05 → `src/main/java/com/bankingsystem/notification/` → `NotificationObserverTests`
- FR-06 → `src/main/java/com/bankingsystem/state/` → `StatePatternTests`
- FR-07 → `src/main/java/com/bankingsystem/facade/BankFacade.java` → `FacadeIntegrationTests`

---

## How to validate / run tests 🔁

- Run full test suite locally: `run-tests.bat` (or `mvn -DskipTests=false test`).
- Run coverage and validation locally (matches CI): `mvn -DskipTests=false test jacoco:report jacoco:check`
- Run Checkstyle locally: `mvn checkstyle:check` (Checkstyle is configured to warn by default; CI also runs it and uploads the report).
- CI: check workflow `.github/workflows/ci.yml` and Actions runs. Recent successful run: see Actions page for the `CI` workflow.
- Inspect test reports: `target/surefire-reports/` (uploaded as `test-results` artifact by CI).

---

## Notes & next steps 💡

- I can add automated checks (e.g., a GitHub Action to fail the build if coverage < 70%).
- If you want, I can add requirement IDs as Java annotations or link tests systematically to a requirements-check script.

---

_Generated automatically by the repository maintainer tooling._
