package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
    private String url = "jdbc:mysql://localhost:3306/tu_base_de_datos";
    private String usuario = "tu_usuario";
    private String contraseña = "tu_contraseña";
    private Connection conexion;

    public Connection abrirConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(url, usuario, contraseña);
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