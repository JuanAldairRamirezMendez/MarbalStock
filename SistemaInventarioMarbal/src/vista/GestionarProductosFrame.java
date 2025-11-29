package vista;

import controlador.InventarioController;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionarProductosFrame extends JFrame {
    private InventarioController inventarioController;
    private JTable tbl;
    private DefaultTableModel model;

    public GestionarProductosFrame(InventarioController inventarioController) {
        this.inventarioController = inventarioController;
        setTitle("Sistema de Inventario MARBAL - Gestionar Productos");
        setSize(700, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        String[] cols = new String[]{"ID", "Producto", "Tipo", "Unidad", "Total Mensual", "Stock Actual", "Máx. Diario"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tbl = new JTable(model);
        tbl.setFillsViewportHeight(true);

        JScrollPane sp = new JScrollPane(tbl);

        JButton btnRefrescar = UIFactory.createRoundedButton("Refrescar", UIConstants.PRIMARY, Color.WHITE, 120, 28);
        JButton btnEditar = UIFactory.createRoundedButton("Editar", UIConstants.PRIMARY, Color.WHITE, 120, 28);
        JButton btnEliminar = UIFactory.createRoundedButton("Eliminar", UIConstants.PRIMARY, Color.WHITE, 120, 28);

        btnRefrescar.addActionListener(e -> cargarProductos());
        btnEditar.addActionListener(e -> editarSeleccion());
        btnEliminar.addActionListener(e -> eliminarSeleccion());

        JPanel bottom = new JPanel();
        bottom.setBackground(UIConstants.BACKGROUND);
        bottom.add(btnRefrescar);
        bottom.add(btnEditar);
        bottom.add(btnEliminar);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sp, BorderLayout.CENTER);
        getContentPane().add(bottom, BorderLayout.SOUTH);

        cargarProductos();
    }

    private void cargarProductos() {
        model.setRowCount(0);
        if (inventarioController == null) return;
        List<Producto> list = inventarioController.listarProductos();
        for (Producto p : list) {
            Object[] row = new Object[] {
                    p.getId(),
                    p.getNombre(),
                    p.getTipo(),
                    "-", // unidad no implementada en modelo
                    0,   // total mensual placeholder
                    p.getStock(),
                    0    // max diario placeholder
            };
            model.addRow(row);
        }
    }

    private void editarSeleccion() {
        int r = tbl.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Seleccione un producto para editar"); return; }
        int id = (int) model.getValueAt(r, 0);
        JOptionPane.showMessageDialog(this, "Editar producto id=" + id + " (pendiente de implementar)");
    }

    private void eliminarSeleccion() {
        int r = tbl.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar"); return; }
        int id = (int) model.getValueAt(r, 0);
        int ok = JOptionPane.showConfirmDialog(this, "Eliminar producto id="+id+"?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            // Si inventarioController tiene método para eliminar, usarlo. Si no, solo quitar fila.
            try {
                inventarioController.eliminarProducto(id);
            } catch (Exception ex) {
                // ignore if not implemented
            }
            cargarProductos();
        }
    }
}
