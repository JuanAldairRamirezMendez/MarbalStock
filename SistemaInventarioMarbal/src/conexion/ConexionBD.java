package conexion;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConexionBD - Clase de conexión a la base de datos (ahora PostgreSQL por defecto)
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Juan Ramírez (U20201597)
 * ROL: Conexión a Base de Datos + Script SQL
 * 
 * RESPONSABILIDADES:
 * - Programar conexión JDBC (MySQL)
 * - Probar conexión desde clase test
 * - Documentar parámetros de conexión (usuario, contraseña, puerto)
 * 
 * DESCRIPCIÓN:
 * Esta clase implementa el patrón Singleton para gestionar la conexión
 * a la base de datos MySQL del sistema de inventario. Permite centralizar
 * toda la información en una base de datos relacional (RF09).
 * 
 * BASE DE DATOS: PostgreSQL
 * PUERTO: 5432
 * ESQUEMA: inventario_marbal
 * 
 * FECHA: Octubre 2025
 * CURSO: Sección 40833
 * DOCENTE: Bances Saavedra, David Enrique
 * 
 * @author Juan Ramírez
 * @version 1.0
 */
public class ConexionBD {
    private String url;
    private String usuario;
    private String password;
    private String driver;
    private Connection conexion;

    public ConexionBD() {
        loadConfig();
    }

    private void loadConfig() {
        Properties props = new Properties();
        // Intentar leer propiedades desde varias ubicaciones relativas comunes
        // Permitir anular el path mediante -Ddb.props=ruta
        String sysProp = System.getProperty("db.props");
        String[] candidatePaths;
        if (sysProp != null && !sysProp.isEmpty()) {
            candidatePaths = new String[] {
                sysProp,
                "recursos/config/db.properties",
                "SistemaInventarioMarbal/recursos/config/db.properties",
                "src/main/resources/db.properties",
                "db.properties"
            };
        } else {
            candidatePaths = new String[] {
                "recursos/config/db.properties",
                "SistemaInventarioMarbal/recursos/config/db.properties",
                "src/main/resources/db.properties",
                "db.properties"
            };
        }
        boolean loaded = false;
        for (String path : candidatePaths) {
            try (FileInputStream fis = new FileInputStream(path)) {
                props.load(fis);
                loaded = true;
                break;
            } catch (IOException ignored) {
                // intentar siguiente ruta
            }
        }
        if (loaded) {
            String type = props.getProperty("db.type", "mysql").trim().toLowerCase();
            this.url = props.getProperty("db.url");
            this.usuario = props.getProperty("db.user");
            this.password = props.getProperty("db.password");
            this.driver = props.getProperty("db.driver");

            if ((this.driver == null || this.driver.isEmpty())) {
                if ("postgresql".equals(type)) {
                    this.driver = "org.postgresql.Driver";
                } else if ("mysql".equals(type)) {
                    this.driver = "com.mysql.cj.jdbc.Driver";
                } else {
                    // por defecto usar PostgreSQL
                    this.driver = "org.postgresql.Driver";
                }
            }

            try {
                Class.forName(this.driver);
            } catch (ClassNotFoundException e) {
                System.err.println("Advertencia: driver JDBC no encontrado: " + this.driver);
            }

        }
        if (!loaded) {
            System.err.println("No se pudo leer recursos/config/db.properties. Usando valores por defecto (PostgreSQL).");
            // valores por defecto razonables para PostgreSQL (actualizados)
            this.url = "jdbc:postgresql://localhost:5440/inventario_marbal";
            this.usuario = "marbal";
            this.password = "TuPassSegura123";
            this.driver = "org.postgresql.Driver";
        }
    }

    public Connection abrirConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(url, usuario, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conexion;
    }

    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}