FROM postgres:16-alpine
COPY init.sql /docker-entrypoint-initdb.d/01-init.sql
COPY migrations/*.sql /docker-entrypoint-initdb.d/
