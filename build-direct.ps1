# Direct Java compilation without Maven network dependencies
# This script compiles all source files and demonstrates pattern functionality

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "Direct Java Compilation (No Maven Required)" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan

$srcDir = "src\main\java"
$outputDir = "target\classes"
$testDir = "src\test\java"
$testOutputDir = "target\test-classes"

# Create output directories
New-Item -ItemType Directory -Path $outputDir -Force | Out-Null
New-Item -ItemType Directory -Path $testOutputDir -Force | Out-Null

Write-Host "`n[1/3] Compiling main source files..." -ForegroundColor Yellow

# Find all Java files in main
$javaFiles = Get-ChildItem -Path $srcDir -Filter "*.java" -Recurse | ForEach-Object { $_.FullName }

if ($javaFiles.Count -eq 0) {
    Write-Host "ERROR: No Java files found in $srcDir" -ForegroundColor Red
    exit 1
}

Write-Host "Found $($javaFiles.Count) source files" -ForegroundColor Gray

# Compile main source
javac -d $outputDir -encoding UTF-8 $javaFiles
if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Main source compilation successful" -ForegroundColor Green
} else {
    Write-Host "✗ Main source compilation failed" -ForegroundColor Red
    exit 1
}

# Count compiled classes
$classFiles = Get-ChildItem -Path $outputDir -Filter "*.class" -Recurse
Write-Host "  Compiled $($classFiles.Count) class files" -ForegroundColor Green

Write-Host "`n[2/3] Compiling test files..." -ForegroundColor Yellow

$testFiles = Get-ChildItem -Path $testDir -Filter "*.java" -Recurse | ForEach-Object { $_.FullName }

if ($testFiles.Count -eq 0) {
    Write-Host "No test files found - tests require JUnit/Mockito which need Maven" -ForegroundColor Gray
} else {
    # Attempt to compile tests (will fail without JUnit/Mockito on classpath)
    Write-Host "Note: Full test compilation requires JUnit and Mockito jars" -ForegroundColor Gray
}

Write-Host "`n[3/3] Demonstrating Pattern Functionality..." -ForegroundColor Yellow

# Run the demo class (which exists and shows pattern functionality)
Write-Host "`nRunning BankingSystemDemo.java..." -ForegroundColor Cyan
java -cp $outputDir com.bankingsystem.BankingSystemDemo

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n✓ Demo executed successfully!" -ForegroundColor Green
} else {
    Write-Host "`n✗ Demo execution failed" -ForegroundColor Red
}

Write-Host "`n================================================" -ForegroundColor Cyan
Write-Host "Compilation Summary:" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "Source files compiled: $(($javaFiles | Measure-Object).Count)" -ForegroundColor Green
Write-Host "Class files generated: $($classFiles.Count)" -ForegroundColor Green
Write-Host "Packages: 7 (account, transaction, interest, notification, state, facade, core)" -ForegroundColor Green
Write-Host "Design Patterns: 6 (Composite, Chain, Strategy, Observer, Facade, State)" -ForegroundColor Green
Write-Host "`nTo run the full test suite with Maven:" -ForegroundColor Yellow
Write-Host "  1. Fix the SSL certificate issue on your system" -ForegroundColor Gray
Write-Host "  2. Run: mvn clean test" -ForegroundColor Gray
Write-Host "`nFor Maven network fix, try:" -ForegroundColor Yellow
Write-Host "  mvn help:active-profiles -X 2>&1 | findstr /i certificate" -ForegroundColor Gray
Write-Host "`n================================================" -ForegroundColor Cyan
