# ProducerHub

A local music producer dashboard built with Java 21, Spring Boot, Thymeleaf and PostgreSQL. Organise beats, keep artist contacts, record licences and track payments.

## Features

- Beat catalogue with BPM, key, genre, status, search and optional audio upload/playback.
- Artist contacts with add, edit and delete pages.
- Licence records connecting a beat to an artist, with type, price, date and paid/unpaid status.
- Licence filters for all, paid and unpaid records.
- Dashboard with catalogue counts, recorded licence value and payments received.
- Confirmation pages prevent deleting artists or beats that still have licences.

## Run on Windows

1. Install Java 21 and PostgreSQL. Check Java in PowerShell with `java -version`.
2. Create a PostgreSQL database named `producerhub` (pgAdmin's Create > Database works). The existing configuration uses the `postgres` user and port `5432`.
3. Open PowerShell in the project folder and set the PostgreSQL password for this terminal session:

   ```powershell
   $env:DB_PASSWORD = "your_postgres_password"
   ```

4. Start the application:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

5. Open <http://localhost:8080/>. Stop the server with `Ctrl+C`.

If your PostgreSQL user, port or database name differs, adjust `src/main/resources/application.properties`. Spring's `ddl-auto=update` creates missing tables on startup. It does not create the database itself.

## How the code fits together

| Layer | Example | Job |
| --- | --- | --- |
| Browser page | `templates/licenses/list.html` | Displays data and sends form requests |
| Controller | `LicenseController` | Maps URLs to Java methods |
| Service | `LicenseService` | Validates input and applies app rules |
| Repository | `LicenseRepository` | Reads/writes PostgreSQL rows |
| Entity | `License` | Defines a persisted record |

For example, submitting **Record licence** sends a POST to `/licenses`. The controller reads the form values, the service loads the selected beat and artist, validates the price, and the repository saves the licence. The controller then redirects back to the list.

## Useful URLs

| Page | URL |
| --- | --- |
| Dashboard | `/` |
| Beats | `/beats` |
| Artists | `/artists` |
| Licences | `/licenses` |
| Paid licences | `/licenses?payment=paid` |
| Unpaid licences | `/licenses?payment=unpaid` |

## Data and uploads

PostgreSQL stores the beats, artists and licences. Uploaded audio is stored in the project-root `uploads/` directory, which Git ignores; a GitHub clone will have the code but none of your local database records or uploaded audio. Keep separate backups of both if you move computers.

The beat's `AVAILABLE`, `LICENSED` and `SOLD` status is edited manually. Recording a licence does not change it automatically, because non-exclusive licences can be sold to more than one artist. Licence revenue sums all recorded prices; payments received sums only records marked paid.

## Build and tests

With PostgreSQL running and `DB_PASSWORD` set:

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
```

This project is intended for local use. Add authentication and production configuration before exposing it publicly.
