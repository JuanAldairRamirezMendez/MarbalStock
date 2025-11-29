
-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS inventario_marbal
    DEFAULT CHARACTER SET = utf8mb4
    DEFAULT COLLATE = utf8mb4_unicode_ci;

-- Usar la base de datos
USE inventario_marbal;
-- Crear tabla de productos (mejorada)

CREATE TABLE IF NOT EXISTS productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    stock DECIMAL(10,2) NOT NULL DEFAULT 0,
    unidad VARCHAR(20) DEFAULT 'unidad',
    tipo VARCHAR(20) DEFAULT 'ADICIONAL', -- CONTRATADO o ADICIONAL
    consumo_mensual INT DEFAULT 0,
    alerta_threshold INT DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_tipo_producto CHECK (tipo IN ('CONTRATADO','ADICIONAL'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla de proveedores

CREATE TABLE IF NOT EXISTS proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contacto VARCHAR(100) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla de usuarios

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    salt VARCHAR(64),
    rol VARCHAR(50) NOT NULL,
    activo TINYINT DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Usuario por defecto (password 'admin')
INSERT INTO usuarios (nombre, username, password_hash, rol, activo)
VALUES ('Administrador','admin',SHA2('admin',256),'ADMINISTRADOR',1)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Crear tabla de clientes

CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla de ventas

CREATE TABLE IF NOT EXISTS ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla de órdenes de compra

CREATE TABLE IF NOT EXISTS ordenes_compra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    proveedor_id INT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla de reportes (si es necesario)

CREATE TABLE IF NOT EXISTS reportes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    descripcion TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla de movimientos de inventario

CREATE TABLE IF NOT EXISTS movimiento_inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- EJ: INGRESO, SALIDA, AJUSTE
    descripcion VARCHAR(255),
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================================================
-- Crear usuario de base de datos y otorgar privilegios
-- ======================================================
CREATE USER IF NOT EXISTS 'marbal'@'localhost' IDENTIFIED BY 'MarbalPass2025';
GRANT ALL PRIVILEGES ON inventario_marbal.* TO 'marbal'@'localhost';
FLUSH PRIVILEGES;

-- ======================================================
-- Datos precargados (proveedores, productos, usuarios)
-- ======================================================
INSERT IGNORE INTO proveedores (nombre, contacto) VALUES
  ('Proveedor A', 'contactoA@example.com'),
  ('Proveedor B', 'contactoB@example.com');

INSERT IGNORE INTO productos (nombre, precio, stock, unidad, tipo, consumo_mensual, alerta_threshold)
VALUES
  ('Harina', 10.50, 100.00, 'kg', 'CONTRATADO', 300, 1),
  ('Azúcar', 4.20, 50.00, 'kg', 'CONTRATADO', 150, 1),
  ('Lápiz', 0.20, 500.00, 'unidad', 'ADICIONAL', 100, 2);

-- Usuarios precargados para login (admin/admin y operario/op123)
INSERT IGNORE INTO usuarios (nombre, username, password_hash, rol, activo)
VALUES
  ('Administrador','admin',SHA2('admin',256),'ADMINISTRADOR',1),
  ('Operario','operario',SHA2('op123',256),'OPERARIO',1);

-- ======================================================
-- Funciones y procedimientos útiles
-- 1) consumo_diario_max(producto_id)
-- 2) registrar_consumo(producto_id, cantidad, descripcion)
-- 3) revisar_alertas() -> genera reporte con lista de productos en alerta
-- ======================================================

DELIMITER $$

CREATE FUNCTION consumo_diario_max(p_producto_id INT) RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE cm INT DEFAULT 0;
    SELECT consumo_mensual INTO cm FROM productos WHERE id = p_producto_id;
    IF cm IS NULL OR cm = 0 THEN
        RETURN 0.00;
    END IF;
    RETURN ROUND(cm / 30.0, 2);
END$$

CREATE PROCEDURE registrar_consumo(IN p_producto_id INT, IN p_cantidad DECIMAL(10,2), IN p_descripcion VARCHAR(255))
BEGIN
    DECLARE v_stock DECIMAL(10,2);
    DECLARE v_threshold INT;
    -- Bloquear la fila para evitar condiciones de carrera
    SELECT stock, alerta_threshold INTO v_stock, v_threshold FROM productos WHERE id = p_producto_id FOR UPDATE;
    IF v_stock IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Producto no encontrado';
    END IF;
    IF v_stock - p_cantidad < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock insuficiente';
    END IF;

    INSERT INTO movimiento_inventario (producto_id, cantidad, tipo, descripcion)
    VALUES (p_producto_id, p_cantidad, 'SALIDA', p_descripcion);

    UPDATE productos SET stock = stock - p_cantidad WHERE id = p_producto_id;

    -- Comprobar alerta
    SELECT stock, alerta_threshold INTO v_stock, v_threshold FROM productos WHERE id = p_producto_id;
    IF v_stock <= v_threshold THEN
        INSERT INTO reportes (fecha, descripcion) VALUES (NOW(), CONCAT('ALERTA: producto ', p_producto_id, ' stock bajo: ', v_stock));
    END IF;
END$$

CREATE PROCEDURE revisar_alertas()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE pid INT;
    DECLARE pnombre VARCHAR(100);
    DECLARE pstock DECIMAL(10,2);
    DECLARE cur CURSOR FOR SELECT id, nombre, stock FROM productos WHERE stock <= alerta_threshold;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO pid, pnombre, pstock;
        IF done THEN
            LEAVE read_loop;
        END IF;
        INSERT INTO reportes (fecha, descripcion) VALUES (NOW(), CONCAT('ALERTA: ', pnombre, ' (id=', pid, ') stock=', pstock));
    END LOOP;
    CLOSE cur;
END$$

DELIMITER ;

-- ======================================================
-- Vista para ver productos en alerta
-- ======================================================
DROP VIEW IF EXISTS v_productos_alerta;
CREATE VIEW v_productos_alerta AS
SELECT id, nombre, stock, alerta_threshold FROM productos WHERE stock <= alerta_threshold;
