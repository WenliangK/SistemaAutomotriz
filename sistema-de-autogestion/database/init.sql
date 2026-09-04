CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN','MECANICO','ALMACENERO')),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(150),
    documento VARCHAR(20)
);
CREATE TABLE vehiculo (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER NOT NULL REFERENCES cliente(id),
    placa VARCHAR(10) UNIQUE NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    anio INTEGER
);
CREATE TABLE recepcion (
    id SERIAL PRIMARY KEY,
    vehiculo_id INTEGER NOT NULL REFERENCES vehiculo(id),
    fecha_ingreso TIMESTAMP NOT NULL DEFAULT now(),
    problema_reportado TEXT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
);
CREATE TABLE diagnostico (
    id SERIAL PRIMARY KEY,
    recepcion_id INTEGER NOT NULL REFERENCES recepcion(id),
    mecanico_id INTEGER NOT NULL REFERENCES usuario(id),
    descripcion TEXT NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT now()
);
CREATE TABLE servicio (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    precio_base NUMERIC(10,2) NOT NULL
);
CREATE TABLE producto (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('REPUESTO','INSUMO')),
    precio_unitario NUMERIC(10,2) NOT NULL,
    stock_actual INTEGER NOT NULL DEFAULT 0,
    stock_minimo INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT chk_stock_no_negativo CHECK (stock_actual >= 0)
);
CREATE TABLE cotizacion (
    id SERIAL PRIMARY KEY,
    diagnostico_id INTEGER NOT NULL REFERENCES diagnostico(id),
    total NUMERIC(10,2) NOT NULL DEFAULT 0,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
        CHECK (estado IN ('PENDIENTE','APROBADA','RECHAZADA')),
    fecha TIMESTAMP NOT NULL DEFAULT now()
);
CREATE TABLE cotizacion_servicio (
    id SERIAL PRIMARY KEY,
    cotizacion_id INTEGER NOT NULL REFERENCES cotizacion(id),
    servicio_id INTEGER NOT NULL REFERENCES servicio(id),
    precio NUMERIC(10,2) NOT NULL
);

CREATE TABLE cotizacion_producto (
    id SERIAL PRIMARY KEY,
    cotizacion_id INTEGER NOT NULL REFERENCES cotizacion(id),
    producto_id INTEGER NOT NULL REFERENCES producto(id),
    cantidad_estimada INTEGER NOT NULL,
    precio_unitario NUMERIC(10,2) NOT NULL
);
CREATE TABLE orden_trabajo (
    id SERIAL PRIMARY KEY,
    cotizacion_id INTEGER NOT NULL REFERENCES cotizacion(id),
    mecanico_id INTEGER NOT NULL REFERENCES usuario(id),
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
        CHECK (estado IN ('PENDIENTE','EN_PROCESO','EN_PRUEBA','FINALIZADA','CANCELADA')),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT now(),
    fecha_fin TIMESTAMP
);
CREATE TABLE ot_producto_usado (
    id SERIAL PRIMARY KEY,
    orden_trabajo_id INTEGER NOT NULL REFERENCES orden_trabajo(id),
    producto_id INTEGER NOT NULL REFERENCES producto(id),
    cantidad_usada INTEGER NOT NULL CHECK (cantidad_usada > 0)
);
CREATE TABLE inventario_movimiento (
    id SERIAL PRIMARY KEY,
    producto_id INTEGER NOT NULL REFERENCES producto(id),
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ENTRADA','CONSUMO','AJUSTE')),
    cantidad INTEGER NOT NULL,
    motivo VARCHAR(255),
    fecha TIMESTAMP NOT NULL DEFAULT now()
);
CREATE TABLE pago_entrega (
    id SERIAL PRIMARY KEY,
    orden_trabajo_id INTEGER NOT NULL UNIQUE REFERENCES orden_trabajo(id),
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

INSERT INTO usuario (nombre, email, password_hash, rol) VALUES
('Admin Taller',    'admin@sanmartin.pe',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeJrzG1MKNR66oJSz6YRZhHfn7JGp0XS', 'ADMIN'),
('Mecánico Uno',    'mecanico1@sanmartin.pe', '$2a$10$N9qo8uLOickgx2ZMRZoMyeJrzG1MKNR66oJSz6YRZhHfn7JGp0XS', 'MECANICO'),
('Almacenero',      'almacen@sanmartin.pe',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeJrzG1MKNR66oJSz6YRZhHfn7JGp0XS', 'ALMACENERO');
INSERT INTO cliente (nombre, telefono, email, documento) VALUES
('Juan Pérez',      '951234567', 'juan.perez@gmail.com',   '45123698'),
('María López',     '962345678', 'maria.lopez@hotmail.com','40258741'),
('Carlos García',   '973456789', 'carlos.garcia@yahoo.com','41369852');
INSERT INTO vehiculo (cliente_id, placa, marca, modelo, anio) VALUES
(1, 'ABC-123', 'Toyota',    'Corolla',   2020),
(2, 'DEF-456', 'Hyundai',   'Accent',    2019),
(3, 'GHI-789', 'Nissan',    'Sentra',    2021);
INSERT INTO servicio (nombre, precio_base) VALUES
('Cambio de aceite',            80.00),
('Alineación y balanceo',      120.00),
('Cambio de frenos',           200.00),
('Diagnóstico computarizado',  150.00),
('Reparación de motor',        800.00),
('Cambio de correa de distribución', 350.00),
('Lavado técnico',              50.00),
('Revisión de suspensión',     100.00);
INSERT INTO producto (nombre, tipo, precio_unitario, stock_actual, stock_minimo) VALUES
('Aceite 5W-30 4L',            'INSUMO',    65.00,  50, 10),
('Filtro de aceite',           'REPUESTO',  25.00,  40,  8),
('Filtro de aire',             'REPUESTO',  35.00,  30,  8),
('Pastillas de freno delanteras','REPUESTO', 120.00, 20,  5),
('Pastillas de freno traseras', 'REPUESTO', 100.00, 15,  5),
('Disco de freno',             'REPUESTO',  180.00, 10,  3),
('Correa de distribución',     'REPUESTO',  90.00,  8,   3),
('Líquido de frenos 500ml',    'INSUMO',    30.00,  25,  5),
('Refrigerante 1L',            'INSUMO',    20.00,  30,  8),
('Bujias (juego 4)',           'REPUESTO',  45.00,  20,  5),
('Amortiguador delantero',     'REPUESTO',  250.00,  6,  2),
('Aceite de transmisión ATF',  'INSUMO',    55.00,  15,  5);
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
