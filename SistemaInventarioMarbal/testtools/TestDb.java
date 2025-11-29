package testtools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDb {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5440/inventario_marbal";
        String user = "marbal";
        String pass = "TuPassSegura123";

        System.out.println("Conectando a: " + url + " como usuario: " + user);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL no encontrado en classpath: " + e.getMessage());
            System.exit(2);
        }

        try (Connection c = DriverManager.getConnection(url, user, pass);
             Statement s = c.createStatement()) {

            System.out.println("Conexión OK");

            try (ResultSet rs = s.executeQuery("SELECT current_user")) {
                if (rs.next()) {
                    System.out.println("current_user = " + rs.getString(1));
                }
            }

            String sql = "INSERT INTO usuarios (nombre, username, password_hash, rol, activo) "
                       + "VALUES ('PruebaAuto','prueba_auto', encode(digest('prueba','sha256'),'hex'), 'OPERARIO', true) "
                       + "ON CONFLICT (username) DO NOTHING";

            int updated = s.executeUpdate(sql);
            if (updated > 0) {
                System.out.println("INSERT realizado con éxito (filas afectadas: " + updated + ")");
            } else {
                System.out.println("INSERT no insertó (ya existía username 'prueba_auto' o conflict)");
            }

        } catch (Exception ex) {
            System.err.println("Error durante conexión/ejecución: " + ex.getClass().getName() + ": " + ex.getMessage());
            ex.printStackTrace(System.err);
            System.exit(3);
        }
    }
}
