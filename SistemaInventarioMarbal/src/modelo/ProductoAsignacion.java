package modelo;

import java.time.LocalDateTime;

/**
 * Representa la asignación anual de un producto para un cliente.
 */
public class ProductoAsignacion {
    private int id;
    private int clienteId;
    private int productoId;
    private double asignacionAnual; // en la unidad del producto
    private String unidad;
    private boolean activo;
    private LocalDateTime createdAt;

    public ProductoAsignacion(int id, int clienteId, int productoId, double asignacionAnual, String unidad, boolean activo) {
        this.id = id;
        this.clienteId = clienteId;
        this.productoId = productoId;
        this.asignacionAnual = asignacionAnual;
        this.unidad = unidad;
        this.activo = activo;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public int getClienteId() { return clienteId; }
    public int getProductoId() { return productoId; }
    public double getAsignacionAnual() { return asignacionAnual; }
    public String getUnidad() { return unidad; }
    public boolean isActivo() { return activo; }

    public double getMaxMensual() { return asignacionAnual / 12.0; }
    public double getMaxDiario(int diasMes) { return getMaxMensual() / (diasMes <= 0 ? 30 : diasMes); }
}
