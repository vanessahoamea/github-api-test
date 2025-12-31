#!/usr/bin/env python3
import os
import sys
import re
import base64
from pathlib import Path

def sanitize_files(directory, api_key):
    if not api_key:
        print("No API key provided")
        return

    patterns = [
        re.escape(api_key),
        re.escape(base64.b64encode(api_key.encode()).decode()),
    ]
    extensions = {".html", ".json", ".xml"}

    processed = 0
    for path in Path(directory).rglob("*"):
        if path.is_dir():
            sanitize_files(path, api_key)
        elif path.is_file() and path.suffix.lower() in extensions:
            try:
                with open(path, "r", encoding="utf-8") as file:
                    content = file.read()

                original_content = content
                for pattern in patterns:
                    if pattern:
                        content = re.sub(pattern, "&lt;Bearer Token&gt;", content)

                if content != original_content:
                    with open(path, "w", encoding="utf-8") as file:
                        file.write(content)
                    processed += 1
                    print(f"Sanitized: {path}")
            except (UnicodeDecodeError, PermissionError, IsADirectoryError):
                continue

    if processed > 0:
        print(f"{directory}: Processed {processed} files")

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python sanitize_reports.py <directory> <api_key>")
        sys.exit(1)

    sanitize_files(sys.argv[1], sys.argv[2])