DROP SCHEMA IF EXISTS autoscaribe_db;
CREATE SCHEMA autoscaribe_db;
USE autoscaribe_db;


-- 1. TABLAS DE SEGURIDAD Y USUARIOS 

CREATE TABLE usuario (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(30) UNIQUE NOT NULL,
  password VARCHAR(512) NOT NULL,
  nombre VARCHAR(20) NOT NULL,
  apellidos VARCHAR(30) NOT NULL,
  correo VARCHAR(75) UNIQUE NOT NULL,
  telefono VARCHAR(25),
  ruta_imagen VARCHAR(1024),
  activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE rol (
  id_rol INT AUTO_INCREMENT PRIMARY KEY,
  rol VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE usuario_rol (
  id_usuario INT NOT NULL,
  id_rol INT NOT NULL,
  PRIMARY KEY (id_usuario, id_rol),
  FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
  FOREIGN KEY (id_rol) REFERENCES rol(id_rol) ON DELETE CASCADE
);

CREATE TABLE ruta (
  id_ruta INT AUTO_INCREMENT PRIMARY KEY,
  ruta VARCHAR(255) NOT NULL,
  requiere_rol BOOLEAN NOT NULL,
  id_rol INT,
  FOREIGN KEY (id_rol) REFERENCES rol(id_rol) ON DELETE CASCADE
);


-- 2. TABLAS DE INVENTARIO Y SOLICITUDES (

CREATE TABLE vehiculo (
  id_vehiculo INT AUTO_INCREMENT PRIMARY KEY,
  marca VARCHAR(40) NOT NULL,
  modelo VARCHAR(40) NOT NULL,
  año INT DEFAULT NULL,
  categoria VARCHAR(50) DEFAULT NULL,
  descripcion VARCHAR(500) DEFAULT NULL,
  precio DECIMAL(38,2) DEFAULT NULL,
  ruta_imagen VARCHAR(1024) DEFAULT NULL
);

CREATE TABLE solicitud (
  id_solicitud INT AUTO_INCREMENT PRIMARY KEY,
  id_cliente INT NOT NULL,
  id_vendedor INT NOT NULL,
  id_vehiculo INT NOT NULL,
  mensaje VARCHAR(500) NOT NULL,
  estado VARCHAR(20) DEFAULT 'PENDIENTE',
  fecha_solicitud DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
  FOREIGN KEY (id_vendedor) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
  FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id_vehiculo) ON DELETE CASCADE
);


-- 3. INSERCIÓN DE CONFIGURACIÓN BÁSICA


-- Insertar Roles 
INSERT INTO rol (id_rol, rol) VALUES 
(1, 'ADMIN'),
(2, 'CLIENTE'),
(3, 'VENDEDOR');

-- Insertar Rutas Públicas (requiere_rol = false)
INSERT INTO ruta (ruta, requiere_rol, id_rol) VALUES 
('/', false, null),
('/catalogo/**', false, null),
('/webjars/**', false, null),
('/js/**', false, null),
('/css/**', false, null),
('/images/**', false, null),
('/login', false, null),
('/registro', false, null),
('/registro/**', false, null),
('/logout', false, null),
('/acceso_denegado', false, null);

-- Insertar Rutas Protegidas (requiere_rol = true)
INSERT INTO ruta (ruta, requiere_rol, id_rol) VALUES 
('/usuario/**', true, 1),
('/rol/**', true, 1),
('/vehiculo/**', true, 3),
('/solicitud/**', true, 3);

-- Insertar un par de vehículos de prueba
INSERT INTO vehiculo (id_vehiculo, marca, modelo, año, categoria, descripcion, precio, ruta_imagen) VALUES 
(1, 'Toyota', 'Corolla', 2024, 'Sedán', 'Toyota Corolla 2024 en excelente estado.', 18900000.00, 'https://commons.wikimedia.org/wiki/Special:FilePath/2024%20Toyota%20Corolla%20LE.jpg?width=900'),
(2, 'Hyundai', 'Tucson', 2024, 'SUV', 'Hyundai Tucson 2024 con interior amplio.', 22500000.00, 'https://commons.wikimedia.org/wiki/Special:FilePath/2025%20Hyundai%20Tucson%20au%20SIAM%202025.jpg?width=900');

select * from usuario;

UPDATE autoscaribe_db.usuario_rol 
SET id_rol = 1 
WHERE id_usuario = 1;

CREATE TABLE imagen_vehiculo (
  id_imagen INT AUTO_INCREMENT PRIMARY KEY,
  id_vehiculo INT NOT NULL,
  ruta_imagen VARCHAR(1024) NOT NULL,
  orden INT DEFAULT 0,
  FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id_vehiculo) ON DELETE CASCADE
);

INSERT INTO imagen_vehiculo (id_vehiculo, ruta_imagen, orden) VALUES
(1, 'https://commons.wikimedia.org/wiki/Special:FilePath/2024%20Toyota%20Corolla%20LE.jpg?width=900', 0),
(2, 'https://commons.wikimedia.org/wiki/Special:FilePath/2025%20Hyundai%20Tucson%20au%20SIAM%202025.jpg?width=900', 0);