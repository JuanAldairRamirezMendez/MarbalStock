-- Migration script for PostgreSQL (clean, ready to run in pgAdmin)
-- Path: recursos/bd/migration_postgres.sql
-- WARNING: review credentials and run in a safe environment.

-- 1) Create role (if needed) and grant privileges
DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'marbal') THEN
       CREATE ROLE marbal LOGIN PASSWORD 'TuPassSegura123';
   ELSE
       ALTER ROLE marbal WITH PASSWORD 'TuPassSegura123';
   END IF;
END$$;

-- 2) Ensure database exists (comment out if you create DB manually)
-- 2) Ensure database exists (CREATE DATABASE cannot be run inside a DO/transaction block)
-- Note: the CREATE DATABASE statement must be executed outside of any transaction
-- (for example, directly in pgAdmin or via psql). The script intentionally does
-- not run CREATE DATABASE inside a DO block because that would fail.
-- If you want to create the database from the command line, run one of these
-- commands as a superuser (adjust host/user/path as needed):
-- PowerShell (example, ajusta la ruta a tu psql):
-- & 'C:\Program Files\PostgreSQL\14\bin\psql.exe' -U postgres -h localhost -c "CREATE DATABASE inventario_marbal OWNER marbal;"
-- psql (unix-like):
-- psql -U postgres -h localhost -c "CREATE DATABASE inventario_marbal OWNER marbal;"
-- Or create the database from pgAdmin: right-click 'Databases' -> Create -> Database,
-- set Name = inventario_marbal and Owner = marbal.
-- If you prefer to run the creation from this file, copy the following line and
-- execute it separately (outside psql transaction):
-- CREATE DATABASE inventario_marbal OWNER marbal;

-- 3) Switch search_path or create schema if desired
-- SET search_path = public;

-- 4) Enable extensions if needed (pgcrypto used previously for hashing)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 5) Core tables (only create if not exists to avoid accidental drops)

-- usuarios (minimal columns; adapt if your schema differs)
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(255),
    nombre VARCHAR(150),
    rol VARCHAR(40) DEFAULT 'OPERARIO',
    activo BOOLEAN DEFAULT TRUE,
    creado TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

-- productos (simplified snapshot - keep your existing table if present)
-- NOTE: tests expect table name `productos` and numeric `stock`/`precio` types
CREATE TABLE IF NOT EXISTS productos (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(64) UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(80),
    unidad VARCHAR(40),
    stock NUMERIC(12,2) DEFAULT 0.0,
    consumo_mensual NUMERIC(12,2) DEFAULT 0.0,
    precio NUMERIC(12,2) DEFAULT 0.0,
    creado TIMESTAMP DEFAULT now()
);

-- movimiento_inventario: añade cliente_id si no existe
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name='movimiento_inventario' AND column_name='cliente_id'
    ) THEN
        ALTER TABLE movimiento_inventario
        ADD COLUMN cliente_id INTEGER;
    END IF;
END$$;

-- Tabla para almacenar la asignación anual por cliente y producto
CREATE TABLE IF NOT EXISTS producto_asignacion (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER NOT NULL,
    producto_id INTEGER NOT NULL,
    asignacion_anual NUMERIC(12,3) NOT NULL CHECK (asignacion_anual >= 0),
    creado TIMESTAMP DEFAULT now(),
    UNIQUE (cliente_id, producto_id)
);

-- Tabla para alertas de reparto y excepciones
CREATE TABLE IF NOT EXISTS alertas_reparto (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER,
    producto_id INTEGER,
    tipo VARCHAR(40) NOT NULL, -- e.g., ALERTA, EXCEPCION
    mensaje TEXT,
    creado TIMESTAMP DEFAULT now(),
    creado_por VARCHAR(150),
    metadata JSONB
);

-- Indexes to speed queries by cliente/producto
CREATE INDEX IF NOT EXISTS idx_producto_asignacion_cliente_producto ON producto_asignacion(cliente_id, producto_id);
CREATE INDEX IF NOT EXISTS idx_alertas_reparto_cliente_producto ON alertas_reparto(cliente_id, producto_id);

-- (Optional) Seed sample data for testing
-- Inserta un cliente y producto de ejemplo si no existen (ajusta nombres/campos según tu BD)
INSERT INTO productos (codigo, nombre, tipo, unidad, stock)
SELECT 'P-1000', 'Producto Ejemplo', 'OTROS', 'UNIDAD', 100
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE codigo='P-1000');

-- Insert admin user including a salt (generated) if not present
INSERT INTO usuarios (username, password_hash, salt, nombre, rol, activo)
SELECT 'admin', encode(digest('adminpass'||s, 'sha256'),'hex'), s, 'Administrador', 'ADMIN', true
FROM (
    SELECT encode(gen_random_bytes(12), 'hex') AS s
) AS _salt
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE username='admin');

-- Inserta una asignación de ejemplo: cliente_id=1, producto_id=1, asignacion_anual=1200
INSERT INTO producto_asignacion (cliente_id, producto_id, asignacion_anual)
SELECT 1, (SELECT id FROM productos WHERE codigo='P-1000' LIMIT 1), 1200
WHERE NOT EXISTS (SELECT 1 FROM producto_asignacion WHERE cliente_id=1 AND producto_id=(SELECT id FROM productos WHERE codigo='P-1000' LIMIT 1));

-- 6) Grants: dar permisos al role marbal sobre tablas principales
GRANT SELECT, INSERT, UPDATE, DELETE ON productos TO marbal;
GRANT SELECT, INSERT, UPDATE, DELETE ON movimiento_inventario TO marbal;
GRANT SELECT, INSERT, UPDATE, DELETE ON producto_asignacion TO marbal;
GRANT SELECT, INSERT, UPDATE, DELETE ON alertas_reparto TO marbal;
GRANT SELECT, INSERT, UPDATE, DELETE ON usuarios TO marbal;

-- Conceder permisos sobre secuencias para evitar errores al insertar en columnas SERIAL
-- (esto evita el error de permiso sobre la secuencia que genera el SERIAL/SEQUENCE al insertar)
-- Ejecutar en pgAdmin o psql si corresponde. Ajusta el esquema si no es `public`.
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO marbal;
-- Si prefieres dar permisos sólo a la secuencia concreta para `alertas_reparto`:
-- GRANT USAGE, SELECT ON SEQUENCE alertas_reparto_id_seq TO marbal;

-- Fin del script. Recomendación: revisar manualmente antes de ejecutar en producción.

-- Si la columna creado_por no existía (script ejecutado anteriormente), añadirla para compatibilidad con el controlador
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name='alertas_reparto' AND column_name='creado_por'
    ) THEN
        ALTER TABLE alertas_reparto ADD COLUMN creado_por VARCHAR(150);
    END IF;
END$$;
