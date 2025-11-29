
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
    -- Script para PostgreSQL

    -- Crear la base de datos (si se ejecuta desde un cliente con privilegios)
    -- Si la base ya existe la instrucción puede fallar; es aceptable capturar/ignorar el error al ejecutar desde la aplicación
    CREATE DATABASE inventario_marbal;

    -- Las siguientes instrucciones deben ejecutarse conectadas a la base `inventario_marbal`.

    -- Crear tabla de productos
    -- Habilitar extensión pgcrypto para funciones de hashing (si no está instalada)
    CREATE EXTENSION IF NOT EXISTS pgcrypto;

    CREATE TABLE IF NOT EXISTS productos (
        id SERIAL PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL,
        precio NUMERIC(10,2) NOT NULL,
        stock NUMERIC(10,2) NOT NULL DEFAULT 0,
        unidad VARCHAR(20) DEFAULT 'unidad',
        tipo VARCHAR(20) DEFAULT 'ADICIONAL',
        consumo_mensual INT DEFAULT 0,
        alerta_threshold INT DEFAULT 1,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT chk_tipo_producto CHECK (tipo IN ('CONTRATADO','ADICIONAL'))
    );

    -- Crear tabla de proveedores
    CREATE TABLE IF NOT EXISTS proveedores (
        id SERIAL PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL,
        contacto VARCHAR(100) NOT NULL,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    -- Script para PostgreSQL

    -- Crear la base de datos (si se ejecuta desde un cliente con privilegios)
    -- Si la base ya existe la instrucción puede fallar; es aceptable capturar/ignorar el error al ejecutar desde la aplicación
    CREATE DATABASE inventario_marbal;

    -- Las siguientes instrucciones deben ejecutarse conectadas a la base `inventario_marbal`.

    -- Crear tabla de productos
    CREATE TABLE IF NOT EXISTS productos (
        id SERIAL PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL,
        precio NUMERIC(10,2) NOT NULL,
        stock NUMERIC(10,2) NOT NULL DEFAULT 0,
        unidad VARCHAR(20) DEFAULT 'unidad',
        tipo VARCHAR(20) DEFAULT 'ADICIONAL',
        consumo_mensual INT DEFAULT 0,
        alerta_threshold INT DEFAULT 1,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT chk_tipo_producto CHECK (tipo IN ('CONTRATADO','ADICIONAL'))
    );

    -- Crear tabla de proveedores
    CREATE TABLE IF NOT EXISTS proveedores (
        id SERIAL PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL,
        contacto VARCHAR(100) NOT NULL,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

    -- Crear tabla de usuarios
    CREATE TABLE IF NOT EXISTS usuarios (
        id SERIAL PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL,
        username VARCHAR(50) NOT NULL UNIQUE,
        password_hash VARCHAR(255) NOT NULL,
        salt VARCHAR(255),
        rol VARCHAR(50) NOT NULL,
        activo SMALLINT DEFAULT 1,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

    -- Insertar usuario por defecto (password 'admin')
    -- Nota: se usa pgcrypto para calcular SHA-256 y guardarlo como hex
    INSERT INTO usuarios (nombre, username, password_hash, rol, activo)
    VALUES ('Administrador','admin', encode(digest('admin','sha256'),'hex'),'ADMINISTRADOR',1)
    ON CONFLICT (username) DO UPDATE SET nombre = EXCLUDED.nombre;

    -- Crear tabla de clientes
    CREATE TABLE IF NOT EXISTS clientes (
        id SERIAL PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL,
        direccion VARCHAR(255) NOT NULL,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

    -- Crear tabla de ventas
    CREATE TABLE IF NOT EXISTS ventas (
        id SERIAL PRIMARY KEY,
        fecha TIMESTAMP NOT NULL,
        total NUMERIC(10,2) NOT NULL,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

    -- Crear tabla de ordenes de compra
    CREATE TABLE IF NOT EXISTS ordenes_compra (
        id SERIAL PRIMARY KEY,
        fecha TIMESTAMP NOT NULL,
        proveedor_id INT,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (proveedor_id) REFERENCES proveedores(id)
    );

    -- Crear tabla de reportes
    CREATE TABLE IF NOT EXISTS reportes (
        id SERIAL PRIMARY KEY,
        fecha TIMESTAMP NOT NULL,
        descripcion TEXT NOT NULL,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

    -- Crear tabla de movimientos de inventario
    CREATE TABLE IF NOT EXISTS movimiento_inventario (
        id SERIAL PRIMARY KEY,
        producto_id INT NOT NULL,
        cliente_id INT,
        cantidad NUMERIC(10,2) NOT NULL,
        tipo VARCHAR(50) NOT NULL,
        descripcion VARCHAR(255),
        fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
        FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE SET NULL
    );

    -- Tabla que almacena la asignación anual por cliente y producto
    CREATE TABLE IF NOT EXISTS producto_asignacion (
        id SERIAL PRIMARY KEY,
        cliente_id INT NOT NULL REFERENCES clientes(id) ON DELETE CASCADE,
        producto_id INT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
        asignacion_anual NUMERIC(12,3) NOT NULL,
        unidad VARCHAR(20) DEFAULT 'unidad',
        activo BOOLEAN DEFAULT TRUE,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

    -- Tabla de alertas generadas por la lógica de reparto
    CREATE TABLE IF NOT EXISTS alertas_reparto (
        id SERIAL PRIMARY KEY,
        cliente_id INT REFERENCES clientes(id) ON DELETE SET NULL,
        producto_id INT REFERENCES productos(id) ON DELETE SET NULL,
        tipo VARCHAR(32), -- CERCANO | EXCEDIDO | EXCEPCION
        mensaje TEXT,
        creado_por VARCHAR(100),
        creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        estado VARCHAR(20) DEFAULT 'PENDIENTE'
    );

    -- Crear role/usuario de base de datos y otorgar privilegios (ejecutar con superuser)
    DO $$
    BEGIN
       IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'marbal') THEN
           CREATE ROLE marbal WITH LOGIN PASSWORD 'TuPassSegura123';
       ELSE
           -- Si ya existe, actualizar la contraseña para mantenerla sincronizada
           EXECUTE 'ALTER ROLE marbal WITH PASSWORD ''TuPassSegura123''';
       END IF;
    END$$;

    -- Otorgar permisos necesarios sobre la base y el esquema público
    GRANT CONNECT ON DATABASE inventario_marbal TO marbal;
    \c inventario_marbal
    GRANT USAGE ON SCHEMA public TO marbal;
    GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO marbal;
    GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO marbal;
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO marbal;
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO marbal;

    -- Datos precargados (proveedores, productos, usuarios)
    INSERT INTO proveedores (nombre, contacto) VALUES
      ('Proveedor A', 'contactoA@example.com'),
      ('Proveedor B', 'contactoB@example.com')
    ON CONFLICT DO NOTHING;

    INSERT INTO productos (nombre, precio, stock, unidad, tipo, consumo_mensual, alerta_threshold)
    VALUES
      ('Harina', 10.50, 100.00, 'kg', 'CONTRATADO', 300, 1),
      ('Azúcar', 4.20, 50.00, 'kg', 'CONTRATADO', 150, 1),
      ('Lápiz', 0.20, 500.00, 'unidad', 'ADICIONAL', 100, 2)
    ON CONFLICT DO NOTHING;

    -- Usuarios precargados para login (admin/admin y operario/op123)
    INSERT INTO usuarios (nombre, username, password_hash, rol, activo)
    VALUES
      ('Administrador','admin', encode(digest('admin','sha256'),'hex'),'ADMINISTRADOR',1),
      ('Operario','operario', encode(digest('op123','sha256'),'hex'),'OPERARIO',1)
    ON CONFLICT (username) DO NOTHING;

    -- Vista para ver productos en alerta
    DROP VIEW IF EXISTS v_productos_alerta;
    CREATE VIEW v_productos_alerta AS
    SELECT id, nombre, stock, alerta_threshold FROM productos WHERE stock <= alerta_threshold;
