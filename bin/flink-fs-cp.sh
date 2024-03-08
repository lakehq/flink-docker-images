#!/usr/bin/env bash

FLINK_HOME="${FLINK_HOME:-/opt/flink}"
FLINK_CONF_DIR="${FLINK_CONF_DIR:-${FLINK_HOME}/conf}"

export FLINK_CONF_DIR

FLINK_VERSION="$(basename "${FLINK_HOME}"/lib/flink-dist-*.jar)"
FLINK_VERSION="${FLINK_VERSION#flink-dist-}"
FLINK_VERSION="${FLINK_VERSION%.jar}"

java -cp "${FLINK_HOME}/opt/flink-fs-utils-${FLINK_VERSION}.jar:${FLINK_HOME}/lib/flink-dist-${FLINK_VERSION}.jar" \
    org.apache.flink.core.fs.CopyTool "$@"
