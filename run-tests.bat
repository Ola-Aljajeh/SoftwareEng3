@echo off
REM run-tests.bat - Run Maven unit tests and print the test report location
REM Usage: run-tests.bat [<TestClass> | <TestClass>#<testMethod>]
























SETLOCAL

echo Running Maven tests...
IF "%~1"=="" (
  mvn -DskipTests=false test
) ELSE (
  echo Running specified test: %~1
  mvn -Dtest=%~1 -DskipTests=false test
)

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