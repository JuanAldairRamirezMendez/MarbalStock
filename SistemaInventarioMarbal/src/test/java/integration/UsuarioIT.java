package integration;

import conexion.ConexionBD;
import controlador.UsuarioController;
import modelo.Usuario;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.Statement;

public class UsuarioIT {
    private static final String PROPS_PATH = "recursos/config/db.test.properties";

    @BeforeAll
    public static void setup() throws Exception {
        // Ensure config directory exists
        File cfg = new File(PROPS_PATH);
        // tell ConexionBD to use the test properties file for this JVM
        System.setProperty("db.props", PROPS_PATH);
        cfg.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(cfg)) {
            fw.write("db.type=postgresql\n");
            fw.write("db.url=jdbc:postgresql://127.0.0.1:5432/marbal_test\n");
            fw.write("db.user=root\n");
            fw.write("db.password=root\n");
            fw.write("db.driver=org.postgresql.Driver\n");
        }

        // create minimal table if not exists
        ConexionBD cb = new ConexionBD();
        Connection conn = cb.abrirConexion();
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id SERIAL PRIMARY KEY, " +
                    "nombre VARCHAR(100), " +
                    "username VARCHAR(100) UNIQUE, " +
                    "password_hash VARCHAR(255), " +
                    "salt VARCHAR(255), " +
                    "rol VARCHAR(50), " +
                    "activo SMALLINT DEFAULT 1);");
        }
        cb.cerrarConexion();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        // Optionally drop table to keep DB clean
        try {
            ConexionBD cb = new ConexionBD();
            Connection conn = cb.abrirConexion();
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("DROP TABLE IF EXISTS usuarios;");
            }
            cb.cerrarConexion();
        } catch (Exception ignored) {
        }
        // remove test properties file
        try {
            Files.deleteIfExists(new File(PROPS_PATH).toPath());
        } catch (Exception ignored) {}
    }

    @Test
    public void testRegistrarYAutenticar() {
        UsuarioController uc = new UsuarioController();
        String username = "it_user_" + System.currentTimeMillis();
        boolean ok = uc.registrarUsuario(username, "IT Test", "pass1234", "OPERARIO");
        Assertions.assertTrue(ok, "Registro de usuario debe retornar true");

        Usuario u = uc.autenticar(username, "pass1234");
        Assertions.assertNotNull(u, "Autenticación debe devolver Usuario");
        Assertions.assertEquals("IT Test", u.getNombre());
    }
}
