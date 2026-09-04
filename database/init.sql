CREATE TABLE IF NOT EXISTS Usuario (
    id_usuario SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL
);

INSERT INTO Usuario (username, password, rol, nombre_completo) 
VALUES ('admin', '123456', 'Administrador', 'Propietario San Martin');