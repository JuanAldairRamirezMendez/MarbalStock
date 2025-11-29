package conexion;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConexionBD - Clase de conexión a la base de datos MySQL
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
 * BASE DE DATOS: MySQL
 * PUERTO: 3306
 * ESQUEMA: marbal_inventario
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
        String[] candidatePaths = new String[] {
            "recursos/config/db.properties",
            "SistemaInventarioMarbal/recursos/config/db.properties",
            "src/main/resources/db.properties",
            "db.properties"
        };
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
                } else {
                    this.driver = "com.mysql.cj.jdbc.Driver";
                }
            }

            try {
                Class.forName(this.driver);
            } catch (ClassNotFoundException e) {
                // driver no disponible en CLASSPATH, seguirá DriverManager si el driver está instalado
                System.err.println("Advertencia: driver JDBC no encontrado: " + this.driver);
            }

        }
        if (!loaded) {
            System.err.println("No se pudo leer recursos/config/db.properties. Usando valores por defecto.");
            // valores por defecto razonables
            this.url = "jdbc:mysql://localhost:3306/inventario_marbal?useSSL=false&serverTimezone=UTC";
            this.usuario = "root";
            this.password = "";
            this.driver = "com.mysql.cj.jdbc.Driver";
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