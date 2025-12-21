# COMPLETE PROJECT DOCUMENTATION — Advanced Banking System

## Overview
This repository contains an educational banking system demonstrating six classical design patterns (Composite, Chain of Responsibility, Strategy, Observer, Facade, State). The project is implemented in Java and intended to be easy to run and test.

## Quick facts
- Language: Java (target: **Java 21 LTS**)
- Build: Maven
- Tests: JUnit 5 + Mockito
- CI: GitHub Actions (JDK 21)

---

## Contents
- Project purpose & patterns implemented
- How to build & run the demo
- How to run tests (doctor’s instructions)
- Architecture summary and key files
- Troubleshooting & notes

---

## 1) How to build & run
Prerequisites:
- Java 21 (JDK) installed and JAVA_HOME pointing to it
- Maven 3.8+

Build and run:
- Build (compile + tests): `mvn -DskipTests=false package`
- Build only: `mvn -DskipTests=true package`
- Run demo (GUI or console): see top-level `run-gui.bat` and `run-demo.bat`

---

## 2) Unit testing (doctor-ready instructions)
We use JUnit 5 and Mockito for unit testing. Tests live in `src/test/java/com/bankingsystem/` and use Maven Surefire to run.

To run tests (recommended):
- From project root, run: `run-tests.bat`
  - This helper runs Maven tests and lists generated reports (`target/surefire-reports/`).
- To run a single test class: `run-tests.bat ApprovalChainTests`
- To run a single test method: `run-tests.bat ApprovalChainTests#testSmallTransactionAutoApproved`

Test reports location: `target/surefire-reports/` (XML + human readable .txt)

---

## 3) Architecture summary
See the examples in `src/main/java/com/bankingsystem/` for modules and patterns. Key packages:
- `account` — Composite pattern (Account, AccountGroup, concrete accounts)
- `transaction` — Chain of Responsibility (approval handlers)
- `interest` — Strategy pattern (interest calculations)
- `notification` — Observer pattern (Email, SMS, InApp)
- `state` — Account states (Active, Frozen, Suspended, Closed)
- `facade` — `BankFacade` simplified API

---

## 4) CI
A GitHub Actions workflow was added to run the Maven build and tests under Java 21. The workflow file is `.github/workflows/ci.yml`.

---

## 5) Troubleshooting
- If Maven runs under the wrong JDK, set `JAVA_HOME` and ensure `mvn -v` shows Java 21.
- If tests fail, run the single failing test with `run-tests.bat <TestClass>#<method>` and check `target/surefire-reports/` for stack traces.

---

## 6) Where the original docs went
All previous Markdown documentation has been moved to `docs/archive/` for safekeeping. The repository now uses this single canonical document.

**Archived scripts:** Migration and helper Python scripts used during maintenance (e.g., `remove-logging.py`, `fix-method-calls.py`, `compile-all.py`, `build.py`) have been archived under `docs/archive/scripts/`.

---

## Contact
If you want any section expanded or prefer a different organization, tell me which sections to include/omit and I'll revise the document.
