#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUTPUT_DIR="${TMPDIR:-/tmp}/getting-better-classes"

rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"

find "$ROOT_DIR/DSA" -name "*.java" -print0 | xargs -0 javac -d "$OUTPUT_DIR"

echo "Compiled DSA Java files into $OUTPUT_DIR"
