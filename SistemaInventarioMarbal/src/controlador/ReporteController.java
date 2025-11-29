package controlador;

import modelo.Reporte;
import java.util.ArrayList;
import modelo.Producto;

/**
 * ReporteController - Controlador para generación de reportes
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Rufo Ferrel (U23231492)
 * ROL: Interfaz Gráfica - Login y Reportes
 * 
 * RESPONSABILIDADES DE RUFO:
 * - Crear controlador para reportes de inventario y ventas
 * - Mostrar reportes en JTable o JTextArea
 * - Implementar filtros por fecha, cliente, producto
 * - Exportar reportes a formato imprimible (PDF)
 * 
 * DESCRIPCIÓN:
 * Este controlador gestiona la generación y visualización de reportes
 * del sistema, proporcionando información actualizada y confiable en
 * tiempo real para respaldar la toma de decisiones (RF05, RF10).
 * 
 * TIPOS DE REPORTES IMPLEMENTADOS:
 * 
 * 1. REPORTE DE INVENTARIO:
 * - Stock actual de todos los productos
 * - Productos en nivel crítico (stock < 5)
 * - Valorización total del inventario
 * - Productos más/menos rotados
 * 
 * 2. REPORTE DE VENTAS:
 * - Ventas por período (diario, semanal, mensual)
 * - Ventas por cliente
 * - Ventas por producto
 * - Análisis de tendencias y proyecciones
 * 
 * 3. REPORTE DE CONSUMO DIARIO:
 * - Registro de consumos del día
 * - Comparación con límites permitidos (RF06)
 * - Productos contratados vs adicionales (RF03)
 * 
 * 4. REPORTE DE ÓRDENES DE COMPRA:
 * - Órdenes pendientes de aprobación
 * - Órdenes enviadas a proveedores
 * - Órdenes completadas y recibidas
 * 
 * FORMATO DE VISUALIZACIÓN:
 * - JTable: Para reportes tabulares con múltiples registros
 * - JTextArea: Para resúmenes ejecutivos y totales
 * - PDF: Para impresión y archivo físico con firma (RF10)
 * 
 * PROBLEMA QUE SOLUCIONA:
 * Elimina las "limitaciones para generar reportes confiables" que
 * actualmente enfrenta Marbal al usar Excel, proporcionando reportes
 * automáticos, precisos y en tiempo real.
 * 
 * INTEGRACIÓN CON OTRAS CLASES:
 * - Reporte.java (Modelo): Estructura de datos del reporte
 * - InventarioController.java (Diego): Datos de inventario
 * - Venta.java (Keila): Datos de ventas
 * - ConexionBD.java (Juan): Consultas a base de datos
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF05: Generar reportes de ventas o inventario
 * - RF10: Permitir impresión de reportes con formato para firma
 * 
 * FECHA: Octubre 2025
 * 
 * @author Rufo Ferrel
 * @version 1.0
 */
public class ReporteController {

    private Reporte reporte;

    public ReporteController() {
        this.reporte = new Reporte();
    }
    /** Devuelve un reporte tabular del inventario actual (consultando InventarioController). */
    public Object[][] listarInventario() {
        InventarioController inv = new InventarioController();
        ArrayList<Producto> productos = inv.listarProductos();
        Object[][] rows = new Object[productos.size()][];
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            rows[i] = new Object[] { p.getId(), p.getCodigo(), p.getNombre(), p.getTipo(), p.getStock(), p.getPrecioVenta(), p.getIdProveedor() };
        }
        return rows;
    }

    public String[] getColumnNamesInventario() {
        return new String[] { "ID", "Código", "Nombre", "Tipo", "Stock", "Precio Venta", "Proveedor ID" };
    }

    /**
     * Lista inventario filtrado por umbral de stock (productos con stock < stockThreshold)
     * y/o por idProveedor. Si un parámetro es null se ignora ese filtro.
     */
    public Object[][] listarInventarioFiltrado(Integer stockThreshold, Integer idProveedor) {
        InventarioController inv = new InventarioController();
        ArrayList<Producto> productos = inv.listarProductos();
        ArrayList<Object[]> rows = new ArrayList<>();
        for (Producto p : productos) {
            boolean pasa = true;
            if (stockThreshold != null) {
                if (!(p.getStock() < stockThreshold)) {
                    pasa = false;
                }
            }
            if (idProveedor != null) {
                if (p.getIdProveedor() != idProveedor.intValue()) {
                    pasa = false;
                }
            }
            if (pasa) {
                rows.add(new Object[] { p.getId(), p.getCodigo(), p.getNombre(), p.getTipo(), p.getStock(), p.getPrecioVenta(), p.getIdProveedor() });
            }
        }
        Object[][] result = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) result[i] = rows.get(i);
        return result;
    }

    public void generarReporte() {
        // delegar a modelo si es necesario (reporte de productos/inventario)
        reporte.generarReporteProductos();
    }

    public void exportarReporte(String formato) {
        // delegar a modelo o implementar export real
        reporte.exportarReporte(formato);
    }
}