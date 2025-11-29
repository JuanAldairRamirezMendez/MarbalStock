package vista;

import controlador.InventarioController;
import modelo.ProductoAsignacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AsignacionFrame extends JFrame {
    private InventarioController controller;
    private JTable tbl;
    private DefaultTableModel model;

    public AsignacionFrame(InventarioController controller) {
        this.controller = controller;
        setTitle("Asignaciones por Cliente");
        setSize(700, 400);
        setLocationRelativeTo(null);
        initialize();
    }

    private void initialize() {
        model = new DefaultTableModel(new String[] { "ID", "Cliente ID", "Producto ID", "Asign. anual" }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tbl = new JTable(model);
        JScrollPane sp = new JScrollPane(tbl);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnBorrar = new JButton("Borrar");

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.add(btnNuevo); btns.add(btnEditar); btns.add(btnBorrar);

        btnNuevo.addActionListener(e -> onNuevo());
        btnEditar.addActionListener(e -> onEditar());
        btnBorrar.addActionListener(e -> onBorrar());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sp, BorderLayout.CENTER);
        getContentPane().add(btns, BorderLayout.SOUTH);

        cargarAsignaciones();
    }

    private void cargarAsignaciones() {
        model.setRowCount(0);
        java.util.ArrayList<ProductoAsignacion> lista = controller.listarAsignaciones();
        for (ProductoAsignacion a : lista) {
            model.addRow(new Object[] { a.getId(), a.getClienteId(), a.getProductoId(), a.getAsignacionAnual() });
        }
    }

    private void onNuevo() {
        AsignacionDialog dlg = new AsignacionDialog(this);
        dlg.setVisible(true);
        ProductoAsignacion res = dlg.getResultado();
        if (res != null) {
            boolean ok = controller.crearAsignacion(res);
            if (ok) { cargarAsignaciones(); JOptionPane.showMessageDialog(this, "Asignación creada"); }
            else JOptionPane.showMessageDialog(this, "Error creando asignación");
        }
    }

    private void onEditar() {
        int sel = tbl.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Seleccione una asignación"); return; }
        int id = (int) model.getValueAt(sel, 0);
        int clienteId = (int) model.getValueAt(sel, 1);
        int productoId = (int) model.getValueAt(sel, 2);
        double asign = Double.parseDouble(String.valueOf(model.getValueAt(sel, 3)));
        ProductoAsignacion a = new ProductoAsignacion(id, clienteId, productoId, asign, "", true);
        AsignacionDialog dlg = new AsignacionDialog(this);
        dlg.setValores(a);
        dlg.setVisible(true);
        ProductoAsignacion res = dlg.getResultado();
        if (res != null) {
            // mantener id
            ProductoAsignacion updated = new ProductoAsignacion(id, res.getClienteId(), res.getProductoId(), res.getAsignacionAnual(), "", true);
            boolean ok = controller.actualizarAsignacion(updated);
            if (ok) { cargarAsignaciones(); JOptionPane.showMessageDialog(this, "Asignación actualizada"); }
            else JOptionPane.showMessageDialog(this, "Error actualizando asignación");
        }
    }

    private void onBorrar() {
        int sel = tbl.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Seleccione una asignación"); return; }
        int id = (int) model.getValueAt(sel, 0);
        int ok = JOptionPane.showConfirmDialog(this, "¿Eliminar asignación id=" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            boolean res = controller.eliminarAsignacion(id);
            if (res) { cargarAsignaciones(); JOptionPane.showMessageDialog(this, "Eliminada"); }
            else JOptionPane.showMessageDialog(this, "Error al eliminar");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AsignacionFrame f = new AsignacionFrame(new InventarioController());
            f.setVisible(true);
        });
    }
}
