FROM postgres:16-alpine
# Sin COPY de init.sql a proposito: el esquema lo crea y versiona Flyway
# al arrancar el backend (backend/src/main/resources/db/migration/).
# El entrypoint de Postgres NO debe crear tablas: en volumenes existentes
# se saltaria igual y en volumenes nuevos chocaria con Flyway.
