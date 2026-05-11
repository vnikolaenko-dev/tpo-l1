#!/bin/bash

mkdir -p results

jmeter -n \
-t test-plan.jmx \
-l results/load-test.jtl \
-e \
-o results/dashboard