CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN','MECANICO','ALMACENERO')),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(150),
    documento VARCHAR(20)
);
CREATE TABLE vehiculo (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES cliente(id),
    placa VARCHAR(10) UNIQUE NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    anio INTEGER
);
CREATE TABLE recepcion (
    id BIGSERIAL PRIMARY KEY,
    vehiculo_id BIGINT NOT NULL REFERENCES vehiculo(id),
    fecha_ingreso TIMESTAMP NOT NULL DEFAULT now(),
    problema_reportado TEXT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
);
CREATE TABLE diagnostico (
    id BIGSERIAL PRIMARY KEY,
    recepcion_id BIGINT NOT NULL REFERENCES recepcion(id),
    mecanico_id BIGINT NOT NULL REFERENCES usuario(id),
    descripcion TEXT NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT now()
);
CREATE TABLE servicio (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    precio_base NUMERIC(10,2) NOT NULL
);
CREATE TABLE producto (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('REPUESTO','INSUMO')),
    precio_unitario NUMERIC(10,2) NOT NULL,
    stock_actual INTEGER NOT NULL DEFAULT 0,
    stock_minimo INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT chk_stock_no_negativo CHECK (stock_actual >= 0)
);
CREATE TABLE cotizacion (
    id BIGSERIAL PRIMARY KEY,
    diagnostico_id BIGINT NOT NULL REFERENCES diagnostico(id),
    total NUMERIC(10,2) NOT NULL DEFAULT 0,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
        CHECK (estado IN ('PENDIENTE','APROBADA','RECHAZADA')),
    fecha TIMESTAMP NOT NULL DEFAULT now()
);
CREATE TABLE cotizacion_servicio (
    id BIGSERIAL PRIMARY KEY,
    cotizacion_id BIGINT NOT NULL REFERENCES cotizacion(id),
    servicio_id BIGINT NOT NULL REFERENCES servicio(id),
    precio NUMERIC(10,2) NOT NULL
);

CREATE TABLE cotizacion_producto (
    id BIGSERIAL PRIMARY KEY,
    cotizacion_id BIGINT NOT NULL REFERENCES cotizacion(id),
    producto_id BIGINT NOT NULL REFERENCES producto(id),
    cantidad_estimada INTEGER NOT NULL,
    precio_unitario NUMERIC(10,2) NOT NULL
);
CREATE TABLE orden_trabajo (
    id BIGSERIAL PRIMARY KEY,
    cotizacion_id BIGINT NOT NULL REFERENCES cotizacion(id),
    mecanico_id BIGINT NOT NULL REFERENCES usuario(id),
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
        CHECK (estado IN ('PENDIENTE','EN_PROCESO','EN_PRUEBA','FINALIZADA','CANCELADA')),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT now(),
    fecha_fin TIMESTAMP
);
CREATE TABLE ot_producto_usado (
    id BIGSERIAL PRIMARY KEY,
    orden_trabajo_id BIGINT NOT NULL REFERENCES orden_trabajo(id),
    producto_id BIGINT NOT NULL REFERENCES producto(id),
    cantidad_usada INTEGER NOT NULL CHECK (cantidad_usada > 0)
);
CREATE TABLE inventario_movimiento (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL REFERENCES producto(id),
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ENTRADA','CONSUMO','AJUSTE')),
    cantidad INTEGER NOT NULL,
    motivo VARCHAR(255),
    fecha TIMESTAMP NOT NULL DEFAULT now()
);
CREATE TABLE pago_entrega (
    id BIGSERIAL PRIMARY KEY,
    orden_trabajo_id BIGINT NOT NULL UNIQUE REFERENCES orden_trabajo(id),
    monto NUMERIC(10,2) NOT NULL,
    fecha_pago TIMESTAMP,
    fecha_entrega TIMESTAMP
);
CREATE INDEX idx_vehiculo_cliente ON vehiculo(cliente_id);
CREATE INDEX idx_recepcion_vehiculo ON recepcion(vehiculo_id);
CREATE INDEX idx_recepcion_estado ON recepcion(estado);
CREATE INDEX idx_diagnostico_recepcion ON diagnostico(recepcion_id);
CREATE INDEX idx_diagnostico_mecanico ON diagnostico(mecanico_id);
CREATE INDEX idx_cotizacion_diagnostico ON cotizacion(diagnostico_id);
CREATE INDEX idx_cotizacion_estado ON cotizacion(estado);
CREATE INDEX idx_cotizacion_servicio_cotizacion ON cotizacion_servicio(cotizacion_id);
CREATE INDEX idx_cotizacion_producto_cotizacion ON cotizacion_producto(cotizacion_id);
CREATE INDEX idx_ot_cotizacion ON orden_trabajo(cotizacion_id);
CREATE INDEX idx_ot_mecanico ON orden_trabajo(mecanico_id);
CREATE INDEX idx_ot_estado ON orden_trabajo(estado);
CREATE INDEX idx_ot_producto_usado_ot ON ot_producto_usado(orden_trabajo_id);
CREATE INDEX idx_ot_producto_usado_producto ON ot_producto_usado(producto_id);
CREATE INDEX idx_inventario_movimiento_producto ON inventario_movimiento(producto_id);
CREATE INDEX idx_inventario_movimiento_tipo ON inventario_movimiento(tipo);
CREATE INDEX idx_pago_entrega_ot ON pago_entrega(orden_trabajo_id);

-- Seed data is handled by DataInitializer.java (CommandLineRunner)
-- which correctly hashes passwords with BCrypt on first app start.
-- Do NOT add INSERT statements here for users/data.

CREATE OR REPLACE VIEW v_alertas_stock AS
SELECT
    p.id,
    p.nombre,
    p.tipo,
    p.stock_actual,
    p.stock_minimo,
    (p.stock_minimo - p.stock_actual) AS cantidad_faltante
FROM producto p
WHERE p.stock_actual < p.stock_minimo;
