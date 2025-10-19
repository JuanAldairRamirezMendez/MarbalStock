package modelo;

/**
 * Venta - Clase entidad para registrar transacciones de venta
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Keila Mateo (U23262823)
 * ROL: Desarrollo de Modelo - Entidades
 * 
 * RESPONSABILIDADES:
 * - Crear atributos, constructores, getters y setters
 * - Calcular precio final y ganancia por venta
 * - Registrar detalles de cada transacción
 * - Validar stock disponible antes de confirmar venta
 * 
 * DESCRIPCIÓN:
 * Esta clase representa la entidad Venta del sistema. Registra las
 * transacciones de venta mayorista realizadas por Marbal, asociando
 * productos, clientes y montos para generar reportes confiables (RF05).
 * 
 * OBJETIVO DEL SISTEMA:
 * Reemplazar el control manual en Excel para garantizar mayor precisión,
 * reducir errores humanos y ofrecer información en tiempo real que
 * respalde la toma de decisiones estratégicas.
 * 
 * ATRIBUTOS PRINCIPALES:
 * - id: Identificador único de la venta
 * - fecha: Fecha y hora de la transacción
 * - total: Monto total de la venta
 * - cliente: Cliente que realizó la compra
 * - detalleVenta: Lista de productos vendidos con cantidades
 * - estado: Estado de la venta (Pendiente, Completada, Cancelada)
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF01: Registrar consumo diario de productos
 * - RF05: Generar reportes de ventas
 * - RF06: Validar límite diario de consumo
 * 
 * FECHA: Octubre 2025
 * 
 * @author Keila Mateo
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.List;

public class Venta {

    private int id;
    private Cliente cliente;
    private List<Producto> productos;
    private double total;

    public Venta() {
        productos = new ArrayList<>();
    }

    public Venta(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.productos = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<Producto> getProductos() { return productos; }

    public double getTotal() { return total; }

    // Agregar producto a la venta (valida stock)
    public void agregarProducto(Producto p) {
        if (p.hayStock()) {
            productos.add(p);
            total += p.getPrecioVenta();
        } else {
            System.out.println("⚠️ No hay stock disponible del producto: " + p.getNombre());
        }
    }

    // Calcular el total de la venta
    public double calcularTotal() {
        total = 0;
        for (Producto p : productos) {
            total += p.getPrecioVenta();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Venta [id=" + id + ", cliente=" + cliente.getNombre() + 
               ", total=" + total + ", productos=" + productos.size() + "]";
    }
}
