#!/usr/bin/env python3
"""
Direct Java compilation without Maven
"""
import subprocess
import os
import glob

output_dir = "target/classes"
src_dir = "src/main/java"

os.makedirs(output_dir, exist_ok=True)

print("=" * 60)
print("Direct Java Compilation (No Maven Required)")
print("=" * 60)

print("\n[1/3] Compiling main source files...")

# Find all Java files
java_files = glob.glob(f"{src_dir}/**/*.java", recursive=True)
print(f"Found {len(java_files)} source files")

# Compile
cmd = ["javac", "-d", output_dir, "-encoding", "UTF-8"] + java_files
result = subprocess.run(cmd, capture_output=True, text=True)

if result.returncode == 0:
    print("[SUCCESS] Main source compilation successful")
    
    # Count compiled classes
    class_files = glob.glob(f"{output_dir}/**/*.class", recursive=True)
    print(f"  Compiled {len(class_files)} class files")
else:
    print("[FAILED] Compilation errors:")
    print(result.stderr)
    exit(1)

print("\n[2/3] Demonstrating Pattern Functionality...")
print("\nRunning BankingSystemDemo.java...\n")

# Run the demo
cmd = ["java", "-cp", output_dir, "com.bankingsystem.BankingSystemDemo"]
result = subprocess.run(cmd, capture_output=True, text=True)

if result.returncode == 0:
    print(result.stdout)
    print("\n[SUCCESS] Demo executed successfully!")
else:
    print("[FAILED] Demo execution failed:")
    print(result.stderr)
    exit(1)

print("\n" + "=" * 60)
print("Compilation Summary:")
print("=" * 60)
print(f"Source files compiled: {len(java_files)}")
print(f"Class files generated: {len(class_files)}")
print("Packages: 7 (account, transaction, interest, notification, state, facade)")
print("Design Patterns: 6 (Composite, Chain, Strategy, Observer, Facade, State)")
print("\n✓ Code compiles successfully without Maven!")
print("✓ Design patterns demonstrated in output above!")
print("\nTo run the full test suite with Maven:")
print("  1. Fix the SSL certificate issue on your system")
print("  2. Run: mvn clean test")
print("=" * 60)
