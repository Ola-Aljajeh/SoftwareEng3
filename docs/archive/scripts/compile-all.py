#!/usr/bin/env python3
"""Compile all Java files including new GUI"""
import subprocess
import glob
import os

os.makedirs("target/classes", exist_ok=True)

# Find all Java files
java_files = glob.glob("src/main/java/**/*.java", recursive=True)
print(f"Compiling {len(java_files)} Java files...")

cmd = ["javac", "-d", "target/classes", "-encoding", "UTF-8"] + java_files
result = subprocess.run(cmd, capture_output=True, text=True)

if result.returncode == 0:
    print("✓ Compilation successful!")
    print(f"  Compiled {len(java_files)} source files")
else:
    print("✗ Compilation failed:")
    lines = result.stderr.split('\n')
    for line in lines[:15]:  # Show first 15 errors
        if line.strip():
            print(f"  {line}")

exit(result.returncode)
