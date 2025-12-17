#!/usr/bin/env python3
"""
Remove multi-line logger statements that were left incomplete
"""
import glob
import os
import re

src_dir = "src/main/java"
pattern = os.path.join(src_dir, "**/*.java")

for java_file in glob.glob(pattern, recursive=True):
    with open(java_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original = content
    
    # Remove multi-line logger statements
    # Pattern: logger.XXX("text, 
    #              more params");
    content = re.sub(
        r'strategy\.getClass\(\)\.getSimpleName\(\)[,\s]+\w+(?:\.\w+)*\(\)\);',
        '';',
        content
    )
    
    # Remove dangling method arguments that were part of logger calls
    content = re.sub(
        r'strategy\.getClass\(\)\.getSimpleName\(\),\s+\w+(?:\.\w+)*\(\)\);',
        ';',
        content
    )
    
    content = re.sub(
        r'transaction\.getTra[^\)]*Id\(\),\s+transaction\.getAmount\(\)\);',
        ';',
        content
    )
    
    if content != original:
        with open(java_file, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"✓ {os.path.basename(java_file)}")
    else:
        print(f"- {os.path.basename(java_file)}")

print(f"Done")
