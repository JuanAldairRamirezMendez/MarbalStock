package vista;

import controlador.InventarioController;
import modelo.MovimientoInventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class MovimientosFrame extends JFrame {
    private InventarioController inventarioController;
    private JTable tbl;
    private DefaultTableModel model;

    public MovimientosFrame(InventarioController inventarioController) {
        this.inventarioController = inventarioController;
        setTitle("Movimientos de Inventario");
        setSize(800, 400);
        setLocationRelativeTo(null);
        initialize();
    }

    private void initialize() {
        model = new DefaultTableModel(new String[]{"ID","Producto ID","Cantidad","Tipo","Descripción","Fecha"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tbl = new JTable(model);
        JScrollPane scroll = new JScrollPane(tbl);

        JButton btnRef = new JButton("Refrescar");
        btnRef.addActionListener(e -> cargarMovimientos());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.add(btnRef);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);

        cargarMovimientos();
    }

    private void cargarMovimientos() {
        model.setRowCount(0);
        ArrayList<MovimientoInventario> movs = inventarioController.listarMovimientos();
        for (MovimientoInventario m : movs) {
            model.addRow(new Object[]{m.getId(), m.getProductoId(), m.getCantidad(), m.getTipo(), m.getDescripcion(), m.getFecha()});
        }
    }
}
