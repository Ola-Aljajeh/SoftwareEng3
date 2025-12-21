#!/usr/bin/env python3
"""
Fix lines with just method calls left from logger removal
"""
import re

files = [
    "src/main/java/com/bankingsystem/transaction/ApprovalHandler.java"
]

for file_path in files:
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original = content
    
    # Remove lines that are just indented method call parts
    # Pattern: spaces + this.getClass().getSimpleName(), transaction.getXxx(), transaction.getYyy());
    content = re.sub(
        r'\s+this\.getClass\(\)\.getSimpleName\(\), [^\n]+\n',
        '\n',
        content
    )
    
    if content != original:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"✓ Fixed {file_path}")

print("Done")
