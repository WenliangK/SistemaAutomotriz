-- Migracion: estados de cotizacion ampliados
-- La v1.0.0 agrego la transicion APROBADA -> CONVERTIDA al crear una OT,
-- pero la constraint de instalaciones previas no incluia CONVERTIDA ni
-- EN_DIAGNOSTICO, lo que provocaba al crear una orden de trabajo:
--   'violates check constraint "cotizacion_estado_check"'
--
-- Ejecutar SOLO si tu volumen de datos es anterior a esta fecha:
--   docker exec -it autogestion_db psql -U autogestion -d autogestion \
--     -c "$(cat database/migrations/2026-09-20_cotizacion_estados.sql)"
--
-- Las instalaciones nuevas ya no la necesitan: init.sql nace corregido.

ALTER TABLE cotizacion DROP CONSTRAINT IF EXISTS cotizacion_estado_check;

ALTER TABLE cotizacion ADD CONSTRAINT cotizacion_estado_check
    CHECK (estado IN ('PENDIENTE','EN_DIAGNOSTICO','APROBADA','RECHAZADA','CONVERTIDA'));
