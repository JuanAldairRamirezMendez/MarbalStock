package modelo;

/**
 * Usuario - Clase entidad para gestión de usuarios del sistema
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Erick Estrada (U22302925) - Controladores
 *              Keila Mateo (U23262823) - Modelo
 * ROL: Modelo de entidad utilizado por UsuarioController
 * 
 * DESCRIPCIÓN:
 * Esta clase representa la entidad Usuario del sistema. Define los
 * diferentes roles de usuario (Administrador, Operario) para implementar
 * el sistema de autenticación y gestión de permisos (RF07).
 * 
 * ROLES DEL SISTEMA:
 * - ADMINISTRADOR: Gestiona productos, usuarios, valida operaciones,
 *                  accede a todos los módulos del sistema
 * - OPERARIO: Registra consumos diarios, consulta inventario,
 *             acceso limitado según permisos
 * 
 * SEGURIDAD:
 * La contraseña se encriptará usando SHA-256 en UsuarioController
 * (responsabilidad de Erick Estrada).
 * 
 * ATRIBUTOS PRINCIPALES:
 * - id: Identificador único del usuario
 * - nombre: Nombre completo del usuario
 * - rol: Tipo de usuario (Administrador/Operario)
 * - username: Nombre de usuario para login
 * - password: Contraseña encriptada (SHA-256)
 * - activo: Estado del usuario en el sistema
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF07: Gestionar usuarios y permisos
 * - Login y autenticación del sistema
 * 
 * FECHA: Octubre 2025
 * 
 * @author Keila Mateo (Modelo)
 * @author Erick Estrada (Lógica de seguridad)
 * @version 1.0
 */
public class Usuario {
    private int id;
    // nombre para mostrar (puede ser igual a username si no hay nombre completo)
    private String nombre;
    private String rol;
    // nuevos campos para autenticación
    private String username;
    private String passwordHash;

    // Constructor clásico usado en vistas actuales
    public Usuario(int id, String nombre, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
    }

    // Constructor extendido para autenticación real
    public Usuario(int id, String username, String passwordHash, String rol) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        // Si no contamos con nombre completo, usamos el username como nombre para mostrar
        this.nombre = username;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", username='" + username + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}