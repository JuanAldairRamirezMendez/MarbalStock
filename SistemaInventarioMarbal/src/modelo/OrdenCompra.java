package modelo;

/**
 * OrdenCompra - Clase entidad para órdenes de compra automáticas
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Erick Estrada (U22302925) - Controlador
 *              Keila Mateo (U23262823) - Modelo
 * ROL: Entidad utilizada por OrdenCompraController
 * 
 * DESCRIPCIÓN:
 * Esta clase representa la entidad OrdenCompra del sistema. Permite
 * gestionar las órdenes de compra que se generan automáticamente
 * cuando el stock de un producto cae por debajo del nivel crítico (RF04).
 * 
 * FUNCIONALIDAD PRINCIPAL:
 * El sistema genera automáticamente órdenes de compra cuando:
 * - Stock del producto < 5 unidades (nivel crítico)
 * - Se identifica desabastecimiento de productos críticos
 * - Se requiere reposición eficiente de inventario
 * 
 * CONTEXTO DE NEGOCIO:
 * Soluciona el problema de "procesos de reposición poco eficientes"
 * mencionado en la justificación del proyecto, evitando el
 * desabastecimiento de productos críticos.
 * 
 * ATRIBUTOS PRINCIPALES:
 * - id: Identificador único de la orden
 * - fecha: Fecha de generación de la orden
 * - proveedor: Proveedor asignado para la compra
 * - producto: Producto a reponer
 * - cantidad: Cantidad a ordenar
 * - estado: Estado de la orden (Pendiente, Enviada, Recibida)
 * - generadaAutomaticamente: Indica si fue generada por el sistema
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF02: Generar alerta de stock bajo
 * - RF04: Generar orden de compra automática
 * 
 * FECHA: Octubre 2025
 * 
 * @author Keila Mateo (Modelo)
 * @author Erick Estrada (Lógica de generación automática)
 * @version 1.0
 */
public class OrdenCompra {
    private int id;
    private String fecha;
    private Proveedor proveedor;

    public OrdenCompra(int id, String fecha, Proveedor proveedor) {
        this.id = id;
        this.fecha = fecha;
        this.proveedor = proveedor;
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

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}