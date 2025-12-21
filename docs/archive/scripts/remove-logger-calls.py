#!/usr/bin/env python3
"""
Remove remaining logger calls from Java files
"""
import re
import glob
import os

src_dir = "src/main/java"
pattern = os.path.join(src_dir, "**/*.java")

for java_file in glob.glob(pattern, recursive=True):
    with open(java_file, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    new_lines = []
    for line in lines:
        # Skip lines that only contain logger calls (with indentation)
        if re.match(r'\s*logger\.(debug|info|warn|error)\(', line):
            continue
        new_lines.append(line)
    
    with open(java_file, 'w', encoding='utf-8') as f:
        f.writelines(new_lines)
    
    print(f"✓ {os.path.basename(java_file)}")

print(f"Done removing all logger calls")
