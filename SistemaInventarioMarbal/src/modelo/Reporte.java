package modelo;

/**
 * Reporte - Clase entidad para generación de reportes del sistema
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Rufo Ferrel (U23231492) - Controlador de Reportes
 * ROL: Entidad utilizada por ReporteController
 * 
 * DESCRIPCIÓN:
 * Esta clase representa la entidad Reporte del sistema. Permite generar
 * reportes y resúmenes que muestren consumos de productos y stock restante,
 * ofreciendo información en tiempo real para respaldar la toma de decisiones
 * estratégicas (RF05, RF10).
 * 
 * PROBLEMA QUE SOLUCIONA:
 * Actualmente Marbal enfrenta "limitaciones para generar reportes confiables
 * que respalden la toma de decisiones estratégicas" debido al uso de Excel.
 * Este módulo automatiza la generación de reportes precisos.
 * 
 * TIPOS DE REPORTES:
 * 1. Reporte de Inventario: Stock actual, productos críticos, valorización
 * 2. Reporte de Ventas: Ventas por período, cliente, producto
 * 3. Reporte de Consumo: Consumo diario de productos
 * 4. Reporte de Órdenes de Compra: Órdenes pendientes y completadas
 * 
 * FORMATO DE SALIDA:
 * - Visualización en JTable o JTextArea (responsabilidad de Rufo Ferrel)
 * - Exportación a PDF para firma y documentación (RF10)
 * - Generación de resúmenes ejecutivos
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
import conexion.ConexionBD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reporte {

    // =========================================================
    // 1. Reporte de Inventario: Stock actual, productos críticos, valorización
    //    NOTA: Este reporte sí se puede generar completamente con la tabla 'productos'.
    // =========================================================
    /**
     * Genera un reporte completo de Inventario. 
     * Incluye stock actual, identifica productos críticos y calcula la valorización.
     * @param umbralCritico El stock mínimo para considerar un producto como crítico.
     * @return Los datos procesados del inventario.
     */
    public List<Map<String, Object>> generarReporteInventario(int umbralCritico) {
        System.out.println("Generando Reporte de Inventario: Stock, Críticos, Valorización.");
        String sql = "SELECT id_producto, nombre, precio, stock, (precio * stock) AS valorizacion FROM producto ORDER BY nombre";
        List<Map<String, Object>> data = new ArrayList<>();
        ConexionBD cb = new ConexionBD();
        try (Connection c = cb.abrirConexion(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id_producto", rs.getInt("id_producto"));
                row.put("nombre", rs.getString("nombre"));
                row.put("precio", rs.getDouble("precio"));
                row.put("stock", rs.getInt("stock"));
                row.put("valorizacion", rs.getDouble("valorizacion"));
                row.put("critico", rs.getInt("stock") < umbralCritico);
                data.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de inventario: " + e.getMessage());
        }
        return data;
    }

    // =UNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCOREUNDERSCORE
    // 2. Reporte de Ventas: Ventas por período, cliente, producto
    //    LIMITACIÓN: No se puede reportar por 'producto' o 'cliente' ya que la tabla 'ventas' solo tiene fecha y total.
    // =========================================================
    /**
     * Genera un reporte de ventas **solo por totales y período**.
     * Es imposible reportar por 'producto' o 'cliente' con el esquema actual.
     * @param fechaInicio Fecha de inicio del período.
     * @param fechaFin Fecha de fin del período.
     * @return Los datos de las ventas (solo fecha y total).
     */
    public List<Map<String, Object>> generarReporteVentasPorPeriodo(String fechaInicio, String fechaFin) {
        System.out.println("Generando Reporte de Ventas (SOLO TOTALES) para el período: " + fechaInicio + " a " + fechaFin);
        String sql = "SELECT DATE(fecha) AS fecha, SUM(total) AS total FROM venta WHERE fecha BETWEEN ? AND ? GROUP BY DATE(fecha) ORDER BY fecha";
        List<Map<String, Object>> data = new ArrayList<>();
        ConexionBD cb = new ConexionBD();
        try (Connection c = cb.abrirConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("fecha", rs.getString("fecha"));
                    row.put("total", rs.getDouble("total"));
                    data.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de ventas: " + e.getMessage());
        }
        return data;
    }

    // =========================================================
    // 3. Reporte de Consumo: Consumo diario de productos
    //    LIMITACIÓN: Imposible de generar. La tabla 'ventas' no tiene registro de los productos consumidos.
    // =========================================================
    /**
     * **IMPOSIBLE DE GENERAR** con el esquema actual. 
     * La tabla 'ventas' solo tiene el total de la transacción, no los productos consumidos.
     * @return null
     */
    public List<Map<String, Object>> generarReporteConsumoDiario() {
        System.out.println("Generando Reporte de Consumo Diario de Productos...");
        System.out.println("-> ERROR: Este reporte es inviable. La base de datos no registra qué productos se vendieron.");
        return null;
    }
    
    // =========================================================
    // 4. Reporte de Órdenes de Compra: Órdenes pendientes y completadas
    //    LIMITACIÓN: Solo se puede reportar por proveedor y fecha. No se puede filtrar por 'estado' (pendiente/completada).
    // =========================================================
    /**
     * Genera un reporte de Órdenes de Compra **sin poder filtrar por estado**.
     * Es imposible filtrar por 'estado' ya que la tabla 'ordenes_compra' no tiene ese campo.
     * @return Todas las órdenes de compra registradas.
     */
    public List<Map<String, Object>> generarReporteOrdenesCompra() {
        System.out.println("Generando Reporte de Órdenes de Compra");
        String sql = "SELECT oc.id_oc, oc.fecha, p.razon_social AS proveedor, oc.total, oc.estado FROM orden_compra oc JOIN proveedor p ON oc.id_proveedor = p.id_proveedor ORDER BY oc.fecha DESC";
        List<Map<String, Object>> data = new ArrayList<>();
        ConexionBD cb = new ConexionBD();
        try (Connection c = cb.abrirConexion(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id_oc", rs.getInt("id_oc"));
                row.put("fecha", rs.getTimestamp("fecha"));
                row.put("proveedor", rs.getString("proveedor"));
                row.put("total", rs.getDouble("total"));
                row.put("estado", rs.getString("estado"));
                data.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de órdenes de compra: " + e.getMessage());
        }
        return data;
    }

    // =========================================================
    // Método de Exportación (Actualizado)
    // =========================================================
    /**
     * Exporta cualquier reporte generado a un archivo en el formato especificado.
     * @param reporteData El reporte (los datos) a exportar.
     * @param nombreArchivo El nombre base para el archivo (ej. "Inventario_2025").
     * @param formato El formato de salida ("PDF", "XLSX" o "CSV").
     */
    // Contenido de Reporte.java (Modelo)

// ... (métodos generarReporteInventario, generarReporteVentasPorPeriodo, etc. se mantienen)

// =========================================================
// Método de Exportación (Actualizado con Lógica PDF)
// =========================================================
/**
 * Exporta cualquier reporte generado a un archivo en el formato especificado.
 * @param reporteData El reporte (los datos) a exportar.
 * @param nombreArchivo El nombre base para el archivo (ej. "Ventas_Abril").
 * @param formato El formato de salida ("PDF", "XLSX" o "CSV").
 */
public void exportarReporte(List<Map<String, Object>> reporteData, String nombreArchivo, String formato) {
    if (reporteData == null || reporteData.isEmpty()) {
        System.out.println("No hay datos para exportar.");
        return;
    }

    switch (formato.toUpperCase()) {
        case "CSV": {
            File out = new File(nombreArchivo + ".csv");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(out, java.nio.charset.StandardCharsets.UTF_8))) {
                // encabezados por claves del primer registro
                Map<String, Object> first = reporteData.get(0);
                String[] headers = first.keySet().toArray(new String[0]);
                bw.write(String.join(",", headers));
                bw.newLine();
                for (Map<String, Object> row : reporteData) {
                    List<String> vals = new ArrayList<>();
                    for (String h : headers) {
                        Object v = row.get(h);
                        String s = v == null ? "" : v.toString().replace("\n", " ").replace(",", ";");
                        vals.add('"' + s + '"');
                    }
                    bw.write(String.join(",", vals));
                    bw.newLine();
                }
                System.out.println("CSV exportado en: " + out.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error exportando CSV: " + e.getMessage());
            }
            break;
        }
        case "PDF":
            System.out.println("Exportación a PDF no implementada en esta versión.");
            break;
        case "XLSX":
            System.out.println("Exportación a Excel no implementada en esta versión.");
            break;
        default:
            System.out.println("Formato de exportación no soportado: " + formato);
    }
}

    
}