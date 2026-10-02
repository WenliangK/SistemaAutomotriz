-- V3: arregla el CHECK de cotizacion.estado en H2 (y lo unifica en PostgreSQL).
--
-- Problema: V1 creó el CHECK de forma INLINE, sin nombre:
--     estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
--       CHECK (estado IN ('PENDIENTE','APROBADA','RECHAZADA'))
-- PostgreSQL lo bautiza solo (cotizacion_estado_check), así que el parche de V2
-- --DROP + ADD-- sí funcionó allí. H2 le pone un nombre automático
-- (CONSTRAINT_xxx) y ese CHECK restrictivo SIGUE VIVO: en H2/dev cualquier
-- transición de cotizacion a CONVERTIDA o EN_DIAGNOSTICO (crear una OT) falla
-- con "Check constraint violation", y por eso ReporteFlujoIT nunca pudo pasar.
--
-- Solución: recrear la columna con UN solo CHECK con nombre estable. El índice
-- y los CHECK dependientes caen con la columna, así que se redeclara al final.
-- No se toca V1/V2: cambiarlos invalidaria el checksum en instalaciones ya migradas.

ALTER TABLE cotizacion ADD COLUMN estado_tmp VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE';
UPDATE cotizacion SET estado_tmp = estado;
ALTER TABLE cotizacion DROP COLUMN estado;
-- RENAME COLUMN (no ALTER COLUMN ... RENAME TO): es la única forma que aceptan
-- a la vez PostgreSQL y H2.
ALTER TABLE cotizacion RENAME COLUMN estado_tmp TO estado;
ALTER TABLE cotizacion ALTER COLUMN estado SET DEFAULT 'PENDIENTE';

ALTER TABLE cotizacion DROP CONSTRAINT IF EXISTS cotizacion_estado_check;
ALTER TABLE cotizacion ADD CONSTRAINT cotizacion_estado_check
  CHECK (estado IN ('PENDIENTE','EN_DIAGNOSTICO','APROBADA','RECHAZADA','CONVERTIDA'));

CREATE INDEX IF NOT EXISTS idx_cotizacion_estado ON cotizacion(estado);
