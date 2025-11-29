-- PostgreSQL script de creación y datos para la base de datos inventario_marbal
-- Generado: 29-11-2025

-- Conectar como superusuario y crear la base de datos
-- Desde shell: psql -U postgres -f script_bd.sql

-- Crear la base de datos si no existe
DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'inventario_marbal') THEN
       PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE inventario_marbal');
   END IF;
EXCEPTION WHEN undefined_function THEN
   -- dblink puede no estar instalado; crear la DB directamente si se ejecuta dentro de postgres
END$$;

-- Si se ejecuta conectado a inventario_marbal, continuar. De lo contrario, el usuario puede ejecutar
-- los bloques siguientes conectándose a la base de datos correcta.

-- Habilitar extensión pgcrypto para funciones digest
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Tablas
CREATE TABLE IF NOT EXISTS productos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio NUMERIC(10,2) NOT NULL,
    stock NUMERIC(10,2) NOT NULL DEFAULT 0,
    unidad VARCHAR(20) DEFAULT 'unidad',
    tipo VARCHAR(20) DEFAULT 'ADICIONAL', -- CONTRATADO o ADICIONAL
    consumo_mensual INT DEFAULT 0,
    alerta_threshold INT DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT chk_tipo_producto CHECK (tipo IN ('CONTRATADO','ADICIONAL'))
);

CREATE TABLE IF NOT EXISTS proveedores (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contacto VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(128) NOT NULL,
    salt VARCHAR(64),
    rol VARCHAR(50) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS clientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS ventas (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    total NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS ordenes_compra (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    proveedor_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id)
);

CREATE TABLE IF NOT EXISTS reportes (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    descripcion TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS movimiento_inventario (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    cantidad NUMERIC(10,2) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255),
    fecha TIMESTAMP NOT NULL DEFAULT now(),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

-- Usuario de base de datos (rol)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'marbal') THEN
         CREATE ROLE marbal LOGIN PASSWORD 'TuPassSegura123';
    ELSE
         -- Si el rol ya existe, asegurar que la contraseña coincida con la esperada
         EXECUTE format('ALTER ROLE %I WITH PASSWORD %L', 'marbal', 'TuPassSegura123');
    END IF;
EXCEPTION WHEN OTHERS THEN
    RAISE NOTICE 'No se pudo crear/alterar rol marbal: %', SQLERRM;
END$$;

-- Dar permisos sobre la base y esquemas (ejecutar como superusuario)
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO marbal;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO marbal;
GRANT ALL PRIVILEGES ON SCHEMA public TO marbal;

-- Datos precargados
INSERT INTO proveedores (nombre, contacto) VALUES
  ('Proveedor A', 'contactoA@example.com')
ON CONFLICT DO NOTHING;

INSERT INTO proveedores (nombre, contacto) VALUES
  ('Proveedor B', 'contactoB@example.com')
ON CONFLICT DO NOTHING;

INSERT INTO productos (nombre, precio, stock, unidad, tipo, consumo_mensual, alerta_threshold)
VALUES
  ('Harina', 10.50, 100.00, 'kg', 'CONTRATADO', 300, 1),
  ('Azúcar', 4.20, 50.00, 'kg', 'CONTRATADO', 150, 1),
  ('Lápiz', 0.20, 500.00, 'unidad', 'ADICIONAL', 100, 2)
ON CONFLICT DO NOTHING;

-- Insertar usuarios con hash SHA-256 usando pgcrypto
INSERT INTO usuarios (nombre, username, password_hash, rol, activo)
VALUES
  ('Administrador','admin', encode(digest('admin','sha256'),'hex'),'ADMINISTRADOR', true),
  ('Operario','operario', encode(digest('op123','sha256'),'hex'),'OPERARIO', true)
ON CONFLICT (username) DO NOTHING;

-- Funciones y procedimientos (PL/pgSQL)
CREATE OR REPLACE FUNCTION consumo_diario_max(p_producto_id BIGINT) RETURNS NUMERIC AS $$
DECLARE
    cm INT := 0;
BEGIN
    SELECT consumo_mensual INTO cm FROM productos WHERE id = p_producto_id;
    IF cm IS NULL OR cm = 0 THEN
        RETURN 0.00;
    END IF;
    RETURN ROUND(cm::numeric / 30.0, 2);
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION registrar_consumo(p_producto_id BIGINT, p_cantidad NUMERIC, p_descripcion VARCHAR)
RETURNS void AS $$
DECLARE
    v_stock NUMERIC;
    v_threshold INT;
BEGIN
    SELECT stock, alerta_threshold INTO v_stock, v_threshold FROM productos WHERE id = p_producto_id FOR UPDATE;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Producto no encontrado';
    END IF;
    IF v_stock - p_cantidad < 0 THEN
        RAISE EXCEPTION 'Stock insuficiente';
    END IF;

    INSERT INTO movimiento_inventario (producto_id, cantidad, tipo, descripcion)
    VALUES (p_producto_id, p_cantidad, 'SALIDA', p_descripcion);

    UPDATE productos SET stock = stock - p_cantidad WHERE id = p_producto_id;

    SELECT stock, alerta_threshold INTO v_stock, v_threshold FROM productos WHERE id = p_producto_id;
    IF v_stock <= v_threshold THEN
        INSERT INTO reportes (fecha, descripcion) VALUES (now(), format('ALERTA: producto %s stock bajo: %s', p_producto_id, v_stock));
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION revisar_alertas() RETURNS void AS $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN SELECT id, nombre, stock FROM productos WHERE stock <= alerta_threshold LOOP
        INSERT INTO reportes (fecha, descripcion) VALUES (now(), format('ALERTA: %s (id=%s) stock=%s', rec.nombre, rec.id, rec.stock));
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Vista para ver productos en alerta
DROP VIEW IF EXISTS v_productos_alerta;
CREATE VIEW v_productos_alerta AS
SELECT id, nombre, stock, alerta_threshold FROM productos WHERE stock <= alerta_threshold;

-- Nota: para ejecutar este script completo se requiere conectarse como superusuario para crear la base y el rol,
-- o ejecutar las secciones de creación de base/rol por separado.
