package controlador;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;

/**
 * Nota: este controlador mantiene métodos en memoria para gestión básica,
 * pero añade autenticación contra la base de datos mediante `autenticar`.
 */

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
    private String lastErrorMessage;

    public UsuarioController() {
        this.usuarios = new ArrayList<>();
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    /**
     * Autentica un usuario contra la tabla `usuarios` en la base de datos.
     * Devuelve un objeto `Usuario` con id, nombre y rol si la autenticación es correcta.
     */
    public Usuario autenticar(String username, String password) {
        ConexionBD cb = new ConexionBD();
        Connection conn = cb.abrirConexion();
        if (conn == null) {
            System.err.println("UsuarioController: no se pudo abrir conexión a la base de datos. Verifique recursos/config/db.properties y credenciales.");
            return null;
        }

        String sql = "SELECT id, nombre, rol, password_hash, salt FROM usuarios WHERE username = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    String salt = rs.getString("salt");
                    String computed;
                    if (salt != null && !salt.isEmpty()) {
                        computed = SecurityUtil.sha256Hex(salt + password);
                    } else {
                        computed = SecurityUtil.sha256Hex(password);
                    }
                    if (computed.equalsIgnoreCase(storedHash)) {
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        String rol = rs.getString("rol");
                        return new Usuario(id, nombre, rol);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cb.cerrarConexion();
        }
        return null;
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     * Genera un salt aleatorio y guarda el hash SHA-256(salt+password).
     * Retorna true si se insertó correctamente, false en caso contrario.
     */
    public boolean registrarUsuario(String username, String nombre, String password, String rol) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            lastErrorMessage = "Username y contraseña son obligatorios.";
            return false;
        }
        ConexionBD cb = new ConexionBD();
        Connection conn = cb.abrirConexion();
        if (conn == null) {
            lastErrorMessage = "No se pudo conectar a la base de datos.";
            System.err.println("UsuarioController.registrarUsuario: no se pudo abrir conexión a la base de datos.");
            return false;
        }
        try {
            // comprobar si username existe
            String check = "SELECT id FROM usuarios WHERE username = ? LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(check)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return false; // ya existe
                }
            }

            // generar salt simple (UUID)
            String salt = java.util.UUID.randomUUID().toString().replaceAll("-", "");
            String hash = SecurityUtil.sha256Hex(salt + password);

            String insert = "INSERT INTO usuarios (nombre, username, password_hash, salt, rol, activo) VALUES (?, ?, ?, ?, ?, 1)";
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, nombre == null ? username : nombre);
                ps.setString(2, username);
                ps.setString(3, hash);
                ps.setString(4, salt);
                ps.setString(5, rol == null ? "OPERARIO" : rol);
                int affected = ps.executeUpdate();
                if (affected > 0) {
                    lastErrorMessage = null;
                }
                return affected > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Detallar error para UI
            lastErrorMessage = ex.getMessage();
        } finally {
            cb.cerrarConexion();
        }
        return false;
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
}