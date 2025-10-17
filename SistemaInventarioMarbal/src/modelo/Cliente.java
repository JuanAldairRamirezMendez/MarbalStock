package modelo;

/**
 * Cliente - Clase entidad para gestionar clientes
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Keila Mateo (U23262823)
 * ROL: Desarrollo de Modelo - Entidades
 * 
 * RESPONSABILIDADES:
 * - Crear atributos, constructores, getters y setters
 * - Gestionar información de clientes mayoristas
 * - Mantener historial de ventas por cliente
 * 
 * DESCRIPCIÓN:
 * Esta clase representa la entidad Cliente del sistema. Almacena
 * información de los clientes mayoristas de Marbal (ferreterías,
 * comerciantes minoristas, contratistas) según la descripción del negocio.
 * 
 * CONTEXTO DE NEGOCIO:
 * Marbal atiende a diversos tipos de clientes: ferreterías de barrio,
 * comerciantes minoristas y contratistas que compran materiales de
 * construcción, alimentos y bebidas al por mayor.
 * 
 * ATRIBUTOS PRINCIPALES:
 * - id: Identificador único del cliente
 * - nombre: Razón social o nombre del cliente
 * - direccion: Dirección de entrega
 * - ruc: Número de RUC del cliente
 * - telefono: Número de contacto
 * - tipoCliente: Clasificación (Ferretería, Minorista, Contratista)
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF05: Generar reportes de ventas
 * - RF08: Editar o eliminar registros de clientes
 * 
 * FECHA: Octubre 2025
 * 
 * @author Keila Mateo
 * @version 1.0
 */
public class Cliente {
    private int id;
    private String nombre;
    private String direccion;

    public Cliente(int id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}