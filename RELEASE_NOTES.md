# Release v1.0.0-java21 — Java 21 LTS upgrade

**Release date**: 2025-12-21

## Summary
This release upgrades the project to **Java 21 (LTS)** and includes related quality-of-life improvements and documentation consolidation.

## Notable changes
- Upgraded project build to target **Java 21** (maven.compiler.release = 21)
- Verified the project compiles and **all tests pass** under JDK 21
- Added GitHub Actions CI workflow running on **JDK 21** (`.github/workflows/ci.yml`)
- Consolidated documentation into `COMPLETE_PROJECT_DOCUMENTATION.md` and archived previous docs under `docs/archive/`
- Added `run-tests.bat` to simplify running unit tests and listing test reports
- Cleaned up branches and stashes related to the upgrade

## Notes for maintainers
- To reproduce tests locally: `run-tests.bat` or `mvn -DskipTests=false test`
- Release tag: `v1.0.0-java21`

If you want a different tag name (e.g., `v1.0.0`), tell me and I can re-tag and update the release.
