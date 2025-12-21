# Tools & Dependencies — BankingSystem (Regenerated)

This file documents the runtime, build, test and maintenance tools used in the repository and gives actionable commands and notes for developers.

---

## Languages & Runtimes
- **Java 21 (LTS)** — primary runtime for app and tests. CI uses Temurin Java 21 (e.g., 21.0.9+10). Confirm with `mvn -v` and `java -version`.
- **Python 3** — used for maintenance scripts in `docs/archive/scripts/` and for the PlantUML render helper `tools/render_plantuml.py` (uses standard library only).
- **Shell / Batch** — Windows `.bat` helpers: `run-tests.bat`, `run-demo.bat`, `run-gui.bat`. Use PowerShell on Windows.

---

## Build & Packaging
- **Apache Maven** — primary build tool (run `mvn -B -DskipTests=false test package`).
- Plugins in `pom.xml`:
  - `maven-compiler-plugin` (v3.11.0) — configured with `<release>21</release>`.
  - `maven-surefire-plugin` (v3.1.2) — runs tests and generates `target/surefire-reports/`.

Quick commands:
- Full build + tests: `mvn -B -DskipTests=false package`
- Run a single test class: `mvn -Dtest=com.bankingsystem.AccountTests test`

---

## Testing & QA
- **JUnit 5 (junit-jupiter 5.10.0)** — unit and integration tests.
- **Mockito (5.5.0)** — mocking in tests.
- **Surefire reports** located at `target/surefire-reports/` and uploaded as a CI artifact named `test-results`.
- Test helper: `run-tests.bat` (runs Maven test command and lists generated reports).

Tip: To debug a failing test locally, run the specific test method: `mvn -Dtest=ApprovalChainTests#testSmallTransactionAutoApproved test`.

---

## Continuous Integration
- **GitHub Actions** workflow: `.github/workflows/ci.yml` (job `build`) — checks out code, sets up JDK 21 via `actions/setup-java@v4`, runs Maven, uploads test artifacts using `actions/upload-artifact@v4`.
- Runner: `ubuntu-latest` (Ubuntu 24.04 image). The CI run sets `JAVA_HOME` to the provisioned JDK and runs `mvn -B -DskipTests=false test package`.

---

## Version Control & Release
- **Git / GitHub** — source control, branches, tags and releases. The repository has an annotated release `v1.0.0-java21`.
- **GitHub CLI (gh)** — handy for inspecting runs and artifacts from the command line (used in maintenance: `gh run list`, `gh run view <id> --log`).

---

## Diagrams & Rendering
- PlantUML sources: `diagrams/*.puml` (class and sequence diagrams).
- Rendered PNGs are stored in `docs/`:
  - `docs/class-diagram.png`
  - `docs/sequence-transfer.png`
  - `docs/sequence-interest.png`
  - `docs/sequence-notification.png`
- Helper script to (re)render diagrams: `tools/render_plantuml.py` (no extra dependencies; uses the public PlantUML server). Run:

  ```powershell
  python tools/render_plantuml.py
  ```

- If you prefer offline rendering, install PlantUML and Graphviz and adapt the script to call a local renderer, or add a GitHub Action to generate images on push.

---

## Maintenance & Helper Scripts
- `run-tests.bat` — helper for running tests and listing reports.
- Demo starters: `run-demo.bat`, `run-gui.bat`.
- Python maintenance scripts (archived): `docs/archive/scripts/` contains `remove-logging.py`, `remove-logger-calls.py`, `fix-method-calls.py`, `compile-all.py`, `build.py`.
- PlantUML render helper: `tools/render_plantuml.py` — encodes and fetches PNGs from the public PlantUML server.

---

## Observability & Misc
- `logback.xml` (in `src/main/resources`) for demo logging configuration.
- Tests may load byte-buddy or other agents during execution (observed as warnings in CI logs); this is test-time behavior only.

---

## Recommendations & Notes
- Keep local JDK and Maven versions aligned with CI to avoid `--release` or compilation mismatches.
- Consider adding a GitHub Action or workflow step to auto-render PlantUML sources into checked-in images (useful for PR previews).
- If you want, I can add a `Makefile` or additional scripts to standardize developer workflows (build, test, render diagrams).

---

*Regenerated: 2025-12-21*