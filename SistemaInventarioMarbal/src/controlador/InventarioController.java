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
        // Preferimos la tabla `producto` (singular) creada por el script de migración.
        // Si no existe, intentamos usar `productos` (retrocompatibilidad).
        String sql = "SELECT id, nombre, precio, stock, tipo FROM producto";
        Connection conn = conexionBD.abrirConexion();
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
            } catch (SQLException ex) {
                // Si la tabla `producto` no existe, caemos a la versión plural `productos`.
                sql = "SELECT id, nombre, precio, stock, tipo, consumo_mensual, alerta_threshold FROM productos";
                if (ps != null) try { ps.close(); } catch (Exception ignore) {}
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
            }
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                double stockDec = rs.getDouble("stock");
                String tipo = rs.getString("tipo");
                // columnas consumo_mensual y alerta_threshold pueden no existir en la nueva tabla
                int consumoMensual = 0; int alerta = 0;
                try { consumoMensual = rs.getInt("consumo_mensual"); } catch (Exception ignore) {}
                try { alerta = rs.getInt("alerta_threshold"); } catch (Exception ignore) {}
                int cantidad = (int) Math.round(stockDec);
                // Mapear a fields del modelo Producto; algunos campos pueden quedar por defecto
                Producto p = new Producto(id, "", nombre, tipo, cantidad, 5, 0.0, precio, 0);
                productos.add(p);
            }
            if (rs != null) try { rs.close(); } catch (Exception ignore) {}
            if (ps != null) try { ps.close(); } catch (Exception ignore) {}
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productos;
    }

    public Producto obtenerProductoPorId(int idBusqueda) {
        String sql = "SELECT id, nombre, precio, stock, tipo FROM producto WHERE id = ?";
        Connection conn = conexionBD.abrirConexion();
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, idBusqueda);
                rs = ps.executeQuery();
            } catch (SQLException ex) {
                // fallback a tabla plural
                sql = "SELECT id, nombre, precio, stock, tipo FROM productos WHERE id = ?";
                if (ps != null) try { ps.close(); } catch (Exception ignore) {}
                ps = conn.prepareStatement(sql);
                ps.setInt(1, idBusqueda);
                rs = ps.executeQuery();
            }
            if (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                double stockDec = rs.getDouble("stock");
                String tipo = rs.getString("tipo");
                int cantidad = (int) Math.round(stockDec);
                return new Producto(id, "", nombre, tipo, cantidad, 5, 0.0, precio, 0);
            }
            if (rs != null) try { rs.close(); } catch (Exception ignore) {}
            if (ps != null) try { ps.close(); } catch (Exception ignore) {}
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean agregarProducto(Producto producto) {
        // Insertar en `producto` si existe, sino en `productos` con columnas antiguas
        String sql = "INSERT INTO producto (nombre, precio, stock, tipo) VALUES (?, ?, ?, ?)";
        Connection conn = conexionBD.abrirConexion();
        try {
            PreparedStatement ps = null;
            try {
                ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, producto.getNombre());
                ps.setDouble(2, producto.getPrecioVenta());
                ps.setDouble(3, producto.getStock());
                ps.setString(4, producto.getTipo());
                int affected = ps.executeUpdate();
                if (affected > 0) {
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) { producto.setId(keys.getInt(1)); }
                    }
                    registrarMovimiento(new MovimientoInventario(producto.getId(), producto.getStock(), "INGRESO", "Ingreso inicial"));
                    return true;
                }
            } catch (SQLException ex) {
                // fallback: tabla antigua 'productos' con columnas consumo_mensual y alerta_threshold
                try { if (ps != null) ps.close(); } catch (Exception ignore) {}
                sql = "INSERT INTO productos (nombre, precio, stock, tipo, consumo_mensual, alerta_threshold) VALUES (?, ?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, producto.getNombre());
                ps.setDouble(2, producto.getPrecioVenta());
                ps.setDouble(3, producto.getStock());
                ps.setString(4, producto.getTipo());
                ps.setInt(5, producto.getStockMinimo());
                ps.setInt(6, producto.getStockMinimo());
                int affected = ps.executeUpdate();
                if (affected > 0) {
                    try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) producto.setId(keys.getInt(1)); }
                    registrarMovimiento(new MovimientoInventario(producto.getId(), producto.getStock(), "INGRESO", "Ingreso inicial"));
                    return true;
                }
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
        String sql = "DELETE FROM producto WHERE id = ?";
        Connection conn = conexionBD.abrirConexion();
        try {
            PreparedStatement ps = null;
            try {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                int affected = ps.executeUpdate();
                return affected > 0;
            } catch (SQLException ex) {
                // fallback a tabla antigua
                try { if (ps != null) ps.close(); } catch (Exception ignore) {}
                sql = "DELETE FROM productos WHERE id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                int affected = ps.executeUpdate();
                return affected > 0;
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    public boolean modificarProducto(Producto productoModificado) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, stock = ?, tipo = ? WHERE id = ?";
        Connection conn = conexionBD.abrirConexion();
        try {
            PreparedStatement ps = null;
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1, productoModificado.getNombre());
                ps.setDouble(2, productoModificado.getPrecioVenta());
                ps.setDouble(3, productoModificado.getStock());
                ps.setString(4, productoModificado.getTipo());
                ps.setInt(5, productoModificado.getId());
                int affected = ps.executeUpdate();
                if (affected > 0) {
                    registrarMovimiento(new MovimientoInventario(productoModificado.getId(), productoModificado.getStock(), "AJUSTE", "Modificación de producto"));
                    if (!productoModificado.validarStock()) checkStockAndGenerarOrden(productoModificado);
                    return true;
                }
            } catch (SQLException ex) {
                // fallback a tabla antigua
                try { if (ps != null) ps.close(); } catch (Exception ignore) {}
                sql = "UPDATE productos SET nombre = ?, precio = ?, stock = ?, tipo = ? WHERE id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, productoModificado.getNombre());
                ps.setDouble(2, productoModificado.getPrecioVenta());
                ps.setDouble(3, productoModificado.getStock());
                ps.setString(4, productoModificado.getTipo());
                ps.setInt(5, productoModificado.getId());
                int affected = ps.executeUpdate();
                if (affected > 0) {
                    registrarMovimiento(new MovimientoInventario(productoModificado.getId(), productoModificado.getStock(), "AJUSTE", "Modificación de producto"));
                    if (!productoModificado.validarStock()) checkStockAndGenerarOrden(productoModificado);
                    return true;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    /** Registra un movimiento en la tabla movimiento_inventario */
    public boolean registrarMovimiento(MovimientoInventario movimiento) {
        String insertSql = "INSERT INTO movimiento_inventario (producto_id, cantidad, tipo, descripcion) VALUES (?, ?, ?, ?)";
        // Actualizar preferiblemente la tabla `producto`, si falla usar `productos` por compatibilidad
        String updateStockSql = "UPDATE producto SET stock = stock + ? WHERE id = ?"; // sumará o restará según signo
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
                    PreparedStatement psUpd = null;
                    try {
                        psUpd = conn.prepareStatement(updateStockSql);
                        psUpd.setInt(1, delta);
                        psUpd.setInt(2, movimiento.getProductoId());
                        psUpd.executeUpdate();
                    } catch (SQLException ex) {
                        // fallback a tabla antigua 'productos'
                        try { if (psUpd != null) psUpd.close(); } catch (Exception ignore) {}
                        psUpd = conn.prepareStatement("UPDATE productos SET stock = stock + ? WHERE id = ?");
                        psUpd.setInt(1, delta);
                        psUpd.setInt(2, movimiento.getProductoId());
                        psUpd.executeUpdate();
                    } finally {
                        try { if (psUpd != null) psUpd.close(); } catch (Exception ignore) {}
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

    /* ----------------- NUEVAS FUNCIONES PARA REPARTO POR CLIENTE ----------------- */
    /** Recupera la asignación anual para un cliente y producto */
    public modelo.ProductoAsignacion obtenerAsignacion(int clienteId, int productoId) {
        // Seleccionamos solo las columnas que garantizamos que existen en la migración
        String sql = "SELECT id, cliente_id, producto_id, asignacion_anual FROM producto_asignacion WHERE cliente_id = ? AND producto_id = ? LIMIT 1";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setInt(2, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    double asign = rs.getDouble("asignacion_anual");
                    // unidad y activo no siempre estarán presentes; usar valores por defecto
                    String unidad = "";
                    boolean activo = true;
                    return new modelo.ProductoAsignacion(id, clienteId, productoId, asign, unidad, activo);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /** Lista todas las asignaciones en la BD (limitado a 1000) */
    public java.util.ArrayList<modelo.ProductoAsignacion> listarAsignaciones() {
        java.util.ArrayList<modelo.ProductoAsignacion> lista = new java.util.ArrayList<>();
        String sql = "SELECT id, cliente_id, producto_id, asignacion_anual FROM producto_asignacion ORDER BY id DESC LIMIT 1000";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int clienteId = rs.getInt("cliente_id");
                int productoId = rs.getInt("producto_id");
                double asign = rs.getDouble("asignacion_anual");
                lista.add(new modelo.ProductoAsignacion(id, clienteId, productoId, asign, "", true));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }

    /** Crea una nueva asignación */
    public boolean crearAsignacion(modelo.ProductoAsignacion a) {
        String sql = "INSERT INTO producto_asignacion (cliente_id, producto_id, asignacion_anual) VALUES (?, ?, ?)";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getClienteId());
            ps.setInt(2, a.getProductoId());
            ps.setDouble(3, a.getAsignacionAnual());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) { /* opcional: set id */ } }
                return true;
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    /** Actualiza una asignación existente */
    public boolean actualizarAsignacion(modelo.ProductoAsignacion a) {
        String sql = "UPDATE producto_asignacion SET asignacion_anual = ? WHERE id = ?";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, a.getAsignacionAnual());
            ps.setInt(2, a.getId());
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    /** Elimina una asignación por id */
    public boolean eliminarAsignacion(int id) {
        String sql = "DELETE FROM producto_asignacion WHERE id = ?";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    /** Suma el consumo (SALIDA) de un cliente sobre un producto dentro de un periodo */
    public double sumarConsumoPeriodo(int clienteId, int productoId, java.time.LocalDateTime inicio, java.time.LocalDateTime fin) {
        String sql = "SELECT COALESCE(SUM(cantidad),0) AS total FROM movimiento_inventario WHERE producto_id = ? AND cliente_id = ? AND tipo = 'SALIDA' AND fecha >= ? AND fecha <= ?";
        Connection conn = conexionBD.abrirConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productoId);
            ps.setInt(2, clienteId);
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(inicio));
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("total");
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return 0.0;
    }

    /** Comprueba si un cliente puede consumir cierta cantidad de un producto según la asignación y reglas. */
    public ValidationResult puedeConsumir(int clienteId, int productoId, double cantidadSolicitada, boolean esAdmin) {
        modelo.ProductoAsignacion asign = obtenerAsignacion(clienteId, productoId);
        if (asign == null) return new ValidationResult(false, "No existe asignación para este cliente y producto.");

        java.time.LocalDate hoy = java.time.LocalDate.now();
        java.time.LocalDateTime inicioDia = hoy.atStartOfDay();
        java.time.LocalDateTime finDia = hoy.plusDays(1).atStartOfDay().minusSeconds(1);
        java.time.LocalDate inicioMesDate = hoy.withDayOfMonth(1);
        java.time.LocalDateTime inicioMes = inicioMesDate.atStartOfDay();
        java.time.LocalDateTime finMes = inicioMesDate.plusMonths(1).atStartOfDay().minusSeconds(1);

        double consumidoHoy = sumarConsumoPeriodo(clienteId, productoId, inicioDia, finDia);
        double consumidoMes = sumarConsumoPeriodo(clienteId, productoId, inicioMes, finMes);

        double maxMensual = asign.getMaxMensual();
        int diasMes = hoy.lengthOfMonth();
        double maxDiario = asign.getMaxDiario(diasMes);

        // Umbral de alerta: 1 unidad (en la unidad del producto)
        double umbral = 1.0;

        // Si excede diario
        if (consumidoHoy + cantidadSolicitada > maxDiario) {
            if (esAdmin) {
                return new ValidationResult(true, "Excede el máximo diario pero usuario es admin: se permitirá con registro de excepción.");
            }
            return new ValidationResult(false, String.format("No puede consumir %.3f (excede máximo diario %.3f). Consumido hoy: %.3f", cantidadSolicitada, maxDiario, consumidoHoy));
        }

        // Si se acerca al umbral mensual o diario, generar alerta (no bloqueante)
        double restanteMes = maxMensual - consumidoMes - cantidadSolicitada;
        double restanteDia = maxDiario - consumidoHoy - cantidadSolicitada;
        if (restanteMes <= umbral || restanteDia <= umbral) {
            crearAlertaReparto(clienteId, productoId, "CERCANO", String.format("Restante mensual %.3f, restante diario %.3f", restanteMes, restanteDia), "sistema");
        }

        return new ValidationResult(true, "OK");
    }

    private void crearAlertaReparto(int clienteId, int productoId, String tipo, String mensaje, String creadoPor) {
        // Usar una conexión nueva y separada para las alertas para no interferir con
        // la transacción principal que pudo haber sido usada por el llamador.
        ConexionBD cb = new ConexionBD();
        Connection conn = cb.abrirConexion();
        String sql = "INSERT INTO alertas_reparto (cliente_id, producto_id, tipo, mensaje, creado_por) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setInt(2, productoId);
            ps.setString(3, tipo);
            ps.setString(4, mensaje);
            ps.setString(5, creadoPor);
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            // intentar sin creado_por en la misma conexión nueva
            try {
                String sql2 = "INSERT INTO alertas_reparto (cliente_id, producto_id, tipo, mensaje) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                    ps2.setInt(1, clienteId);
                    ps2.setInt(2, productoId);
                    ps2.setString(3, tipo);
                    ps2.setString(4, mensaje);
                    ps2.executeUpdate();
                }
            } catch (SQLException ex2) {
                ex2.printStackTrace();
            }
        } finally {
            cb.cerrarConexion();
        }
    }

    /** Registra un consumo (SALIDA) asociado a un cliente, validando límites. */
    public ValidationResult registrarConsumoParaCliente(int clienteId, int productoId, double cantidadSolicitada, String descripcion, boolean esAdmin, String usuario) {
        ValidationResult v = puedeConsumir(clienteId, productoId, cantidadSolicitada, esAdmin);
        if (!v.isOk()) return v;

        // si es admin y excede, registramos excepción en alerta
        try {
            Connection conn = conexionBD.abrirConexion();
            conn.setAutoCommit(false);
            String ins = "INSERT INTO movimiento_inventario (producto_id, cliente_id, cantidad, tipo, descripcion) VALUES (?, ?, ?, 'SALIDA', ?)";
            try (PreparedStatement ps = conn.prepareStatement(ins)) {
                ps.setInt(1, productoId);
                ps.setInt(2, clienteId);
                ps.setDouble(3, cantidadSolicitada);
                ps.setString(4, descripcion == null ? "" : descripcion);
                ps.executeUpdate();
            }
            String upd = "UPDATE producto SET stock = stock - ? WHERE id = ?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn.prepareStatement(upd);
                ps2.setDouble(1, cantidadSolicitada);
                ps2.setInt(2, productoId);
                ps2.executeUpdate();
            } catch (SQLException ex) {
                try { if (ps2 != null) ps2.close(); } catch (Exception ignore) {}
                ps2 = conn.prepareStatement("UPDATE productos SET stock = stock - ? WHERE id = ?");
                ps2.setDouble(1, cantidadSolicitada);
                ps2.setInt(2, productoId);
                ps2.executeUpdate();
            } finally {
                try { if (ps2 != null) ps2.close(); } catch (Exception ignore) {}
            }
            // Commit de la operación principal antes de crear alertas que usan conexiones separadas
            conn.commit();
            // si v.message indica excepcion por admin, registrar alerta tipo EXCEPCION (fuera de la transacción anterior)
            if (v.getMessage() != null && v.getMessage().toLowerCase().contains("excede") && esAdmin) {
                try {
                    crearAlertaReparto(clienteId, productoId, "EXCEPCION", "Consumo autorizado por admin: " + usuario, usuario);
                } catch (Exception ignore) {}
            }
            return new ValidationResult(true, "Consumo registrado");
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ValidationResult(false, "Error registrando consumo: " + ex.getMessage());
        }
    }

    public static class ValidationResult {
        private boolean ok;
        private String message;
        public ValidationResult(boolean ok, String message) { this.ok = ok; this.message = message; }
        public boolean isOk() { return ok; }
        public String getMessage() { return message; }
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
                MovimientoInventario m = new MovimientoInventario(id, productoId, cantidad, null, tipo, descripcion, fecha);
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