package controlador;

import modelo.Reporte;
import java.util.List;
import java.util.Map;

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
 *    - Stock actual de todos los productos
 *    - Productos en nivel crítico (stock < 5)
 *    - Valorización total del inventario
 *    - Productos más/menos rotados
 * 
 * 2. REPORTE DE VENTAS:
 *    - Ventas por período (diario, semanal, mensual)
 *    - Ventas por cliente
 *    - Ventas por producto
 *    - Análisis de tendencias y proyecciones
 * 
 * 3. REPORTE DE CONSUMO DIARIO:
 *    - Registro de consumos del día
 *    - Comparación con límites permitidos (RF06)
 *    - Productos contratados vs adicionales (RF03)
 * 
 * 4. REPORTE DE ÓRDENES DE COMPRA:
 *    - Órdenes pendientes de aprobación
 *    - Órdenes enviadas a proveedores
 *    - Órdenes completadas y recibidas
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
// Asegúrate de que Reporte.java esté en el mismo paquete o sea importable

public class ReporteController {

    private Reporte reporte;

    public ReporteController() {
        // Inicializa el modelo Reporte
        this.reporte = new Reporte();
    }

// ----------------------------------------------------------------------
// MÉTODOS DE GENERACIÓN DE REPORTES
// ----------------------------------------------------------------------

    /**
     * Genera el Reporte de Inventario (Stock, Críticos, Valorización).
     * @param umbralCritico El stock mínimo para identificar productos críticos.
     * @return Los datos del reporte generado.
     */
    public List<Map<String, Object>> generarReporteInventario(int umbralCritico) {
        System.out.println("Controller: Solicitando reporte de Inventario con umbral crítico: " + umbralCritico);
        return reporte.generarReporteInventario(umbralCritico);
    }

    /**
     * Genera el Reporte de Ventas por Período (Solo Totales, dada la limitación de la BD).
     * @param fechaInicio Fecha de inicio para el filtro.
     * @param fechaFin Fecha de fin para el filtro.
     * @return Los datos del reporte generado (Fecha y Total).
     */
    public List<Map<String, Object>> generarReporteVentasPorPeriodo(String fechaInicio, String fechaFin) {
        System.out.println("Controller: Solicitando reporte de Ventas por período.");
        return reporte.generarReporteVentasPorPeriodo(fechaInicio, fechaFin);
    }
    
    /**
     * Genera el Reporte de Consumo Diario (Inviable con el esquema de BD actual).
     * @return null o una indicación de error.
     */
    public List<Map<String, Object>> generarReporteConsumoDiario() {
        System.out.println("Controller: Solicitando reporte de Consumo Diario.");
        return reporte.generarReporteConsumoDiario(); // Este método retornará el error/mensaje de inviabilidad
    }

    /**
     * Genera el Reporte de Órdenes de Compra (Sin filtro de estado, dada la limitación de la BD).
     * @return Los datos de todas las Órdenes de Compra.
     */
    public List<Map<String, Object>> generarReporteOrdenesCompra() {
        System.out.println("Controller: Solicitando reporte de Órdenes de Compra (Todas).");
        return reporte.generarReporteOrdenesCompra();
    }

// ----------------------------------------------------------------------
// MÉTODO DE EXPORTACIÓN
// ----------------------------------------------------------------------

    /**
     * Exporta un conjunto de datos (reporte) previamente generado.
     * @param reporteData Los datos a exportar.
     * @param nombreArchivo El nombre base para el archivo.
     * @param formato El formato de salida ("PDF", "XLSX", "CSV").
     */
    public void exportarReporte(List<Map<String, Object>> reporteData, String nombreArchivo, String formato) {
        if (reporteData == null) {
            System.out.println("Controller: Error, no se puede exportar datos nulos.");
            return;
        }
        System.out.println("Controller: Solicitando exportación a formato " + formato);
        // Delega la lógica de exportación al modelo (Reporte.java)
        reporte.exportarReporte(reporteData, nombreArchivo, formato);
    }
}