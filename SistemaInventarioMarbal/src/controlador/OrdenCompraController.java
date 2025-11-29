package controlador;

import java.util.List;
import modelo.OrdenCompra;
import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Proveedor;

/**
 * OrdenCompraController - Controlador para órdenes de compra automáticas
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Erick Estrada (U22302925)
 * ROL: Controladores de Lógica y Seguridad
 * 
 * RESPONSABILIDADES DE ERICK:
 * - Crear generación automática de órdenes de compra (stock < 5)
 * - Evaluar nivel de stock crítico de productos
 * - Notificar al encargado de compras
 * - Gestionar estados de órdenes de compra
 * 
 * DESCRIPCIÓN:
 * Este controlador implementa la lógica de negocio para generar
 * automáticamente órdenes de compra cuando el stock de productos
 * cae por debajo del nivel crítico (RF04).
 * 
 * ALGORITMO DE GENERACIÓN AUTOMÁTICA:
 * 1. Monitorear stock de productos en tiempo real
 * 2. Detectar cuando stock < 5 unidades (nivel crítico)
 * 3. Identificar proveedor asignado al producto
 * 4. Calcular cantidad óptima de reposición
 * 5. Generar orden de compra automáticamente
 * 6. Notificar al encargado de compras para aprobación
 * 
 * PROBLEMA QUE SOLUCIONA:
 * Elimina los "procesos de reposición poco eficientes" mencionados
 * en la justificación del proyecto, evitando desabastecimiento de
 * productos críticos que actualmente afecta a Marbal.
 * 
 * FLUJO DE TRABAJO:
 * 1. Sistema detecta stock bajo → Genera alerta (RF02)
 * 2. OrdenCompraController crea orden automática → Asigna proveedor
 * 3. Encargado de compras revisa y aprueba → Envía orden
 * 4. Sistema actualiza estado → Confirma recepción
 * 
 * INTEGRACIÓN CON OTRAS CLASES:
 * - OrdenCompra.java (Keila Mateo): Modelo de entidad
 * - InventarioController.java (Diego García): Monitoreo de stock
 * - Producto.java (Keila Mateo): Validación de stock
 * - Proveedor.java (Keila Mateo): Asignación de proveedor
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF02: Generar alerta de stock bajo
 * - RF04: Generar orden de compra automática
 * 
 * FECHA: Octubre 2025
 * 
 * @author Erick Estrada
 * @version 1.0
 */
public class OrdenCompraController {
    private ConexionBD conexionBD;

    public OrdenCompraController() {
        this.conexionBD = new ConexionBD();
    }

    public boolean agregarOrdenCompra(OrdenCompra ordenCompra) {
        String sql = "INSERT INTO ordenes_compra (fecha, proveedor_id) VALUES (?, ?)";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ordenCompra.getFecha());
            Proveedor p = ordenCompra.getProveedor();
            if (p != null) ps.setInt(2, p.getId()); else ps.setNull(2, java.sql.Types.INTEGER);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) ordenCompra.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void eliminarOrdenCompra(int id) {
        // Lógica para eliminar una orden de compra por ID
    }

    public void modificarOrdenCompra(OrdenCompra ordenCompra) {
        // Lógica para modificar una orden de compra
    }

    public OrdenCompra obtenerOrdenCompra(int id) {
        // Lógica para obtener una orden de compra por ID
        return null; // Cambiar por la implementación real
    }

    public List<OrdenCompra> listarOrdenesCompra() {
        // Lógica para listar todas las órdenes de compra
        return null; // Cambiar por la implementación real
    }
}