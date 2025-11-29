package util;

import conexion.ConexionBD;
import controlador.InventarioController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * DBRunner - pequeñas pruebas E2E para asignación y consumo por cliente.
 */
public class DBRunner {
    public static void main(String[] args) {
        System.out.println("DBRunner iniciando...");
        ConexionBD cb = new ConexionBD();
        Connection conn = cb.abrirConexion();
        if (conn == null) {
            System.err.println("No hay conexión a la BD");
            return;
        }

        try {
            Integer productoId = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM producto WHERE codigo = ? LIMIT 1")) {
                ps.setString(1, "P-1000");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) productoId = rs.getInt(1);
                }
            } catch (Exception ex) {
                // fallback a tabla antigua
                try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM productos WHERE nombre = ? LIMIT 1")) {
                    ps.setString(1, "Producto Ejemplo");
                    try (ResultSet rs = ps.executeQuery()) { if (rs.next()) productoId = rs.getInt(1); }
                }
            }

            System.out.println("Producto P-1000 id = " + productoId);

            // consultar asignación
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, asignacion_anual FROM producto_asignacion WHERE cliente_id = ? AND producto_id = ? LIMIT 1")) {
                ps.setInt(1, 1);
                ps.setInt(2, productoId == null ? -1 : productoId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Asignacion encontrada: id=" + rs.getInt("id") + " anual=" + rs.getDouble("asignacion_anual"));
                    } else {
                        System.out.println("No existe asignacion para cliente 1 y producto " + productoId);
                    }
                }
            }

            InventarioController ic = new InventarioController();
            System.out.println("Intentando registrar consumo permitido (2 unidades) para cliente 1...");
            controlador.InventarioController.ValidationResult r1 = ic.registrarConsumoParaCliente(1, productoId == null ? 1 : productoId, 2.0, "Prueba consumo 2", false, "tester");
            System.out.println("Resultado: ok=" + r1.isOk() + " msg=" + r1.getMessage());

            System.out.println("Intentando registrar consumo que excede diario (10 unidades) para cliente 1 (no admin)...");
            controlador.InventarioController.ValidationResult r2 = ic.registrarConsumoParaCliente(1, productoId == null ? 1 : productoId, 10.0, "Prueba consumo 10", false, "tester");
            System.out.println("Resultado: ok=" + r2.isOk() + " msg=" + r2.getMessage());

            System.out.println("Intentando registrar consumo que excede diario (10 unidades) para cliente 1 COMO ADMIN (override)...");
            controlador.InventarioController.ValidationResult r3 = ic.registrarConsumoParaCliente(1, productoId == null ? 1 : productoId, 10.0, "Prueba consumo 10 admin", true, "admin-tester");
            System.out.println("Resultado: ok=" + r3.isOk() + " msg=" + r3.getMessage());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            cb.cerrarConexion();
        }
    }
}
