package controlador;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import modelo.MovimientoInventario;
import modelo.Producto;
import modelo.Proveedor;
import modelo.OrdenCompra;

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
    private ConexionBD conexionBD;
    private OrdenCompraController ordenController;

    public InventarioController() {
        this.conexionBD = new ConexionBD();
        this.ordenController = new OrdenCompraController();
    }

    /** Carga todos los productos desde la base de datos a memoria (lista) */
    public ArrayList<Producto> listarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        String sql = "SELECT id, nombre, precio, cantidad FROM productos";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
                // Mapear a fields del modelo Producto; algunos campos pueden quedar por defecto
                Producto p = new Producto(id, "", nombre, "", cantidad, 5, 0.0, precio, 0);
                productos.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productos;
    }

    public Producto obtenerProductoPorId(int idBusqueda) {
        String sql = "SELECT id, nombre, precio, cantidad FROM productos WHERE id = ?";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBusqueda);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    double precio = rs.getDouble("precio");
                    int cantidad = rs.getInt("cantidad");
                    return new Producto(id, "", nombre, "", cantidad, 5, 0.0, precio, 0);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean agregarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, precio, cantidad) VALUES (?, ?, ?)";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecioVenta());
            ps.setInt(3, producto.getStock());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        producto.setId(keys.getInt(1));
                    }
                }
                // registrar movimiento de ingreso inicial
                registrarMovimiento(new MovimientoInventario(producto.getId(), producto.getStock(), "INGRESO", "Ingreso inicial"));
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean eliminarProducto(int id) {
        // Registrar salida por eliminación (opcional)
        Producto p = obtenerProductoPorId(id);
        if (p != null) {
            registrarMovimiento(new MovimientoInventario(id, p.getStock(), "SALIDA", "Producto eliminado"));
        }
        String sql = "DELETE FROM productos WHERE id = ?";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean modificarProducto(Producto productoModificado) {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, cantidad = ? WHERE id = ?";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productoModificado.getNombre());
            ps.setDouble(2, productoModificado.getPrecioVenta());
            ps.setInt(3, productoModificado.getStock());
            ps.setInt(4, productoModificado.getId());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                registrarMovimiento(new MovimientoInventario(productoModificado.getId(), productoModificado.getStock(), "AJUSTE", "Modificación de producto"));
                // verificar stock mínimo y generar orden si corresponde
                if (!productoModificado.validarStock()) {
                    checkStockAndGenerarOrden(productoModificado);
                }
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /** Registra un movimiento en la tabla movimiento_inventario */
    public boolean registrarMovimiento(MovimientoInventario movimiento) {
        String sql = "INSERT INTO movimiento_inventario (producto_id, cantidad, tipo, descripcion) VALUES (?, ?, ?, ?)";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, movimiento.getProductoId());
            ps.setInt(2, movimiento.getCantidad());
            ps.setString(3, movimiento.getTipo());
            ps.setString(4, movimiento.getDescripcion());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) movimiento.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /** Si el producto está por debajo del stock mínimo, genera una orden automática */
    private void checkStockAndGenerarOrden(Producto producto) {
        // Cantidad a pedir: ejemplo simple = stockMinimo * 2 - stockActual
        int cantidadAOrdenar = producto.getStockMinimo() * 2 - producto.getStock();
        if (cantidadAOrdenar <= 0) return;

        // Buscar proveedor del producto (si existe). Aquí usamos idProveedor del modelo.
        Proveedor prov = new Proveedor(producto.getIdProveedor(), "PROVEEDOR-DESCONOCIDO", "-");
        // Crear orden y delegar al controlador de órdenes
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        OrdenCompra orden = new OrdenCompra(0, fecha, prov);
        // Podríamos adjuntar producto/cantidad en una tabla detalle de orden; por simplicidad, almacenamos la orden y notificamos
        ordenController.agregarOrdenCompra(orden);
    }
}