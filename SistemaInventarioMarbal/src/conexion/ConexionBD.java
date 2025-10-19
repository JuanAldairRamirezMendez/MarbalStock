package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConexionBD - MySQL (XAMPP/Workbench)
 * Responsable: Juan Ramírez (U20201597)
 */
public class ConexionBD {
    private static final String URL =
        "jdbc:mysql://127.0.0.1:3306/marbal_inventario"
        + "?useSSL=false&allowPublicKeyRetrieval=true"
        + "&serverTimezone=America/Lima"
        + "&useUnicode=true&characterEncoding=utf8";
    private static final String USUARIO = "root";      // o 'marbal'
    private static final String PASSWORD = "";         // en XAMPP suele ser vacío

    private Connection conexion;

    public Connection abrirConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar a MySQL: " + e.getMessage(), e);
        }
        return conexion;
    }

    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cerrar la conexión: " + e.getMessage(), e);
        }
    }
}