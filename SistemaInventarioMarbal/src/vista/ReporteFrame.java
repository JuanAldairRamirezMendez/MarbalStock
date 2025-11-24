package vista;

import controlador.ReporteController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/*
Nuevo: ReporteFrame integrado con ReporteController.
Mostrará "venta_detalle" en JTable (estructura proporcionada por controlador).
*/
public class ReporteFrame extends JFrame {
    private ReporteController reporteController;
    private JTable tblReporte;

    public ReporteFrame(ReporteController reporteController) {
        this.reporteController = reporteController;
        setTitle("Reportes - Detalle de Ventas");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initialize();
    }

    private void initialize() {
        String[] cols = reporteController.getColumnNamesVentaDetalle();
        Object[][] data = reporteController.listarVentasDetalle();
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblReporte = new JTable(model);
        add(new JScrollPane(tblReporte), BorderLayout.CENTER);

        JButton btnExport = new JButton("Exportar (CSV)");
        btnExport.addActionListener(e -> {
            reporteController.exportarReporte("CSV");
            JOptionPane.showMessageDialog(this, "Export solicitado (simulado)");
        });
        add(btnExport, BorderLayout.SOUTH);
    }
}