package conexion;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestConexion {
    private static final Logger LOG = Logger.getLogger(TestConexion.class.getName());
    public static void main(String[] args) {
        try {
            ConexionBD db = new ConexionBD();
            Connection c = db.abrirConexion();
            System.out.println("Conexion OK: " + (c != null && !c.isClosed()));
            db.cerrarConexion();
        } catch (Exception e) {
            System.err.println("Conexion FAIL: " + e.getMessage());
            LOG.log(Level.SEVERE, "Error en prueba de conexión", e);
            System.exit(1);
        }
    }
}
