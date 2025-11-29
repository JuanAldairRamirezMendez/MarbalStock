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
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        initialize();
    }

    private void initialize() {
        // Encabezado
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(UIConstants.HEADER);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel lblTitulo = new JLabel("MARBAL - REPORTE DE INVENTARIO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);

        // Botón imprimir a la derecha
        JButton btnImprimir = UIFactory.createRoundedButton("Imprimir", UIConstants.PRIMARY, Color.WHITE, 120, 32);
        btnImprimir.addActionListener(e -> JOptionPane.showMessageDialog(this, "Impresión (simulada)"));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(btnImprimir);
        panelTitulo.add(right, BorderLayout.EAST);

        //tabla
        String[] cols = reporteController.getColumnNamesVentaDetalle();
        Object[][] data = reporteController.listarVentasDetalle();
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tblReporte = new JTable(model);
        tblReporte.setRowHeight(24);
        tblReporte.setFont(new Font("Arial", Font.PLAIN, 13));
        tblReporte.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblReporte.getTableHeader().setBackground(UIConstants.TABLE_HEADER_BG);
        tblReporte.setGridColor(Color.LIGHT_GRAY);
        tblReporte.setShowHorizontalLines(true);
        tblReporte.setShowVerticalLines(false);

        JScrollPane scroll = new JScrollPane(tblReporte);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //Boton exportar
        JButton btnExport = UIFactory.createRoundedButton("Exportar CSV", UIConstants.PRIMARY, Color.WHITE, 150, 34);
        btnExport.addActionListener(e -> {
            reporteController.exportarReporte("CSV");
            JOptionPane.showMessageDialog(this, "Exportación solicitada (simulado)");
        });

        // Pie con líneas para firma
        JPanel firmaPanel = new JPanel(new GridLayout(2, 1));
        firmaPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        firmaPanel.setBackground(Color.WHITE);
        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        line1.add(new JLabel("Firma: _____________________________"));
        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        line2.add(new JLabel("Nombre: _____________________________"));
        firmaPanel.add(line1);
        firmaPanel.add(line2);

        //Panel Principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);
        panelPrincipal.add(firmaPanel, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JButton crearBoton(String texto) {
        return UIFactory.createRoundedButton(texto, UIConstants.PRIMARY, Color.WHITE, 180, 36);
    }
}