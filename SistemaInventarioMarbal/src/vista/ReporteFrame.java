package vista;

import controlador.ReporteController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ReporteFrame extends JFrame {

    // Asegurarse de tener estos imports para evitar "cannot be resolved to a type"
    // import java.util.List;
    // import java.util.Map;

    private JComboBox<String> cmbTipoReporte;
    private JButton btnGenerar;
    private JButton btnExportar;
    private JTable tblReporte;
    private DefaultTableModel tableModel;
    
    private final ReporteController controller;
    
    private List<Map<String, Object>> reporteDataActual; 
    private String nombreReporteActual = "ReporteGenerado";

    private final String[] TIPOS_REPORTE = {
        "Seleccione un Reporte",
        "1. Reporte de Inventario",
        "2. Reporte de Ventas por Período",
        "3. Reporte de Órdenes de Compra",
        "4. Reporte de Consumo Diario (Inviable)" 
    };

    public ReporteFrame(ReporteController controller) {
        this.controller = controller;
        initComponents();
        setupListeners();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Sistema Inventario Marbal - Reportes");
        this.setSize(850, 650);
        this.setLocationRelativeTo(null); 
    }

    private void initComponents() {
        // [CÓDIGO DE INICIALIZACIÓN DE COMPONENTES SIMILAR AL DEL ESTILO LOGIN]
        // ... (Se asume que la configuración de estilos Azul/Verde se mantiene) ...

        setLayout(new BorderLayout());

        // --- Panel de Cabecera (Título Azul) ---
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBackground(new Color(0, 150, 255)); 
        
        JLabel lblTitulo = new JLabel("GENERACIÓN DE REPORTES", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0)); 
        panelCabecera.add(lblTitulo, BorderLayout.CENTER);

        add(panelCabecera, BorderLayout.NORTH);

        // --- Panel Principal (Controles y Tabla) ---
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // --- Panel de Controles (Contiene el ComboBox y Botones) ---
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        panelControles.add(new JLabel("Seleccionar Reporte:"));
        
        cmbTipoReporte = new JComboBox<>(TIPOS_REPORTE);
        cmbTipoReporte.setPreferredSize(new Dimension(280, 30));
        panelControles.add(cmbTipoReporte);

        btnGenerar = new JButton("GENERAR");
        btnGenerar.setBackground(new Color(46, 204, 113)); 
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFont(new Font("Arial", Font.BOLD, 12));
        btnGenerar.setPreferredSize(new Dimension(120, 30));
        panelControles.add(btnGenerar);
        
    btnExportar = new JButton("EXPORTAR");
    btnExportar.setBackground(new Color(46, 204, 113)); 
    btnExportar.setForeground(Color.WHITE);
    btnExportar.setFont(new Font("Arial", Font.BOLD, 12));
    btnExportar.setPreferredSize(new Dimension(150, 30));
    btnExportar.setEnabled(false);
    panelControles.add(btnExportar);
        
        panelPrincipal.add(panelControles, BorderLayout.NORTH);

        // --- JTable (Datos de Reporte) ---
        tableModel = new DefaultTableModel(new Object[]{"Columna 1", "Columna 2", "Columna 3"}, 0);
        tblReporte = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblReporte);
        
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private void setupListeners() {
    btnGenerar.addActionListener(e -> generarReporteSeleccionado());
    btnExportar.addActionListener(e -> exportarReporteActual());
    }

    private void generarReporteSeleccionado() {
        int index = cmbTipoReporte.getSelectedIndex();
        reporteDataActual = null; 
        
        try {
            switch (index) {
                case 1: // 1. Reporte de Inventario (Necesita umbral)
                    // Pide un valor de stock crítico, por ejemplo, 10
                    String sUmbral = JOptionPane.showInputDialog(this, 
                        "Ingrese el Umbral Crítico de Stock (ej. 10):", "Inventario", JOptionPane.QUESTION_MESSAGE);
                    if (sUmbral == null) return;
                    int umbral = Integer.parseInt(sUmbral);

                    reporteDataActual = controller.generarReporteInventario(umbral); 
                    nombreReporteActual = "Inventario";
                    break;

                case 2: // 2. Reporte de Ventas por Período (Necesita fechas)
                    String fechaInicio = JOptionPane.showInputDialog(this, 
                        "Ingrese Fecha de Inicio (YYYY-MM-DD):", "Filtro de Ventas", JOptionPane.QUESTION_MESSAGE);
                    if (fechaInicio == null) return;
                    String fechaFin = JOptionPane.showInputDialog(this, 
                        "Ingrese Fecha de Fin (YYYY-MM-DD):", "Filtro de Ventas", JOptionPane.QUESTION_MESSAGE);
                    if (fechaFin == null) return;
                    
                    reporteDataActual = controller.generarReporteVentasPorPeriodo(fechaInicio, fechaFin); 
                    nombreReporteActual = "VentasTotales";
                    break;
                
                case 3: // 3. Reporte de Órdenes de Compra (SIN PARÁMETRO)
                    // Llamada al método SIN PARÁMETROS para coincidir con el Controller/Modelo
                    reporteDataActual = controller.generarReporteOrdenesCompra(); 
                    nombreReporteActual = "OrdenesCompra";
                    break;
                
                case 4: // 4. Reporte de Consumo Diario (Inviable)
                    reporteDataActual = controller.generarReporteConsumoDiario(); 
                    // El método en el modelo ya manejará la advertencia de inviabilidad
                    nombreReporteActual = "ConsumoDiario";
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Debe seleccionar un reporte.", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
                    return;
            }
            
            mostrarDatosEnTabla(reporteDataActual);
            btnExportar.setEnabled(reporteDataActual != null && !reporteDataActual.isEmpty());
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: El umbral debe ser un número entero.", "ERROR DE ENTRADA", JOptionPane.ERROR_MESSAGE);
            reporteDataActual = null;
            btnExportar.setEnabled(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            reporteDataActual = null;
            btnExportar.setEnabled(false);
        }
    }

    private void mostrarDatosEnTabla(List<Map<String, Object>> data) {
        tableModel.setRowCount(0); 
        tableModel.setColumnCount(0); 

        if (data == null || data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron datos. Verifique la conexión o el filtro.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
    String[] columns = data.get(0).keySet().toArray(String[]::new);
        tableModel.setColumnIdentifiers(columns);
        
        for (Map<String, Object> row : data) {
            Object[] rowData = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                rowData[i] = row.get(columns[i]);
            }
            tableModel.addRow(rowData);
        }
    }
    
    private void exportarReporteActual() {
        if (reporteDataActual != null && !reporteDataActual.isEmpty()) {
            String[] opciones = {"CSV", "PDF", "XLSX"};
            String formato = (String) JOptionPane.showInputDialog(
                    this,
                    "Seleccione el formato de exportación:",
                    "Exportar Reporte",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );
            if (formato == null) return;
            if ("CSV".equalsIgnoreCase(formato)) {
                controller.exportarReporte(reporteDataActual, nombreReporteActual, "CSV");
                JOptionPane.showMessageDialog(this,
                    "Exportación a CSV realizada. Archivo: " + nombreReporteActual + ".csv",
                    "Exportación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                controller.exportarReporte(reporteDataActual, nombreReporteActual, formato);
                JOptionPane.showMessageDialog(this,
                    "El formato " + formato + " aún no está implementado. Intente con CSV.",
                    "Función no disponible",
                    JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay datos generados para exportar. Genere el reporte primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}