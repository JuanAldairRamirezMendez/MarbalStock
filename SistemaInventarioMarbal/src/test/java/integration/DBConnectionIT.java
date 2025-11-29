package integration;

import conexion.ConexionBD;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class DBConnectionIT {

    @Test
    public void testInsertAndSelectProducto() throws Exception {
        ConexionBD cbd = new ConexionBD();
        Connection conn = cbd.abrirConexion();
        assertNotNull(conn, "La conexión no debe ser nula");

        // Insertar un registro de prueba con nombre único temporal
        String nombre = "PruebaConexionTest_" + System.currentTimeMillis();
        String insertSql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?);";
        try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setBigDecimal(2, new java.math.BigDecimal("1.23"));
            ps.setBigDecimal(3, new java.math.BigDecimal("5.00"));
            int rows = ps.executeUpdate();
            assertEquals(1, rows, "Debe insertarse una fila");
            // obtener id generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    assertTrue(id > 0, "ID generado debe ser mayor que 0");
                }
            }
        }

        // Consultar el registro insertado
        String q = "SELECT id, nombre, precio, stock FROM productos WHERE nombre = ?";
        try (PreparedStatement ps2 = conn.prepareStatement(q)) {
            ps2.setString(1, nombre);
            try (ResultSet rs = ps2.executeQuery()) {
                assertTrue(rs.next(), "Debe encontrarse el registro insertado");
                assertEquals(nombre, rs.getString("nombre"));
            }
        }

        // Limpiar: eliminar el registro de prueba
        try (PreparedStatement del = conn.prepareStatement("DELETE FROM productos WHERE nombre = ?")) {
            del.setString(1, nombre);
            del.executeUpdate();
        }

        cbd.cerrarConexion();
    }
}
