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
public class Venta {
    private int id;
    private String fecha;
    private double total;

    public Venta(int id, String fecha, double total) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}