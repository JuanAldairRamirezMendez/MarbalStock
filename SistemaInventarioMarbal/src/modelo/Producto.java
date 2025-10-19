package modelo;

/**
 * Producto - Clase entidad para representar productos del inventario
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Keila Mateo (U23262823)
 * ROL: Desarrollo de Modelo - Entidades
 * 
 * RESPONSABILIDADES:
 * - Crear atributos, constructores, getters y setters
 * - Implementar algoritmo de clasificación de productos (contratado/adicional)
 * - Calcular precio final y ganancia
 * - Validar stock y tipo de producto
 * 
 * DESCRIPCIÓN:
 * Esta clase representa la entidad Producto del sistema. Permite
 * diferenciar entre productos contratados y adicionales (RF01, RF03),
 * controlar el stock y generar alertas cuando llegue a niveles críticos (RF02).
 * 
 * ATRIBUTOS PRINCIPALES:
 * - id: Identificador único del producto
 * - nombre: Nombre descriptivo del producto
 * - precio: Precio unitario del producto
 * - stock: Cantidad disponible en inventario
 * - tipoProducto: Clasificación (CONTRATADO/ADICIONAL)
 * - stockMinimo: Nivel crítico para alertas (por defecto 5)
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF01: Registrar consumo diario de productos
 * - RF02: Generar alerta de stock bajo
 * - RF03: Clasificar producto como contratado o adicional
 * - RF08: Editar o eliminar registros de productos
 * 
 * FECHA: Octubre 2025
 * 
 * @author Keila Mateo
 * @version 1.0
 */
public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private int stock;

    

    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean hayStock() {
        return stock > 0;
    }

    
}