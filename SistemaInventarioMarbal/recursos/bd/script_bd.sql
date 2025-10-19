-- SISTEMA DE INVENTARIO MARBAL - Esquema normalizado (3FN)
DROP DATABASE IF EXISTS marbal_inventario;
CREATE DATABASE marbal_inventario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE marbal_inventario;

-- Catálogos
CREATE TABLE rol (
  id_rol TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(30) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE usuario (
  id_usuario INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(30) NOT NULL UNIQUE,
  email VARCHAR(120) NOT NULL UNIQUE,
  password_hash CHAR(64) NOT NULL, -- SHA-256 hex
  id_rol TINYINT UNSIGNED NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
) ENGINE=InnoDB;

CREATE TABLE proveedor (
  id_proveedor INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  ruc CHAR(11) NOT NULL UNIQUE,
  razon_social VARCHAR(150) NOT NULL,
  telefono VARCHAR(20),
  email VARCHAR(120),
  direccion VARCHAR(200),
  estado TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE cliente (
  id_cliente INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  tipo ENUM('NATURAL','JURIDICA') NOT NULL,
  doc_identidad VARCHAR(15) NOT NULL,
  nombre_razon VARCHAR(150) NOT NULL,
  telefono VARCHAR(20),
  email VARCHAR(120),
  direccion VARCHAR(200),
  UNIQUE KEY uk_cliente_doc (doc_identidad)
) ENGINE=InnoDB;

CREATE TABLE categoria (
  id_categoria SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(80) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE producto (
  id_producto INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  codigo VARCHAR(40) NOT NULL UNIQUE,
  nombre VARCHAR(120) NOT NULL,
  id_categoria SMALLINT UNSIGNED,
  tipo ENUM('CONTRATADO','ADICIONAL') NOT NULL,
  unidad VARCHAR(20) NOT NULL DEFAULT 'UND',
  stock INT NOT NULL DEFAULT 0,
  stock_minimo INT NOT NULL DEFAULT 5,
  precio_costo DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  precio_venta DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  id_proveedor INT UNSIGNED,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),
  FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor),
  CHECK (stock >= 0),
  CHECK (stock_minimo >= 0),
  CHECK (precio_costo >= 0),
  CHECK (precio_venta >= 0)
) ENGINE=InnoDB;

-- Ventas
CREATE TABLE venta (
  id_venta INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  id_cliente INT UNSIGNED,
  id_usuario INT UNSIGNED NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  estado ENUM('REGISTRADA','ANULADA') NOT NULL DEFAULT 'REGISTRADA',
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
  FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
  INDEX idx_fecha (fecha)
) ENGINE=InnoDB;

CREATE TABLE venta_detalle (
  id_venta_detalle BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  id_venta INT UNSIGNED NOT NULL,
  id_producto INT UNSIGNED NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  FOREIGN KEY (id_venta) REFERENCES venta(id_venta) ON DELETE CASCADE,
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  CHECK (cantidad > 0),
  CHECK (precio_unitario >= 0),
  CHECK (subtotal >= 0),
  UNIQUE KEY uk_vd (id_venta, id_producto)
) ENGINE=InnoDB;

-- Órdenes de compra
CREATE TABLE orden_compra (
  id_oc INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  id_proveedor INT UNSIGNED NOT NULL,
  id_usuario INT UNSIGNED NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado ENUM('PENDIENTE','APROBADA','RECIBIDA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
  total DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor),
  FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
  INDEX idx_oc_fecha (fecha)
) ENGINE=InnoDB;

CREATE TABLE orden_compra_detalle (
  id_oc_detalle BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  id_oc INT UNSIGNED NOT NULL,
  id_producto INT UNSIGNED NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  FOREIGN KEY (id_oc) REFERENCES orden_compra(id_oc) ON DELETE CASCADE,
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  CHECK (cantidad > 0),
  CHECK (precio_unitario >= 0),
  CHECK (subtotal >= 0),
  UNIQUE KEY uk_ocd (id_oc, id_producto)
) ENGINE=InnoDB;

-- Movimientos de inventario (auditoría)
CREATE TABLE movimiento_inventario (
  id_mov BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  id_producto INT UNSIGNED NOT NULL,
  tipo ENUM('ENTRADA','SALIDA','AJUSTE') NOT NULL,
  cantidad INT NOT NULL,
  motivo VARCHAR(200),
  ref_tipo ENUM('VENTA','OC','AJUSTE') NULL,
  ref_id INT UNSIGNED NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
  CHECK (cantidad > 0),
  INDEX idx_mov_prod_fecha (id_producto, fecha)
) ENGINE=InnoDB;

-- Vistas para reportes/alertas
CREATE OR REPLACE VIEW vw_productos_bajo_stock AS
SELECT p.id_producto, p.codigo, p.nombre, p.stock, p.stock_minimo, p.tipo,
       pr.razon_social AS proveedor
FROM producto p
LEFT JOIN proveedor pr ON pr.id_proveedor = p.id_proveedor
WHERE p.activo = 1 AND (p.stock < 5 OR p.stock <= p.stock_minimo);

CREATE OR REPLACE VIEW vw_ventas_resumen_diario AS
SELECT DATE(v.fecha) AS fecha, COUNT(*) AS cant_ventas, SUM(v.total) AS total_ventas
FROM venta v
WHERE v.estado = 'REGISTRADA'
GROUP BY DATE(v.fecha)
ORDER BY fecha DESC;

CREATE OR REPLACE VIEW vw_rotacion_producto AS
SELECT p.id_producto, p.codigo, p.nombre,
       COALESCE(SUM(vd.cantidad),0) AS cantidad_vendida,
       COALESCE(SUM(vd.subtotal),0) AS monto_vendido
FROM producto p
LEFT JOIN venta_detalle vd ON vd.id_producto = p.id_producto
LEFT JOIN venta v ON v.id_venta = vd.id_venta AND v.estado = 'REGISTRADA'
GROUP BY p.id_producto, p.codigo, p.nombre
ORDER BY cantidad_vendida DESC;

-- Datos mínimos
INSERT INTO rol (nombre) VALUES ('ADMINISTRADOR'), ('OPERARIO');
INSERT INTO usuario (username, email, password_hash, id_rol, activo)
VALUES ('admin','admin@marbal.com', REPEAT('0',64), 1, 1);
INSERT INTO categoria (nombre) VALUES ('GENERAL');
INSERT INTO proveedor (ruc, razon_social) VALUES ('00000000000', 'Proveedor Genérico');

-- Índices extra
CREATE INDEX idx_producto_codigo ON producto(codigo);
CREATE INDEX idx_venta_cliente ON venta(id_cliente);
CREATE INDEX idx_oc_proveedor ON orden_compra(id_proveedor);

SELECT id_usuario, username, password_hash FROM usuario;

DESCRIBE usuario;

USE marbal_inventario;

-- 1) Crear rol ADMIN si no existe
INSERT INTO rol (nombre)
SELECT 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'ADMIN');

-- 2) Tomar id del rol ADMIN
SELECT @rol_admin := id_rol FROM rol WHERE nombre = 'ADMIN';

-- 3) Si existe admin, actualizarlo; si no, crearlo
UPDATE usuario
SET password_hash = SHA2('admin', 256), id_rol = @rol_admin, activo = 1
WHERE username = 'admin';

INSERT INTO usuario (username, password_hash, id_rol, activo)
SELECT 'admin', SHA2('admin', 256), @rol_admin, 1
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE username = 'admin');

-- 4) Verifica
SELECT u.username, LEFT(u.password_hash, 10) AS hash_prefijo, LENGTH(u.password_hash) AS hash_len,
       u.activo, r.nombre AS rol
FROM usuario u
JOIN rol r ON u.id_rol = r.id_rol
WHERE u.username = 'admin';
