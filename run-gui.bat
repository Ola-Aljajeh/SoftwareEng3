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

echo [*] Launching GUI...
set GUI_CLASS=com.bankingsystem.ModernBankingSystemGUI
if "%1"=="--classic" set GUI_CLASS=com.bankingsystem.BankingSystemGUI
if "%1"=="-c" set GUI_CLASS=com.bankingsystem.BankingSystemGUI
java -cp target\classes %GUI_CLASS%

if errorlevel 1 (
    echo [ERROR] Failed to launch GUI
    pause
    exit /b 1
)

echo [*] GUI closed.
pause
