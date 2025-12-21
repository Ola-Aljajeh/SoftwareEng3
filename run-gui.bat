@echo off
REM Banking System GUI Launcher for Windows

echo.
echo ================================================
echo  Advanced Banking System - Interactive GUI
echo ================================================
echo.

cd /d "%~dp0"

REM Check if compilation is needed
if not exist "target\classes\com\bankingsystem\BankingSystemGUI.class" (
    echo [*] Compiling source files...
    python compile-all.py
    if errorlevel 1 (
        echo [ERROR] Compilation failed!
        pause
        exit /b 1
    )
)

echo [*] Launching GUI (using Maven exec to include dependencies)...
set GUI_CLASS=com.bankingsystem.ModernBankingSystemGUI
if "%1"=="--classic" set GUI_CLASS=com.bankingsystem.BankingSystemGUI
if "%1"=="-c" set GUI_CLASS=com.bankingsystem.BankingSystemGUI
REM Prefer using Maven exec to ensure runtime dependencies (e.g., FlatLaf) are on the classpath
mvn -q -Dexec.mainClass=%GUI_CLASS% -Dexec.classpathScope=runtime exec:java
if errorlevel 1 (
    echo [*] Fallback: attempt to run with copied dependency classpath
    if not exist "target\dependency" (
        mvn dependency:copy-dependencies -DoutputDirectory=target/dependency
    )
    java -cp "target\classes;target\dependency\*" %GUI_CLASS%
)

if errorlevel 1 (
    echo [ERROR] Failed to launch GUI
    pause
    exit /b 1
)

echo [*] GUI closed.
pause
