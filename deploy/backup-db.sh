#!/usr/bin/env bash
set -euo pipefail

DB_NAME="${DB_NAME:-rto}"
DB_USER="${DB_USER:-rto_user}"
BACKUP_DIR="${BACKUP_DIR:-$HOME/backups}"

mkdir -p "$BACKUP_DIR"
OUT="$BACKUP_DIR/${DB_NAME}_$(date +%F_%H-%M-%S).sql"

mysqldump -u "$DB_USER" -p "$DB_NAME" > "$OUT"
echo "Backup saved: $OUT"

