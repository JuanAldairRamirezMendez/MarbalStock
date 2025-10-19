package conexion;

import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try {
            ConexionBD db = new ConexionBD();
            Connection c = db.abrirConexion();
            System.out.println("Conexion OK: " + (c != null && !c.isClosed()));
            db.cerrarConexion();
        } catch (Exception e) {
            System.err.println("Conexion FAIL: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
