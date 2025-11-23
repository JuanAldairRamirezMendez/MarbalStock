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

/**
 * Modelo que representa un producto dentro del sistema MarbalStock.
 * Incluye lógica de negocio para clasificación, cálculo de precios,
 * ganancia y validación de stock.
 */

public class Producto {

    // ===== ATRIBUTOS =====
    private int id;
    private String codigo;
    private String nombre;
    private String tipo;
    private int stock;
    private int stockMinimo;
    private double precioCosto;
    private double precioVenta;
    private int idProveedor;

    // ===== CONSTRUCTOR =====
    public Producto(int id, String codigo, String nombre, String tipo,
                    int stock, int stockMinimo,
                    double precioCosto, double precioVenta,
                    int idProveedor) {

        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;
        this.idProveedor = idProveedor;
    }

    // ===================================================
    // MÉTODOS ALGORÍTMICOS
    // ===================================================

    /**
     * Clasifica el producto según su tipo.
     *
     * @return categoría del producto:
     *         CONSUMIBLE, TECNOLOGÍA, INDUSTRIAL u OTROS.
     */
    public String clasificarProducto() {

        if (tipo == null || tipo.trim().isEmpty()) {
            return "SIN CLASIFICAR";
        }

        switch (tipo.toLowerCase()) {
            case "bebida":
            case "alimento":
                return "CONSUMIBLE";

            case "electrónico":
            case "computo":
                return "TECNOLOGÍA";

            case "herramienta":
            case "material":
                return "INDUSTRIAL";

            default:
                return "OTROS";
        }
    }

    /**
     * Calcula el precio final con IGV (18%).
     *
     * Casos límite:
     * - Si el precio de venta es negativo o 0, retorna 0.
     *
     * @return precio final con IGV incluido.
     */
    public double calcularPrecioFinal() {
        if (precioVenta <= 0) {
            return 0.0;
        }
        return precioVenta * 1.18;
    }

    /**
     * Calcula la ganancia por cada unidad vendida.
     *
     * Casos límite:
     * - Si el precioCosto o precioVenta son <= 0, retorna 0.
     *
     * @return ganancia por unidad.
     */
    public double calcularGanancia() {
        if (precioCosto <= 0 || precioVenta <= 0) {
            return 0.0;
        }
        return precioVenta - precioCosto;
    }

    /**
     * Valida si el stock actual es suficiente.
     *
     * Casos límite:
     * - Si el stock es menor al stock mínimo, retorna false.
     *
     * @return true si el stock es suficiente, false si está bajo.
     */
    public boolean validarStock() {
        return stock >= stockMinimo;
    }

    // ===================================================
    // GETTERS Y SETTERS CON VALIDACIONES
    // ===================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.isEmpty()) {
            this.codigo = "SIN-CODIGO";
        } else {
            this.codigo = codigo;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            this.nombre = "SIN NOMBRE";
        } else {
            this.nombre = nombre;
        }
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if (tipo == null || tipo.isEmpty()) {
            this.tipo = "OTROS";
        } else {
            this.tipo = tipo;
        }
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            this.stock = 0;
        } else {
            this.stock = stock;
        }
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo < 0) {
            this.stockMinimo = 0;
        } else {
            this.stockMinimo = stockMinimo;
        }
    }

    public double getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(double precioCosto) {
        if (precioCosto < 0) {
            this.precioCosto = 0.0;
        } else {
            this.precioCosto = precioCosto;
        }
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        if (precioVenta < 0) {
            this.precioVenta = 0.0;
        } else {
            this.precioVenta = precioVenta;
        }
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    // ===================================================
    // MÉTODO toString() PARA DEPURACIÓN
    // ===================================================

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", stock=" + stock +
                ", stockMinimo=" + stockMinimo +
                ", precioCosto=" + precioCosto +
                ", precioVenta=" + precioVenta +
                ", idProveedor=" + idProveedor +
                '}';
    }
}
