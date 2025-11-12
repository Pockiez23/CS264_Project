# Docker quickstart

## Run API against an existing SQL Server (SSMS)
1. Ensure the SQL Server instance allows TCP connections on port 1433 and that the login exists.
2. Export the credentials before starting Docker Compose:
   ```bash
   export SPRING_DATASOURCE_URL="jdbc:sqlserver://host.docker.internal:1433;instanceName=MSSQLSERVER01;databaseName=PetitionDB;encrypt=false;trustServerCertificate=true"
   export SPRING_DATASOURCE_USERNAME="sa"
   export SPRING_DATASOURCE_PASSWORD="12345"
   ```
   *On Windows PowerShell use* `setx` *or* `$env:SPRING_DATASOURCE_URL = "..."` *instead.*
3. Start the API container:
   ```bash
   docker compose up api
   ```
   The application will read the variables and connect directly to your SSMS database.

## Optional: run with an embedded SQL Server container
If you prefer an isolated database for testing, enable the `localdb` profile:
```bash
docker compose --profile localdb up
```
This starts both the SQL Server and the API containers. The default SA password for the local profile is `Str0ng_Passw0rd!`; override `SPRING_DATASOURCE_PASSWORD` when you start the API if you keep that password.
