@echo off
REM run-tests.bat - Run Maven unit tests and print the test report location
REM Usage: run-tests.bat [<TestClass> | <TestClass>#<testMethod>]
























exit /b %RC%)  echo No surefire reports directory was found.) else (  dir target\surefire-reports /b  echo Listing files in target\surefire-reports:if exist target\surefire-reports (echo Test reports are available at: %CD%\target\surefire-reportsecho.)  echo Tests finished with errors (exit code %RC%).) else (  echo Tests completed successfully.if %RC% EQU 0 (echo.set RC=%ERRORLEVEL%)  mvn -Dtest=%~1 -DskipTests=false test  echo Running specified test: %~1) else (  mvn -DskipTests=false testif "%~1"=="" (echo Running Maven tests...nSETLOCAL