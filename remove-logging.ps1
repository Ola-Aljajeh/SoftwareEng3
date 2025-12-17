# PowerShell script to remove all SLF4J logging from Java source files
# This allows the code to compile without Maven dependencies

Write-Host "Removing SLF4J logging from all source files..." -ForegroundColor Cyan

$srcDir = "src\main\java"
$javaFiles = Get-ChildItem -Path $srcDir -Filter "*.java" -Recurse

foreach ($file in $javaFiles) {
    $content = Get-Content $file.FullName -Raw
    $modified = $false
    
    if ($content -match "org\.slf4j") {
        $content = $content -replace "import org\.slf4j\.Logger;\s*`n", ""
        $content = $content -replace "import org\.slf4j\.LoggerFactory;\s*`n", ""
        $content = $content -replace "`n\s*private static final Logger logger = LoggerFactory\.getLogger\([^)]+\);", ""
        $content = $content -replace "`n\s*protected static final Logger logger = LoggerFactory\.getLogger\([^)]+\);", ""
        $content = $content -replace "`s+logger\.debug\([^;]+\);", ""
        $content = $content -replace "`s+logger\.info\([^;]+\);", ""
        $content = $content -replace "`s+logger\.warn\([^;]+\);", ""
        $modified = $true
    }
    
    if ($modified) {
        Set-Content $file.FullName -Value $content -Encoding UTF8
        Write-Host "✓ $($file.Name)" -ForegroundColor Green
    }
}

Write-Host "Logging removal complete!" -ForegroundColor Cyan
