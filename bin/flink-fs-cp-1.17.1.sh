#!/usr/bin/env bash

FLINK_HOME="${FLINK_HOME:-/opt/flink}"
FLINK_CONF_DIR="${FLINK_CONF_DIR:-${FLINK_HOME}/conf}"

export FLINK_CONF_DIR

java -cp "${FLINK_HOME}/opt/flink-fs-utils-1.17.1.jar:${FLINK_HOME}/lib/flink-dist-1.17.1.jar" \
    org.apache.flink.core.fs.CopyTool "$@"
