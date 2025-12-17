#!/usr/bin/env python3
"""
Remove SLF4J logging from all Java source files to enable compilation without Maven
"""
import re
import glob
import os

src_dir = "src/main/java"
pattern = os.path.join(src_dir, "**/*.java")

count = 0
for java_file in glob.glob(pattern, recursive=True):
    with open(java_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original = content
    
    # Remove SLF4J imports
    content = re.sub(r'import org\.slf4j\.Logger;\s*\n', '', content)
    content = re.sub(r'import org\.slf4j\.LoggerFactory;\s*\n', '', content)
    
    # Remove logger field declarations
    content = re.sub(r'\n\s*private static final Logger logger = LoggerFactory\.getLogger\([^)]+\);', '', content)
    content = re.sub(r'\n\s*protected static final Logger logger = LoggerFactory\.getLogger\([^)]+\);', '', content)
    
    # Remove logger method calls
    content = re.sub(r'\s*logger\.debug\([^)]*\);\s*\n', '\n', content)
    content = re.sub(r'\s*logger\.info\([^)]*\);\s*\n', '\n', content)
    content = re.sub(r'\s*logger\.warn\([^)]*\);\s*\n', '\n', content)
    
    # Clean up extra blank lines
    content = re.sub(r'\n\n\n+', '\n\n', content)
    
    if content != original:
        with open(java_file, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"✓ {os.path.basename(java_file)}")
        count += 1

print(f"\nProcessed {count} files")
