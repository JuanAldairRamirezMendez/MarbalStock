package controlador;

import conexion.ConexionBD;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Usuario;

/**
 * UsuarioController - Controlador de autenticación y gestión de usuarios
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Erick Estrada (U22302925)
 * ROL: Controladores de Lógica y Seguridad
 * 
 * RESPONSABILIDADES DE ERICK:
 * - Implementar encriptación de contraseñas (SHA-256)
 * - Programar autenticación de usuario (login)
 * - Validar roles (Administrador, Operario)
 * - Gestionar permisos de acceso a módulos
 * 
 * DESCRIPCIÓN:
 * Este controlador implementa la lógica de seguridad del sistema,
 * gestionando la autenticación de usuarios y validación de roles.
 * Es utilizado por LoginFrame.java (Rufo Ferrel) para validar credenciales.
 * 
 * ALGORITMOS DE SEGURIDAD:
 * 1. Encriptación SHA-256: Las contraseñas se almacenan encriptadas
 * 2. Validación de roles: Administrador vs Operario
 * 3. Control de sesión: Gestión del usuario activo en el sistema
 * 4. Auditoría: Registro de accesos al sistema
 * 
 * ROLES Y PERMISOS:
 * - ADMINISTRADOR: 
 *   * Gestiona productos, usuarios, valida operaciones
 *   * Acceso completo a todos los módulos
 *   * Genera reportes y órdenes de compra
 * 
 * - OPERARIO:
 *   * Registra consumos diarios
 *   * Consulta inventario
 *   * Acceso limitado según permisos
 * 
 * INTEGRACIÓN CON OTRAS CLASES:
 * - LoginFrame.java (Rufo Ferrel): Interfaz de login
 * - Usuario.java (Keila Mateo): Modelo de entidad
 * - MenuPrincipal.java (Diego García): Control de acceso por rol
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF07: Gestionar usuarios y permisos
 * - Login y autenticación del sistema
 * 
 * FECHA: Octubre 2025
 * 
 * @author Erick Estrada
 * @version 1.0
 */
public class UsuarioController {
    private List<Usuario> usuarios;
    private String lastError;
    private static final Logger LOG = Logger.getLogger(UsuarioController.class.getName());

    public UsuarioController() {
        this.usuarios = new ArrayList<>();
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void eliminarUsuario(int id) {
        usuarios.removeIf(usuario -> usuario.getId() == id);
    }

    public void modificarUsuario(Usuario usuarioModificado) {
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            if (usuario.getId() == usuarioModificado.getId()) {
                usuarios.set(i, usuarioModificado);
                break;
            }
        }
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarios;
    }

    public Usuario obtenerUsuarioPorId(int id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Autentica por username y password en texto claro (se convierte a SHA-256 y compara con password_hash).
     * Carga desde memoria (usuarios) previamente obtenidos de la BD.
     */
    public Usuario autenticar(String username, String passwordPlano) {
        if (username == null || passwordPlano == null) return null;
        String hash = sha256(passwordPlano);
        for (Usuario u : usuarios) {
            if (username.equalsIgnoreCase(u.getUsername()) && hash.equalsIgnoreCase(u.getPasswordHash())) {
                return u;
            }
        }
        return null;
    }

    /**
     * Carga los usuarios desde la base de datos (tabla usuario: id_usuario, username, id_rol)
     * CAMBIO BD: Ajustado para usar el esquema normalizado - tabla 'usuario' con columnas correctas
     */
    public boolean cargarUsuariosDesdeBD() {
        ConexionBD cb = new ConexionBD();
        Connection conn = cb.abrirConexion();
        if (conn == null) {
            lastError = "No se pudo obtener conexión (verifique driver/URL/credenciales).";
            return false;
        }
        // Se usa 'usuario' y se obtienen username y password_hash + rol
        String sql = "SELECT u.id_usuario, u.username, u.password_hash, r.nombre AS rol " +
                     "FROM usuario u INNER JOIN rol r ON u.id_rol = r.id_rol WHERE u.activo = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            usuarios.clear();
            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String username = rs.getString("username");
                String passwordHash = rs.getString("password_hash");
                String rol = rs.getString("rol");
                usuarios.add(new Usuario(id, username, passwordHash, rol));
            }
            lastError = null;
            return true;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error cargando usuarios desde BD", e);
            lastError = e.getClass().getSimpleName() + ": " + e.getMessage();
            return false;
        } finally {
            cb.cerrarConexion();
        }
    }

    // Utilidad SHA-256
    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }

    public String getLastError() {
        return lastError;
    }
}