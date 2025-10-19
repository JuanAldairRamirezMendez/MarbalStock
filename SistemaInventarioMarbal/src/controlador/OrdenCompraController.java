package controlador;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.OrdenCompra;
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
    private final ConexionBD conexionBD;

    public OrdenCompraController() {
        this.conexionBD = new ConexionBD();
    }

    public void agregarOrdenCompra(OrdenCompra ordenCompra) {
        String sql = "INSERT INTO ordenes_compra(fecha, proveedor_id) VALUES (?, ?)";
        try (Connection conn = conexionBD.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ordenCompra.getFecha());
            ps.setObject(2, ordenCompra.getProveedor() != null ? ordenCompra.getProveedor().getId() : null);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ordenCompra.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar orden de compra: " + e.getMessage(), e);
        } finally {
            conexionBD.cerrarConexion();
        }
    }

    public void eliminarOrdenCompra(int id) {
        String sql = "DELETE FROM ordenes_compra WHERE id = ?";
        try (Connection conn = conexionBD.abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar orden de compra: " + e.getMessage(), e);
        } finally {
            conexionBD.cerrarConexion();
        }
    }

    public void modificarOrdenCompra(OrdenCompra ordenCompra) {
        String sql = "UPDATE ordenes_compra SET fecha = ?, proveedor_id = ? WHERE id = ?";
        try (Connection conn = conexionBD.abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ordenCompra.getFecha());
            ps.setObject(2, ordenCompra.getProveedor() != null ? ordenCompra.getProveedor().getId() : null);
            ps.setInt(3, ordenCompra.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar orden de compra: " + e.getMessage(), e);
        } finally {
            conexionBD.cerrarConexion();
        }
    }

    public OrdenCompra obtenerOrdenCompra(int id) {
        String sql = "SELECT oc.id, oc.fecha, p.id AS pid, p.nombre AS pnombre, p.contacto AS pcontacto "
                + "FROM ordenes_compra oc LEFT JOIN proveedores p ON oc.proveedor_id = p.id WHERE oc.id = ?";
        try (Connection conn = conexionBD.abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Proveedor prov = null;
                    int pid = rs.getInt("pid");
                    if (!rs.wasNull()) {
                        prov = new Proveedor(pid, rs.getString("pnombre"), rs.getString("pcontacto"));
                    }
                    OrdenCompra oc = new OrdenCompra(rs.getInt("id"), rs.getString("fecha"), prov);
                    return oc;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener orden de compra: " + e.getMessage(), e);
        } finally {
            conexionBD.cerrarConexion();
        }
        return null;
    }

    public List<OrdenCompra> listarOrdenesCompra() {
        List<OrdenCompra> lista = new ArrayList<>();
        String sql = "SELECT oc.id, oc.fecha, p.id AS pid, p.nombre AS pnombre, p.contacto AS pcontacto "
                + "FROM ordenes_compra oc LEFT JOIN proveedores p ON oc.proveedor_id = p.id";
        try (Connection conn = conexionBD.abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Proveedor prov = null;
                int pid = rs.getInt("pid");
                if (!rs.wasNull()) {
                    prov = new Proveedor(pid, rs.getString("pnombre"), rs.getString("pcontacto"));
                }
                OrdenCompra oc = new OrdenCompra(rs.getInt("id"), rs.getString("fecha"), prov);
                lista.add(oc);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar ordenes de compra: " + e.getMessage(), e);
        } finally {
            conexionBD.cerrarConexion();
        }
        return lista;
    }
}