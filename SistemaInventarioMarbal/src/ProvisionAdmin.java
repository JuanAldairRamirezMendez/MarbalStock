import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProvisionAdmin {
    public static void main(String[] args) throws Exception {
        ConexionBD cb = new ConexionBD();
        try (Connection conn = cb.abrirConexion()) {
            conn.setAutoCommit(false);
            try {
                Integer rolId = null;
                // Buscar rol ADMIN
                try (PreparedStatement ps = conn.prepareStatement("SELECT id_rol FROM rol WHERE nombre = 'ADMIN'")) {
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) rolId = rs.getInt(1);
                    }
                }
                // Crear rol si no existe
                if (rolId == null) {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO rol(nombre) VALUES('ADMIN')", PreparedStatement.RETURN_GENERATED_KEYS)) {
                        ps.executeUpdate();
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (rs.next()) rolId = rs.getInt(1);
                        }
                    }
                }
                if (rolId == null) throw new IllegalStateException("No se pudo obtener id del rol ADMIN");

                // Verificar si existe usuario admin
                Integer userId = null;
                try (PreparedStatement ps = conn.prepareStatement("SELECT id_usuario FROM usuario WHERE username = 'admin'")) {
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) userId = rs.getInt(1);
                    }
                }
                if (userId == null) {
                    // Insertar admin
                    try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO usuario(username, password_hash, id_rol, activo) VALUES('admin', SHA2('admin',256), ?, 1)")) {
                        ps.setInt(1, rolId);
                        ps.executeUpdate();
                        System.out.println("[OK] Usuario 'admin' creado con contraseña 'admin'.");
                    }
                } else {
                    // Actualizar admin
                    try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE usuario SET password_hash = SHA2('admin',256), id_rol = ?, activo = 1 WHERE id_usuario = ?")) {
                        ps.setInt(1, rolId);
                        ps.setInt(2, userId);
                        ps.executeUpdate();
                        System.out.println("[OK] Usuario 'admin' actualizado y activado.");
                    }
                }
                conn.commit();
                System.out.println("[DONE] Provisión completada.");
            } catch (Exception ex) {
                conn.rollback();
                System.err.println("[FAIL] Error en provisión: " + ex.getMessage());
                throw ex;
            }
        }
    }
}
