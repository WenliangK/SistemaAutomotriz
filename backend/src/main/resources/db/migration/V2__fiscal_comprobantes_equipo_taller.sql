-- V2: todo lo agregado en Fases 1-6 (clientes fiscales, comprobantes,
-- equipo, extras de recepcion, inventario valorizado, proveedores, gastos).
-- IDEMPOTENTE: cada sentencia puede re-ejecutarse sin error, porque en BD
-- viejas algunas columnas ya podrian existir (parches manuales anteriores).
-- Corre igual en PostgreSQL 16 y en H2 en modo PostgreSQL.
-- NOTA: no incluye el indice parcial de comprobante vigente ni la FK
-- pago_entrega->comprobante (ninguno de los dos es idempotente en ambos
-- motores); esas dos reglas las garantiza ComprobanteService + validate.

-- 1. Cliente fiscal (un ALTER por columna: H2 no acepta lista con comas)
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS tipo_documento VARCHAR(10) NOT NULL DEFAULT 'DNI';
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS razon_social   VARCHAR(200);
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS direccion      VARCHAR(250);
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS activo         BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS creado_en      TIMESTAMP NOT NULL DEFAULT now();
-- Solo dígitos exactos (TRANSLATE funciona igual en PostgreSQL y en H2)
UPDATE cliente SET tipo_documento = 'RUC'
 WHERE documento IS NOT NULL AND LENGTH(documento) = 11
   AND TRANSLATE(documento, '0123456789', '') = '';
ALTER TABLE cliente DROP CONSTRAINT IF EXISTS chk_cliente_tipo_doc;
ALTER TABLE cliente ADD CONSTRAINT chk_cliente_tipo_doc
  CHECK (tipo_documento IN ('DNI','RUC','CE','PASAPORTE'));
CREATE UNIQUE INDEX IF NOT EXISTS uq_cliente_documento ON cliente(tipo_documento, documento);
CREATE INDEX IF NOT EXISTS idx_cliente_nombre ON cliente(nombre);

-- Proveedores (va antes de ser referenciado por movimientos y gastos)
CREATE TABLE IF NOT EXISTS proveedor (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    ruc VARCHAR(11),
    telefono VARCHAR(30),
    email VARCHAR(150),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT now()
);

-- 2. Series y comprobantes (los numeros los reserva el backend con bloqueo;
--    jamas se reutilizan; un anulado no bloquea re-emitir para la OT)
CREATE TABLE IF NOT EXISTS serie_comprobante (
    serie VARCHAR(4) PRIMARY KEY,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('BOLETA','FACTURA')),
    ultimo_numero INTEGER NOT NULL DEFAULT 0
);
CREATE TABLE IF NOT EXISTS comprobante (
    id BIGSERIAL PRIMARY KEY,
    orden_trabajo_id BIGINT NOT NULL REFERENCES orden_trabajo(id),
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('BOLETA','FACTURA')),
    codigo_sunat VARCHAR(2) NOT NULL,
    serie VARCHAR(4) NOT NULL,
    numero INTEGER NOT NULL,
    fecha_emision TIMESTAMP NOT NULL DEFAULT now(),
    moneda VARCHAR(3) NOT NULL DEFAULT 'PEN',
    forma_pago VARCHAR(10) NOT NULL DEFAULT 'CONTADO' CHECK (forma_pago IN ('CONTADO','CREDITO')),
    metodo_pago VARCHAR(20) NOT NULL,
    emisor_ruc VARCHAR(11) NOT NULL,
    emisor_razon_social VARCHAR(200) NOT NULL,
    emisor_nombre_comercial VARCHAR(200),
    emisor_direccion VARCHAR(250) NOT NULL,
    emisor_ubigeo VARCHAR(6),
    emisor_telefono VARCHAR(30),
    emisor_email VARCHAR(150),
    cliente_id BIGINT REFERENCES cliente(id),
    cliente_tipo_doc VARCHAR(10) NOT NULL,
    cliente_num_doc VARCHAR(12) NOT NULL,
    cliente_nombre VARCHAR(200) NOT NULL,
    cliente_direccion VARCHAR(250),
    total_gravado NUMERIC(12,2) NOT NULL,
    total_exonerado NUMERIC(12,2) NOT NULL DEFAULT 0,
    total_inafecto NUMERIC(12,2) NOT NULL DEFAULT 0,
    total_descuento NUMERIC(12,2) NOT NULL DEFAULT 0,
    igv NUMERIC(12,2) NOT NULL,
    total NUMERIC(12,2) NOT NULL,
    total_letras VARCHAR(250) NOT NULL,
    observacion VARCHAR(500),
    estado VARCHAR(10) NOT NULL DEFAULT 'EMITIDO' CHECK (estado IN ('EMITIDO','ANULADO')),
    motivo_anulacion VARCHAR(250),
    emitido_por BIGINT NOT NULL REFERENCES usuario(id),
    UNIQUE (serie, numero)
);
CREATE TABLE IF NOT EXISTS comprobante_detalle (
    id BIGSERIAL PRIMARY KEY,
    comprobante_id BIGINT NOT NULL REFERENCES comprobante(id) ON DELETE CASCADE,
    item INTEGER NOT NULL,
    codigo VARCHAR(30),
    descripcion VARCHAR(250) NOT NULL,
    unidad_medida VARCHAR(4) NOT NULL,
    cantidad NUMERIC(12,3) NOT NULL,
    valor_unitario NUMERIC(12,4) NOT NULL,
    precio_unitario NUMERIC(12,4) NOT NULL,
    tipo_afectacion VARCHAR(2) NOT NULL DEFAULT '10',
    valor_venta NUMERIC(12,2) NOT NULL,
    igv NUMERIC(12,2) NOT NULL,
    importe_total NUMERIC(12,2) NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_comprobante_fecha ON comprobante(fecha_emision);
CREATE INDEX IF NOT EXISTS idx_comprobante_cliente ON comprobante(cliente_id);

-- 3. Pago apunta al comprobante (el monto real sale del comprobante)
ALTER TABLE pago_entrega ADD COLUMN IF NOT EXISTS comprobante_id BIGINT;

-- 4. Equipo con nombre real
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS nombres        VARCHAR(100);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS apellidos      VARCHAR(100);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS tipo_documento VARCHAR(10) NOT NULL DEFAULT 'DNI';
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS documento      VARCHAR(12);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS telefono       VARCHAR(20);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS especialidad   VARCHAR(30);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS fecha_ingreso  DATE;
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS creado_en      TIMESTAMP NOT NULL DEFAULT now();
UPDATE usuario SET nombres = nombre WHERE nombres IS NULL;
ALTER TABLE usuario DROP CONSTRAINT IF EXISTS chk_usuario_especialidad;
ALTER TABLE usuario ADD CONSTRAINT chk_usuario_especialidad CHECK (
  especialidad IS NULL OR especialidad IN ('MECANICA_GENERAL','MOTOR','FRENOS_SUSPENSION',
    'ELECTRICIDAD','DIAGNOSTICO_COMPUTARIZADO','ALINEACION_BALANCEO'));
CREATE UNIQUE INDEX IF NOT EXISTS uq_usuario_documento ON usuario(tipo_documento, documento);

-- 5. Extras de recepcion (wizard de 4 pasos)
ALTER TABLE recepcion ADD COLUMN IF NOT EXISTS nivel_combustible VARCHAR(20);
ALTER TABLE recepcion ADD COLUMN IF NOT EXISTS danos_previos     VARCHAR(500);
ALTER TABLE recepcion ADD COLUMN IF NOT EXISTS accesorios        VARCHAR(500);
ALTER TABLE recepcion ADD COLUMN IF NOT EXISTS kilometraje       INTEGER;

-- 6. Estados de cotizacion completos (instalaciones sin el parche 2026-09-20)
ALTER TABLE cotizacion DROP CONSTRAINT IF EXISTS cotizacion_estado_check;
ALTER TABLE cotizacion ADD CONSTRAINT cotizacion_estado_check
  CHECK (estado IN ('PENDIENTE','EN_DIAGNOSTICO','APROBADA','RECHAZADA','CONVERTIDA'));

-- 7. Producto valorizado
ALTER TABLE producto ADD COLUMN IF NOT EXISTS costo_unitario NUMERIC(10,2) NOT NULL DEFAULT 0;
ALTER TABLE producto ADD COLUMN IF NOT EXISTS unidad_medida  VARCHAR(4) NOT NULL DEFAULT 'NIU';
ALTER TABLE producto ADD COLUMN IF NOT EXISTS activo         BOOLEAN NOT NULL DEFAULT TRUE;

-- 8. Movimientos con foto completa (el AJUSTE viejo se vuelve positivo).
-- OJO el orden: primero se suelta el CHECK viejo, porque el UPDATE a
-- AJUSTE_POSITIVO violaria la lista antigua (ENTRADA,CONSUMO,AJUSTE).
ALTER TABLE inventario_movimiento DROP CONSTRAINT IF EXISTS inventario_movimiento_tipo_check;
UPDATE inventario_movimiento SET tipo = 'AJUSTE_POSITIVO' WHERE tipo = 'AJUSTE';
ALTER TABLE inventario_movimiento ADD COLUMN IF NOT EXISTS costo_unitario   NUMERIC(10,2);
ALTER TABLE inventario_movimiento ADD COLUMN IF NOT EXISTS proveedor_id     BIGINT REFERENCES proveedor(id);
ALTER TABLE inventario_movimiento ADD COLUMN IF NOT EXISTS orden_trabajo_id BIGINT REFERENCES orden_trabajo(id);
ALTER TABLE inventario_movimiento ADD COLUMN IF NOT EXISTS usuario_id       BIGINT REFERENCES usuario(id);
ALTER TABLE inventario_movimiento ADD COLUMN IF NOT EXISTS documento_ref    VARCHAR(60);
ALTER TABLE inventario_movimiento ADD COLUMN IF NOT EXISTS stock_antes      INTEGER;
ALTER TABLE inventario_movimiento ADD COLUMN IF NOT EXISTS stock_despues    INTEGER;
ALTER TABLE inventario_movimiento ADD CONSTRAINT inventario_movimiento_tipo_check
  CHECK (tipo IN ('ENTRADA','CONSUMO','AJUSTE_POSITIVO','AJUSTE_NEGATIVO','MERMA'));

-- 9. Gastos operativos
CREATE TABLE IF NOT EXISTS gasto (
    id BIGSERIAL PRIMARY KEY,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    categoria VARCHAR(30) NOT NULL CHECK (categoria IN
      ('ALQUILER','SERVICIOS','SUELDOS','HERRAMIENTAS','LIMPIEZA','TRANSPORTE','OTROS')),
    descripcion VARCHAR(250) NOT NULL,
    monto NUMERIC(12,2) NOT NULL CHECK (monto > 0),
    proveedor_id BIGINT REFERENCES proveedor(id),
    documento_ref VARCHAR(60),
    registrado_por BIGINT NOT NULL REFERENCES usuario(id),
    creado_en TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_mov_fecha ON inventario_movimiento(fecha);
CREATE INDEX IF NOT EXISTS idx_mov_ot ON inventario_movimiento(orden_trabajo_id);
CREATE INDEX IF NOT EXISTS idx_gasto_fecha ON gasto(fecha);
