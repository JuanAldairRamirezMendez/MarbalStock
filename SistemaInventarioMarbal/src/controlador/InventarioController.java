package controlador;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Producto;

/**
 * InventarioController - Controlador para gestión del inventario
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Diego García (U23247615)
 * ROL: Líder de integración / Coordinador
 * 
 * RESPONSABILIDADES DE DIEGO:
 * - Integrar Modelo, Vista y Controlador
 * - Asegurar funcionalidad de botones y eventos
 * - Probar conexión a BD (hecha por Juan)
 * - Realizar pruebas finales del sistema
 * 
 * DESCRIPCIÓN:
 * Este controlador gestiona las operaciones CRUD sobre productos del
 * inventario, implementando la lógica de negocio para:
 * - Registrar consumo diario de productos (RF01)
 * - Editar o eliminar registros de productos (RF08)
 * - Controlar stock y generar alertas (RF02)
 * 
 * PATRÓN DE DISEÑO: MVC (Model-View-Controller)
 * - Modelo: Producto.java (Keila Mateo)
 * - Vista: ProductoFrame.java (Diego García)
 * - Controlador: InventarioController.java (Diego García)
 * 
 * INTEGRACIÓN:
 * Este controlador se conecta con:
 * - ConexionBD.java (Juan Ramírez) para persistencia de datos
 * - ProductoFrame.java (Diego García) para interfaz gráfica
 * - OrdenCompraController.java (Erick Estrada) para órdenes automáticas
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF01: Registrar consumo diario de productos
 * - RF02: Generar alerta de stock bajo
 * - RF08: Editar o eliminar registros de productos
 * 
 * FECHA: Octubre 2025
 * 
 * @author Diego García
 * @version 1.0
 */
public class InventarioController {
    private static final Logger LOG = Logger.getLogger(InventarioController.class.getName());
    private final ConexionBD conexionBD = new ConexionBD();

    public boolean agregarProducto(Producto p) {
        String sql = "INSERT INTO producto (id_producto, nombre, precio, stock) VALUES (?, ?, ?, ?)"
                + " ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), precio=VALUES(precio), stock=VALUES(stock)";
        try (Connection c = conexionBD.abrirConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setString(2, p.getNombre());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al insertar/actualizar producto", e);
            return false;
        }
    }

    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try (Connection c = conexionBD.abrirConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al eliminar producto", e);
            return false;
        }
    }

    public boolean modificarProducto(Producto p) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, stock = ? WHERE id_producto = ?";
        try (Connection c = conexionBD.abrirConexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getStock());
            ps.setInt(4, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al modificar producto", e);
            return false;
        }
    }

    public ArrayList<Producto> listarProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, precio, stock FROM producto ORDER BY nombre";
        try (Connection c = conexionBD.abrirConexion(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_producto");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");
                lista.add(new Producto(id, nombre, precio, stock));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al listar productos", e);
        }
        return lista;
    }
}