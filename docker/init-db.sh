#!/bin/sh
set -e

echo "⏳ Waiting a bit for SQL Server to be fully ready..."
sleep 10

echo "🚀 Initializing database ${MSSQL_DB}..."
/opt/mssql-tools18/bin/sqlcmd -C -S mssql,1433 -U sa -P "${SA_PASSWORD}" \
  -v MSSQL_DB="${MSSQL_DB}" \
  -i /init-db.sql

echo "✅ Database init done."
