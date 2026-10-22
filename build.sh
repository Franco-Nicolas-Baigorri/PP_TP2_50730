#!/usr/bin/env bash
set -eu

cd -- "$(dirname -- "$0")"
mkdir -p cls events
trap 'rm -f sources.tmp' EXIT

find src -type f -name '*.java' -print > sources.tmp
javac -encoding UTF-8 -d cls @sources.tmp
java -Dfile.encoding=UTF-8 -cp cls App
