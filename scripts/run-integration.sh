#!/usr/bin/env bash

# abort if any shell cmd returns non-zero error status
set -e

# figure out where this script is located
SCRIPTS_DIR=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)
ROOT_DIR=$(dirname $SCRIPTS_DIR)

cd $ROOT_DIR
echo "build and run application tests"


./gradlew clean
LEAF_INTEGRATION=true ./gradlew :webapp:test --tests "com.leaflogistics.app.LeafApiServerIntegrationTest.testMaxInput"


