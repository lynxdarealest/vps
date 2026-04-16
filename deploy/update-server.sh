#!/usr/bin/env bash
set -euo pipefail

APP_DIR="${APP_DIR:-/opt/rongthanonline}"
SERVICE_NAME="${SERVICE_NAME:-rongthanonline}"

cd "$APP_DIR"

echo "[1/4] Pull latest code"
git pull

echo "[2/4] Build server jar"
cd "$APP_DIR/server"
chmod +x ./mvnw ./run-server.sh
./mvnw -DskipTests package

echo "[3/4] Restart service"
sudo systemctl restart "$SERVICE_NAME"

echo "[4/4] Service status"
sudo systemctl --no-pager --full status "$SERVICE_NAME"

