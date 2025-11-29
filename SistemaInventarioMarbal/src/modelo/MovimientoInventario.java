package modelo;

import java.time.LocalDateTime;

/**
 * MovimientoInventario - Registra entradas/salidas/ajustes de inventario
 */
public class MovimientoInventario {
    private int id;
    private int productoId;
    private int cantidad;
    private Integer clienteId; // nullable
    private String tipo; // INGRESO, SALIDA, AJUSTE
    private String descripcion;
    private LocalDateTime fecha;

    public MovimientoInventario(int id, int productoId, int cantidad, Integer clienteId, String tipo, String descripcion, LocalDateTime fecha) {
        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.clienteId = clienteId;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public MovimientoInventario(int productoId, int cantidad, String tipo, String descripcion) {
        this(0, productoId, cantidad, null, tipo, descripcion, LocalDateTime.now());
    }

    public MovimientoInventario(int productoId, int cantidad, Integer clienteId, String tipo, String descripcion) {
        this(0, productoId, cantidad, clienteId, tipo, descripcion, LocalDateTime.now());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
