#!/usr/bin/env bash
set -euo pipefail

APP_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_NAME="rongthanonline-0.0.1-SNAPSHOT.jar"

cd "$APP_DIR"

if [[ ! -f "target/$JAR_NAME" ]]; then
  echo "[INFO] Jar not found. Building server..."
  chmod +x ./mvnw
  ./mvnw -DskipTests package
fi

export SPRING_DATASOURCE_URL="${SPRING_DATASOURCE_URL:-jdbc:mysql://localhost:3306/rto?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Taipei}"
export SPRING_DATASOURCE_USERNAME="${SPRING_DATASOURCE_USERNAME:-rto_user}"
export SPRING_DATASOURCE_PASSWORD="${SPRING_DATASOURCE_PASSWORD:?Set SPRING_DATASOURCE_PASSWORD before running}"
export SERVER_PORT="${SERVER_PORT:-707}"
export SERVER_SWING_GUI_ENABLED="${SERVER_SWING_GUI_ENABLED:-false}"

exec java ${JAVA_OPTS:-"-Xms512m -Xmx2g"} -jar "target/$JAR_NAME"

