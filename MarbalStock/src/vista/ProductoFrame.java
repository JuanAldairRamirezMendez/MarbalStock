/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import conexion.ConexionBD;
import modelo.Producto;
import controlador.InventarioController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * ---------------------------------------------------------------
 * CLASE: ProductoFrame
 * INTEGRANTE: Diego García
 * ---------------------------------------------------------------
 * Esta clase muestra y gestiona los productos del inventario.
 * Permite:
 *  - Mostrar productos desde la base de datos (si está activa)
 *  - Agregar, editar y eliminar productos
 *  - Trabajar en modo "memoria" si la BD aún no está disponible
 * Se integra con:
 *  - conexion.ConexionBD  → conexión JDBC (hecha por Juan)
 *  - modelo.Producto      → clase de datos (hecha por Keila)
 *  - controlador.InventarioController → lógica (usada como respaldo)
 * ---------------------------------------------------------------
 */
public class ProductoFrame extends JFrame {

    private JTable tabla;
    private DefaultTableModel model;
    private JButton btnAgregar, btnEditar, btnEliminar, btnRefrescar;
    private InventarioController inventarioController;

    public ProductoFrame() {
        inventarioController = new InventarioController();
        initUI();
        cargarProductos();
    }

    //Inicializa los componentes gráficos de la ventana
    private void initUI() {
        setTitle("Gestión de Productos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Tabla de productos
        model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Tipo", "Stock", "Precio"}, 0) {
            // que no se puedan editar directamente desde la tabla
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(model);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);

        // Botones
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnRefrescar = new JButton("Refrescar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);

        add(panelBotones, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Listeners
        btnRefrescar.addActionListener(e -> cargarProductos());
        btnAgregar.addActionListener(e -> mostrarDialogoAgregar());
        btnEditar.addActionListener(e -> mostrarDialogoEditar());
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());

        // Doble clic para ver/editar
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mostrarDialogoEditar();
                }
            }
        });
    }

    /**
     * Intenta cargar productos desde la BD. Si falla, carga desde
     * InventarioController (memoria).
     */
    private void cargarProductos() {
        model.setRowCount(0); // limpiar
        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                throw new SQLException("ConexionBD devolvió null.");
            }

            String sql = "SELECT id, nombre, tipo, stock, precio FROM producto ORDER BY nombre";
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    Object[] fila = new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getInt("stock"),
                        rs.getDouble("precio")
                    };
                    model.addRow(fila);
                }
                if (!any) {
                    // BD ok pero sin datos
                    System.out.println("BD conectada pero no hay productos en la tabla.");
                }
            }
            // opcional: desconectar (ConexionBD puede manejar pool)
            ConexionBD.desconectar();
        } catch (SQLException ex) {
            // Si hay error de BD, intentamos fallback a memoria para continuar desarrollo
            System.err.println("Error cargando productos desde BD: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar productos desde BD.\nSe usará modo memoria local (temporal).",
                    "Aviso BD", JOptionPane.WARNING_MESSAGE);
            cargarProductosDesdeMemoria();
        } catch (Exception ex) {
            System.err.println("Error inesperado: " + ex.getMessage());
            cargarProductosDesdeMemoria();
        }
    }
    
    /**
     * Carga productos de ejemplo en memoria (mientras no haya BD disponible)
     */
    private void cargarProductosDesdeMemoria() {
        model.setRowCount(0);
        ArrayList<Producto> lista = inventarioController.getListaProductos(); // requiere getListaProductos()
        if (lista == null || lista.isEmpty()) {
            // si no hay nada, crear ejemplo para pruebas
            Producto ejemplo = new Producto(1, "Cemento 50kg", "Contratado", 20, 35.50);
            inventarioController.agregarProductoTemporal(ejemplo);
            lista = inventarioController.getListaProductos();
        }
        for (Producto p : lista) {
            model.addRow(new Object[]{p.getId(), p.getNombre(), p.getTipo(), p.getStock(), p.getPrecio()});
        }
    }

    /**
     * Ventana emergente para agregar producto
     */
    private void mostrarDialogoAgregar() {
        JTextField txtNombre = new JTextField();
        JTextField txtTipo = new JTextField();
        JTextField txtStock = new JTextField();
        JTextField txtPrecio = new JTextField();

        Object[] message = {
            "Nombre:", txtNombre,
            "Tipo (Contratado/Adicional):", txtTipo,
            "Stock:", txtStock,
            "Precio:", txtPrecio
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String tipo = txtTipo.getText().trim();
            int stock;
            double precio;
            try {
                stock = Integer.parseInt(txtStock.getText().trim());
                precio = Double.parseDouble(txtPrecio.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Stock o precio no válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Intentar insertar en BD; si falla, insertar en memoria
            if (!insertarProductoEnBD(nombre, tipo, stock, precio)) {
                // fallback memoria
                int nuevoId = inventarioController.getNextIdTemporal();
                Producto p = new Producto(nuevoId, nombre, tipo, stock, precio);
                inventarioController.agregarProductoTemporal(p);
                JOptionPane.showMessageDialog(this, "Producto agregado en memoria (BD no disponible).");
            } else {
                JOptionPane.showMessageDialog(this, "Producto agregado en BD correctamente.");
            }
            cargarProductos();
        }
    }
    
    /**
     * Ventana emergente para editar producto
     */
    private void mostrarDialogoEditar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para editar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object idObj = model.getValueAt(fila, 0);
        if (idObj == null) {
            return;
        }
        int id = (int) idObj;

        String nombre = (String) model.getValueAt(fila, 1);
        String tipo = (String) model.getValueAt(fila, 2);
        int stock = (int) model.getValueAt(fila, 3);
        double precio = (double) model.getValueAt(fila, 4);

        JTextField txtNombre = new JTextField(nombre);
        JTextField txtTipo = new JTextField(tipo);
        JTextField txtStock = new JTextField(String.valueOf(stock));
        JTextField txtPrecio = new JTextField(String.valueOf(precio));

        Object[] message = {
            "Nombre:", txtNombre,
            "Tipo (Contratado/Adicional):", txtTipo,
            "Stock:", txtStock,
            "Precio:", txtPrecio
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Editar Producto", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int nuevoStock = Integer.parseInt(txtStock.getText().trim());
                double nuevoPrecio = Double.parseDouble(txtPrecio.getText().trim());
                String nuevoNombre = txtNombre.getText().trim();
                String nuevoTipo = txtTipo.getText().trim();

                if (!actualizarProductoEnBD(id, nuevoNombre, nuevoTipo, nuevoStock, nuevoPrecio)) {
                    // fallback memoria
                    boolean ok = inventarioController.editarProductoTemporal(id, nuevoNombre, nuevoTipo, nuevoStock, nuevoPrecio);
                    if (ok) {
                        JOptionPane.showMessageDialog(this, "Producto actualizado en memoria (BD no disponible).");
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró producto en memoria.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Producto actualizado en BD correctamente.");
                }
                cargarProductos();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Stock o precio no válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Ventana emergente para eliminar producto
     */
    private void eliminarProductoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) model.getValueAt(fila, 0);
        int opt = JOptionPane.showConfirmDialog(this, "¿Eliminar producto ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opt != JOptionPane.YES_OPTION) {
            return;
        }

        if (!eliminarProductoEnBD(id)) {
            // fallback memoria
            boolean ok = inventarioController.eliminarProductoTemporal(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Producto eliminado de memoria (BD no disponible).");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró producto en memoria.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Producto eliminado en BD correctamente.");
        }
        cargarProductos();
    }

    // ----- Métodos de BD: insertar, actualizar, eliminar -----
    private boolean insertarProductoEnBD(String nombre, String tipo, int stock, double precio) {
        try (Connection conn = ConexionBD.conectar()) {
            if (conn == null) {
                throw new SQLException("ConexionBD devolvió null.");
            }
            String sql = "INSERT INTO producto (nombre, tipo, stock, precio) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setString(2, tipo);
                ps.setInt(3, stock);
                ps.setDouble(4, precio);
                ps.executeUpdate();
            }
            ConexionBD.desconectar();
            return true;
        } catch (SQLException ex) {
            System.err.println("Error insertarProductoEnBD: " + ex.getMessage());
            return false;
        }
    }

    private boolean actualizarProductoEnBD(int id, String nombre, String tipo, int stock, double precio) {
        try (Connection conn = ConexionBD.conectar()) {
            if (conn == null) {
                throw new SQLException("ConexionBD devolvió null.");
            }
            String sql = "UPDATE producto SET nombre=?, tipo=?, stock=?, precio=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setString(2, tipo);
                ps.setInt(3, stock);
                ps.setDouble(4, precio);
                ps.setInt(5, id);
                int rows = ps.executeUpdate();
                ConexionBD.desconectar();
                return rows > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error actualizarProductoEnBD: " + ex.getMessage());
            return false;
        }
    }

    private boolean eliminarProductoEnBD(int id) {
        try (Connection conn = ConexionBD.conectar()) {
            if (conn == null) {
                throw new SQLException("ConexionBD devolvió null.");
            }
            String sql = "DELETE FROM producto WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                ConexionBD.desconectar();
                return rows > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error eliminarProductoEnBD: " + ex.getMessage());
            return false;
        }
    }
}
