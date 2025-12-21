@echo off
REM run-tests.bat - Run Maven unit tests and print the test report location
REM Usage: run-tests.bat [<TestClass> | <TestClass>#<testMethod> | --full | -f]
























SETLOCAL

echo Running Maven tests...
IF "%~1"=="" GOTO run_default
IF /I "%~1"=="--full" GOTO run_full
IF /I "%~1"=="-f" GOTO run_full
GOTO run_named

:run_default
mvn -DskipTests=false test
GOTO after_mvn

:run_full
echo Running full validation (tests, coverage and checkstyle)...
mvn -DskipTests=false test jacoco:report jacoco:check checkstyle:check
GOTO after_mvn

:run_named
echo Running specified test: %~1
mvn -Dtest=%~1 -DskipTests=false test
GOTO after_mvn

:after_mvn

SET RC=%ERRORLEVEL%
echo.
IF %RC% EQU 0 (
  echo Tests completed successfully.
) ELSE (
  echo Tests finished with errors (exit code %RC%).
)
echo.
echo Test reports are available at: %CD%\target\surefire-reports
IF exist target\surefire-reports (
  echo Listing files in target\surefire-reports:
  dir target\surefire-reports /b
) ELSE (
  echo No surefire reports directory was found.
)
ENDLOCAL
exit /b %RC%