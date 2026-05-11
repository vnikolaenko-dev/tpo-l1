#!/bin/bash

mkdir -p results

echo "Running stress test with 9 users"
jmeter -n \
  -t test-plan.jmx \
  -Jthreads=9 \
  -l results/stress-9.jtl \
  -e \
  -o results/stress-9
