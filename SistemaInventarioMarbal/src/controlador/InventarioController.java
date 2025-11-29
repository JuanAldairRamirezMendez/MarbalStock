package controlador;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.CallableStatement;
import java.sql.Timestamp;
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
        String sql = "SELECT id, nombre, precio, stock, tipo, consumo_mensual, alerta_threshold FROM productos";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                double stockDec = rs.getDouble("stock");
                String tipo = rs.getString("tipo");
                int consumoMensual = rs.getInt("consumo_mensual");
                int alerta = rs.getInt("alerta_threshold");
                int cantidad = (int) Math.round(stockDec);
                // Mapear a fields del modelo Producto; algunos campos pueden quedar por defecto
                Producto p = new Producto(id, "", nombre, tipo, cantidad, 5, 0.0, precio, 0);
                productos.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productos;
    }

    public Producto obtenerProductoPorId(int idBusqueda) {
        String sql = "SELECT id, nombre, precio, stock, tipo FROM productos WHERE id = ?";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBusqueda);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    double precio = rs.getDouble("precio");
                    double stockDec = rs.getDouble("stock");
                    String tipo = rs.getString("tipo");
                    int cantidad = (int) Math.round(stockDec);
                    return new Producto(id, "", nombre, tipo, cantidad, 5, 0.0, precio, 0);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean agregarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, precio, stock, tipo, consumo_mensual, alerta_threshold) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecioVenta());
            ps.setDouble(3, producto.getStock());
            ps.setString(4, producto.getTipo());
            ps.setInt(5, producto.getStockMinimo());
            ps.setInt(6, producto.getStockMinimo());
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
        String sql = "UPDATE productos SET nombre = ?, precio = ?, stock = ?, tipo = ? WHERE id = ?";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productoModificado.getNombre());
            ps.setDouble(2, productoModificado.getPrecioVenta());
            ps.setDouble(3, productoModificado.getStock());
            ps.setString(4, productoModificado.getTipo());
            ps.setInt(5, productoModificado.getId());
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
        String insertSql = "INSERT INTO movimiento_inventario (producto_id, cantidad, tipo, descripcion) VALUES (?, ?, ?, ?)";
        String updateStockSql = "UPDATE productos SET stock = stock + ? WHERE id = ?"; // sumará o restará según signo
        Connection conn = conexionBD.abrirConexion();
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement psIns = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psIns.setInt(1, movimiento.getProductoId());
                psIns.setInt(2, movimiento.getCantidad());
                psIns.setString(3, movimiento.getTipo());
                psIns.setString(4, movimiento.getDescripcion());
                int affected = psIns.executeUpdate();
                if (affected > 0) {
                    try (ResultSet keys = psIns.getGeneratedKeys()) {
                        if (keys.next()) movimiento.setId(keys.getInt(1));
                    }
                    // preparar actualización de stock
                    int delta = movimiento.getCantidad();
                    if ("SALIDA".equalsIgnoreCase(movimiento.getTipo())) delta = -Math.abs(delta);
                    else if ("INGRESO".equalsIgnoreCase(movimiento.getTipo())) delta = Math.abs(delta);
                    else if ("AJUSTE".equalsIgnoreCase(movimiento.getTipo())) {
                        // Para ajuste tomamos la cantidad tal cual (puede ser positivo o negativo)
                        // aquí asumimos movimiento.cantidad ya tiene signo
                    }
                    try (PreparedStatement psUpd = conn.prepareStatement(updateStockSql)) {
                        psUpd.setInt(1, delta);
                        psUpd.setInt(2, movimiento.getProductoId());
                        psUpd.executeUpdate();
                    }
                    conn.commit();
                    return true;
                }
            }
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try { conn.rollback(); } catch (SQLException ignore) {}
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignore) {}
        }
        return false;
    }

    /**
     * Llama al stored procedure `registrar_consumo(producto_id, cantidad, descripcion)`
     * implementado en la base de datos. El procedure debe encargarse de validar
     * y actualizar stock y alertas. Retorna true si se ejecutó correctamente.
     */
    public boolean registrarConsumoSP(int productoId, int cantidad, String descripcion) {
        String call = "CALL registrar_consumo(?, ?, ?)";
        Connection conn = conexionBD.abrirConexion();
        try (CallableStatement cs = conn.prepareCall(call)) {
            cs.setInt(1, productoId);
            cs.setInt(2, cantidad);
            cs.setString(3, descripcion == null ? "" : descripcion);
            boolean hadResults = cs.execute();
            // asumimos que la ejecución sin excepción indica éxito
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Recupera la lista de movimientos desde la base de datos.
     */
    public ArrayList<MovimientoInventario> listarMovimientos() {
        ArrayList<MovimientoInventario> movs = new ArrayList<>();
        String sql = "SELECT id, producto_id, cantidad, tipo, descripcion, fecha FROM movimiento_inventario ORDER BY fecha DESC";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int productoId = rs.getInt("producto_id");
                int cantidad = rs.getInt("cantidad");
                String tipo = rs.getString("tipo");
                String descripcion = rs.getString("descripcion");
                Timestamp ts = rs.getTimestamp("fecha");
                LocalDateTime fecha = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();
                MovimientoInventario m = new MovimientoInventario(id, productoId, cantidad, tipo, descripcion, fecha);
                movs.add(m);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return movs;
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