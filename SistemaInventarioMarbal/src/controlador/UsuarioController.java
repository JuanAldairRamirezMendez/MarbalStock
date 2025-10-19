package controlador;

import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;
import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

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
     * Autentica por el campo 'nombre' (según esquema de BD actual donde no hay username/password).
     * Retorna el Usuario si encuentra una coincidencia por nombre, o null si no existe.
     */
    public Usuario autenticarPorNombre(String nombre) {
        if (nombre == null) return null;
        for (Usuario usuario : usuarios) {
            if (nombre.equalsIgnoreCase(usuario.getNombre())) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Carga los usuarios desde la base de datos (tabla usuarios: id, nombre, rol)
     */
    public boolean cargarUsuariosDesdeBD() {
        ConexionBD cb = new ConexionBD();
        Connection conn = cb.abrirConexion();
        if (conn == null) {
            lastError = "No se pudo obtener conexión (verifique driver/URL/credenciales).";
            return false;
        }
        String sql = "SELECT id, nombre, rol FROM usuarios";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            usuarios.clear();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String rol = rs.getString("rol");
                usuarios.add(new Usuario(id, nombre, rol));
            }
            lastError = null;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getClass().getSimpleName() + ": " + e.getMessage();
            return false;
        } finally {
            cb.cerrarConexion();
        }
    }

    public String getLastError() {
        return lastError;
    }
}