#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

if [[ ! -f "./mvnw" ]]; then
	echo "Error: ./mvnw not found. Run this from a Maven Wrapper project."
	exit 1
fi

