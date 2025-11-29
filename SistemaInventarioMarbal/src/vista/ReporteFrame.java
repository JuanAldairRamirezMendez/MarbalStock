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
        //panel
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(0, 123, 255));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        JLabel lblTitulo = new JLabel("Reporte de Ventas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);

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
        tblReporte.getTableHeader().setBackground(new Color(240, 240, 240));
        tblReporte.setGridColor(Color.LIGHT_GRAY);
        tblReporte.setShowHorizontalLines(true);
        tblReporte.setShowVerticalLines(false);

        JScrollPane scroll = new JScrollPane(tblReporte);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //Boton exportar
        JButton btnExport = crearBoton("Exportar CSV");
        btnExport.addActionListener(e -> {
            reporteController.exportarReporte("CSV");
            JOptionPane.showMessageDialog(this, "Exportación solicitada (simulado)");
        });

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBackground(Color.WHITE);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 20));
        panelBoton.add(btnExport);

        //Panel Principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(0, 123, 255));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(180, 36));
        return boton;
    }
}