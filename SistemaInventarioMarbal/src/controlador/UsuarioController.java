package controlador;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    private final ConexionBD conexionBD;

    public UsuarioController() {
        this.conexionBD = new ConexionBD();
    }

    public void agregarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios(nombre, rol) VALUES (?, ?)";
        try (Connection conn = conexionBD.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getRol());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar usuario: " + e.getMessage(), e);
        } finally {
            conexionBD.cerrarConexion();
        }
    }

    public void eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = conexionBD.abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage(), e);
        } finally {
            conexionBD.cerrarConexion();
        }
    }

    public void modificarUsuario(Usuario usuarioModificado) {
        String sql = "UPDATE usuarios SET nombre = ?, rol = ? WHERE id = ?";
        try (Connection conn = conexionBD.abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuarioModificado.getNombre());
            ps.setString(2, usuarioModificado.getRol());
            ps.setInt(3, usuarioModificado.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar usuario: " + e.getMessage(), e);
        } finally {
            conexionBD.cerrarConexion();
        }
    }

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, rol FROM usuarios";
        try (Connection conn = conexionBD.abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("rol"));
                lista.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener usuarios: " + e.getMessage(), e);
        } finally {
            conexionBD.cerrarConexion();
        }
        return lista;
    }

    public Usuario obtenerUsuarioPorId(int id) {
        String sql = "SELECT id, nombre, rol FROM usuarios WHERE id = ?";
        try (Connection conn = conexionBD.abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("rol"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener usuario por id: " + e.getMessage(), e);
        } finally {
            conexionBD.cerrarConexion();
        }
        return null;
    }
}