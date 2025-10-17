package modelo;

/**
 * Proveedor - Clase entidad para gestionar proveedores
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Keila Mateo (U23262823)
 * ROL: Desarrollo de Modelo - Entidades
 * 
 * RESPONSABILIDADES:
 * - Crear atributos, constructores, getters y setters
 * - Gestionar información de proveedores para órdenes de compra
 * - Mantener registro de contactos y datos relevantes
 * 
 * DESCRIPCIÓN:
 * Esta clase representa la entidad Proveedor del sistema. Almacena
 * información de los proveedores que suministran productos a Marbal,
 * facilitando la gestión de órdenes de compra automáticas (RF04).
 * 
 * ATRIBUTOS PRINCIPALES:
 * - id: Identificador único del proveedor
 * - nombre: Razón social o nombre del proveedor
 * - contacto: Información de contacto (teléfono, email)
 * - ruc: Número de RUC del proveedor
 * - direccion: Dirección física del proveedor
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF04: Generar orden de compra automática
 * - RF08: Editar o eliminar registros de proveedores
 * 
 * FECHA: Octubre 2025
 * 
 * @author Keila Mateo
 * @version 1.0
 */
public class Proveedor {
    private int id;
    private String nombre;
    private String contacto;

    public Proveedor(int id, String nombre, String contacto) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
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

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
}