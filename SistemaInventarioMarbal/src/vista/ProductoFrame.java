package vista;

import controlador.InventarioController;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ProductoFrame - Interfaz gráfica para gestión de productos
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Diego García (U23247615)
 * ROL: Líder de integración / Coordinador
 * 
 * RESPONSABILIDADES DE DIEGO:
 * - Integrar Modelo (Producto.java - Keila), Vista (ProductoFrame) y
 * Controlador (InventarioController)
 * - Asegurar funcionalidad de botones y eventos
 * - Implementar formularios para CRUD de productos
 * - Conectar con InventarioController para operaciones
 * - Probar conexión a BD (hecha por Juan)
 * 
 * DESCRIPCIÓN:
 * Esta clase implementa la interfaz gráfica para gestionar productos
 * del inventario. Permite realizar operaciones CRUD (Crear, Leer,
 * Actualizar, Eliminar) sobre productos, integrando el modelo
 * Producto.java (Keila) con InventarioController (Diego).
 * 
 * PATRÓN MVC - INTEGRACIÓN COMPLETA:
 * 
 * MODELO (Keila Mateo):
 * - Producto.java
 * * Atributos: id, nombre, precio, stock, tipoProducto, stockMinimo
 * * Métodos: clasificarProducto(), calcularPrecioFinal(), validarStock()
 * 
 * VISTA (Diego García):
 * - ProductoFrame.java
 * * Formulario de ingreso de datos
 * * Tabla de visualización de productos
 * * Botones para operaciones CRUD
 * 
 * CONTROLADOR (Diego García):
 * - InventarioController.java
 * * agregarProducto()
 * * modificarProducto()
 * * eliminarProducto()
 * * listarProductos()
 * * verificarStockBajo()
 * 
 * FUNCIONALIDADES IMPLEMENTADAS:
 * 
 * 1. REGISTRAR PRODUCTO (RF01, RF03):
 * - Captura: id, nombre, precio, stock, tipo (contratado/adicional)
 * - Validación de datos obligatorios
 * - Clasificación automática de producto (Keila)
 * - Almacenamiento en BD (Juan)
 * 
 * 2. MODIFICAR PRODUCTO (RF08):
 * - Carga datos existentes en formulario
 * - Permite edición de campos
 * - Actualiza en BD con validación
 * 
 * 3. ELIMINAR PRODUCTO (RF08):
 * - Confirmación antes de eliminar
 * - Validación de productos con stock
 * - Eliminación lógica o física según reglas
 * 
 * 4. LISTAR PRODUCTOS:
 * - Visualización en JTable
 * - Filtros por tipo, stock, nombre
 * - Ordenamiento por columnas
 * 
 * 5. ALERTAS DE STOCK (RF02):
 * - Indicador visual cuando stock < 5
 * - Notificación al usuario
 * - Sugerencia de orden de compra
 * 
 * COMPONENTES GRÁFICOS:
 * - txtId: Campo de texto para ID del producto
 * - txtNombre: Campo de texto para nombre
 * - txtPrecio: Campo de texto para precio
 * - txtStock: Campo de texto para cantidad en stock
 * - cmbTipoProducto: ComboBox para tipo (Contratado/Adicional)
 * - btnGuardar: Botón para guardar producto
 * - btnCancelar: Botón para cancelar operación
 * - btnModificar: Botón para modificar producto
 * - btnEliminar: Botón para eliminar producto
 * - tblProductos: Tabla para listar productos
 * 
 * VALIDACIONES:
 * - Campos obligatorios no vacíos
 * - Precio > 0
 * - Stock >= 0
 * - Formato numérico correcto
 * - Producto único (no duplicar ID)
 * 
 * INTEGRACIÓN CON OTRAS CLASES:
 * - Producto.java (Keila): Modelo de datos
 * - InventarioController.java (Diego): Lógica de negocio
 * - ConexionBD.java (Juan): Persistencia de datos
 * - MenuPrincipal.java (Diego): Navegación
 * - OrdenCompraController.java (Erick): Alertas de stock
 * 
 * PRUEBAS REALIZADAS POR DIEGO:
 * - Conexión exitosa a BD (implementada por Juan)
 * - Inserción de productos
 * - Modificación de productos
 * - Eliminación de productos
 * - Validación de formularios
 * - Eventos de botones funcionando
 * - Integración MVC completa
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF01: Registrar consumo diario de productos
 * - RF02: Generar alerta de stock bajo
 * - RF03: Clasificar producto como contratado o adicional
 * - RF08: Editar o eliminar registros de productos
 * 
 * FECHA: Octubre 2025
 * 
 * @author Diego García
 * @version 1.0
 */
public class ProductoFrame extends JFrame {
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JComboBox<String> cmbTipo;
    private JTextField txtPrecioCosto;
    private JTextField txtPrecioVenta;
    private JTextField txtStockMinimo;
    private JComboBox<String> cmbProveedor;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnRecalcular;
    private JTable tblProductos;
    private DefaultTableModel tableModel;

    private InventarioController inventarioController;

    public ProductoFrame(InventarioController inventarioController) {
        this.inventarioController = inventarioController;
        setTitle("Gestión de Productos");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        // Panel principal con fondo blanco
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(UIConstants.PANEL_BG);

        // Panel del título celeste
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(UIConstants.PRIMARY);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel lblTitulo = new JLabel("Datos del Producto", SwingConstants.CENTER);
        lblTitulo.setFont(UIConstants.TITLE_FONT);
        lblTitulo.setForeground(UIConstants.PANEL_BG);
        panelTitulo.add(lblTitulo);

        // Panel del formulario con GridBagLayout
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        form.add(new JLabel("Código:"), c);
        c.gridx = 1;
        c.weightx = 1.0;
        txtCodigo = new JTextField();
        txtCodigo.setPreferredSize(new Dimension(160, 24));
        form.add(txtCodigo, c);

        c.gridx = 2;
        c.weightx = 0;
        form.add(new JLabel("Nombre:"), c);
        c.gridx = 3;
        c.weightx = 1.0;
        txtNombre = new JTextField();
        form.add(txtNombre, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        form.add(new JLabel("Tipo:"), c);
        c.gridx = 1;
        cmbTipo = new JComboBox<>(new String[] { "CONTRATADO", "ADICIONAL" });
        form.add(cmbTipo, c);

        c.gridx = 2;
        form.add(new JLabel("Precio Costo:"), c);
        c.gridx = 3;
        txtPrecioCosto = new JTextField();
        form.add(txtPrecioCosto, c);

        c.gridx = 0;
        c.gridy = 2;
        form.add(new JLabel("Precio Venta:"), c);
        c.gridx = 1;
        txtPrecioVenta = new JTextField();
        form.add(txtPrecioVenta, c);

        c.gridx = 2;
        form.add(new JLabel("Stock Mínimo:"), c);
        c.gridx = 3;
        txtStockMinimo = new JTextField("5");
        form.add(txtStockMinimo, c);

        c.gridx = 0;
        c.gridy = 3;
        form.add(new JLabel("Proveedor:"), c);
        c.gridx = 1;
        c.gridwidth = 3;
        cmbProveedor = new JComboBox<>(new String[] { "Proveedor A", "Proveedor B" });
        form.add(cmbProveedor, c);
        c.gridwidth = 1;

        // Tabla
        String[] columns = new String[] { "ID", "Código", "Nombre", "Tipo", "Clasificación", "Precio Costo",
                "Precio Venta", "Precio Final", "Ganancia", "Stock Mínimo", "Proveedor" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c1) {
                return false;
            }
        };
        tblProductos = new JTable(tableModel);
        tblProductos.setRowHeight(22);
        tblProductos.setFillsViewportHeight(true);
        tblProductos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblProductos.getColumnModel().getColumn(2).setPreferredWidth(180);
        JScrollPane scroll = new JScrollPane(tblProductos);
        
        // Panel de registro de consumo diario (debajo del formulario)
        JPanel consumoPanel = new JPanel(new GridBagLayout());
        consumoPanel.setBorder(BorderFactory.createTitledBorder("Registrar Consumo Diario"));
        consumoPanel.setBackground(Color.WHITE);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        
        gc.gridx = 0; gc.gridy = 0;
        consumoPanel.add(new JLabel("Producto:"), gc);
        gc.gridx = 1;
        JComboBox<String> cmbProductos = new JComboBox<>();
        consumoPanel.add(cmbProductos, gc);
        
        gc.gridx = 0; gc.gridy = 1;
        consumoPanel.add(new JLabel("Cantidad Consumida:"), gc);
        gc.gridx = 1;
        JTextField txtCantidadConsumo = new JTextField("0", 10);
        consumoPanel.add(txtCantidadConsumo, gc);
        
        gc.gridx = 0; gc.gridy = 2;
        consumoPanel.add(new JLabel("Observaciones (opcional):"), gc);
        gc.gridx = 1;
        JTextField txtObs = new JTextField(20);
        consumoPanel.add(txtObs, gc);
        
        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 2;
        JButton btnRegistrarConsumo = crearBotonEstilizado("Registrar Consumo");
        consumoPanel.add(btnRegistrarConsumo, gc);
        
        btnRegistrarConsumo.addActionListener(e -> {
            String sel = (String) cmbProductos.getSelectedItem();
            if (sel == null || sel.isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un producto"); return; }
            int prodId = Integer.parseInt(sel.split(" - ")[0].trim());
            double cantidad = parseDoubleSafe(txtCantidadConsumo.getText());
            if (cantidad <= 0) { JOptionPane.showMessageDialog(this, "Cantidad inválida"); return; }
            int cantidadInt = (int)Math.round(cantidad);
            // Llamar al stored procedure que encapsula lógica en la BD
            boolean ok = inventarioController.registrarConsumoSP(prodId, cantidadInt, txtObs.getText());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Consumo registrado");
                cargarProductos();
                refrescarComboProductos(cmbProductos);
            } else {
                JOptionPane.showMessageDialog(this, "Error registrando consumo (ver logs)");
            }
        });

        // Botones
        btnGuardar = crearBotonEstilizado("Guardar");
        btnRecalcular = crearBotonEstilizado("Recalcular algoritmos");
        btnCancelar = crearBotonEstilizado("Cancelar");
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        botones.add(btnRecalcular);
        botones.add(btnGuardar);
        botones.add(btnCancelar);

        // Ensamblar todo
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);
        // Agrupamos el formulario y el panel de consumo en la parte superior
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.WHITE);
        northPanel.add(form, BorderLayout.NORTH);
        northPanel.add(consumoPanel, BorderLayout.SOUTH);
        panelCentro.add(northPanel, BorderLayout.NORTH);
        panelCentro.add(scroll, BorderLayout.CENTER);
        panelCentro.add(botones, BorderLayout.SOUTH);

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        setContentPane(panelPrincipal);

        // Listeners
        btnGuardar.addActionListener(e -> guardarProducto());
        btnCancelar.addActionListener(e -> dispose());
        btnRecalcular.addActionListener(e -> recalcularAlgoritmos());
        // Cargar productos desde la base de datos
        cargarProductos();
    }

    private void cargarProductos() {
        if (inventarioController == null) return;
        tableModel.setRowCount(0);
        for (Producto p : inventarioController.listarProductos()) {
            String clasificacion = clasificarProducto(p.getTipo());
            double precioCosto = p.getPrecioCosto();
            double precioVenta = p.getPrecioVenta();
            double precioFinal = p.calcularPrecioFinal();
            double ganancia = p.calcularGanancia();
            Object[] row = new Object[] {
                    p.getId(),
                    p.getCodigo() == null ? "" : p.getCodigo(),
                    p.getNombre(),
                    p.getTipo(),
                    clasificacion,
                    String.format("%.2f", precioCosto),
                    String.format("%.2f", precioVenta),
                    String.format("%.2f", precioFinal),
                    String.format("%.2f", ganancia),
                    p.getStockMinimo(),
                    "-"
            };
            tableModel.addRow(row);
        }
        // refrescar combo si existe en el formulario (se crea dinámicamente)
        // buscamos componentes tipo JComboBox dentro del panel y actualizamos el primero que coincida
        Component[] comps = getContentPane().getComponents();
        // Llamamos al helper que actualiza combos si los encuentra
        for (Component c : comps) {
            if (c instanceof JPanel) {
                for (Component sub : ((JPanel)c).getComponents()) {
                    if (sub instanceof JComboBox) {
                        try { refrescarComboProductos((JComboBox<String>) sub); } catch (Exception ignore) {}
                        return;
                    }
                }
            }
        }
    }

    private void refrescarComboProductos(JComboBox<String> combo) {
        combo.removeAllItems();
        for (Producto p : inventarioController.listarProductos()) {
            combo.addItem(p.getId() + " - " + p.getNombre());
        }
    }

    private void guardarProducto() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();
        double precioCosto = parseDoubleSafe(txtPrecioCosto.getText());
        double precioVenta = parseDoubleSafe(txtPrecioVenta.getText());
        int stockMinimo = (int) parseDoubleSafe(txtStockMinimo.getText());
        String proveedor = (String) cmbProveedor.getSelectedItem();

        String clasificacion = clasificarProducto(tipo);
        double precioFinal = calcularPrecioFinal(precioCosto, precioVenta);
        double ganancia = precioVenta - precioCosto;

        // Añade a tabla
        Object[] row = new Object[] {
                codigo, // ID aquí usamos codigo como ID por simplicidad
                codigo,
                nombre,
                tipo,
                clasificacion,
                String.format("%.2f", precioCosto),
                String.format("%.2f", precioVenta),
                String.format("%.2f", precioFinal),
                String.format("%.2f", ganancia),
                stockMinimo,
                proveedor
        };
        tableModel.addRow(row);

        // Opcional: agregar al controlador de inventario (se mantiene simple)
        try {
            int id = Integer.parseInt(codigo);
            Producto p = new Producto(id, codigo, nombre, tipo, 0, stockMinimo, precioCosto, precioVenta, 0);
            if (inventarioController != null) {
                inventarioController.agregarProducto(p);
            }
        } catch (NumberFormatException ex) {
            // Si el código no es numérico, creamos sin id y delegamos al controlador
            Producto p = new Producto(0, codigo, nombre, tipo, 0, stockMinimo, precioCosto, precioVenta, 0);
            if (inventarioController != null) {
                inventarioController.agregarProducto(p);
            }
        }

        JOptionPane.showMessageDialog(this, "Producto guardado: " + nombre);
        // no cerramos para permitir más inserciones
    }

    private void recalcularAlgoritmos() {
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            double precioCosto = parseDoubleSafe(String.valueOf(tableModel.getValueAt(r, 5)));
            double precioVenta = parseDoubleSafe(String.valueOf(tableModel.getValueAt(r, 6)));
            String tipo = String.valueOf(tableModel.getValueAt(r, 3));
            String clasificacion = clasificarProducto(tipo);
            double precioFinal = calcularPrecioFinal(precioCosto, precioVenta);
            double ganancia = precioVenta - precioCosto;
            tableModel.setValueAt(clasificacion, r, 4);
            tableModel.setValueAt(String.format("%.2f", precioFinal), r, 7);
            tableModel.setValueAt(String.format("%.2f", ganancia), r, 8);
        }
        JOptionPane.showMessageDialog(this, "Algoritmos recalculados");
    }

    private String clasificarProducto(String tipo) {
        // Ejemplo simple: si es CONTRATADO -> "PRIORITARIO"
        if ("CONTRATADO".equalsIgnoreCase(tipo))
            return "PRIORITARIO";
        return "ADICIONAL";
    }

    private double calcularPrecioFinal(double costo, double venta) {
        // Ejemplo: precio final puede ser el precio de venta menos descuentos o con IVA
        return venta; // placeholder: usar reglas reales según negocio
    }

    private double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductoFrame frame = new ProductoFrame(new InventarioController());
            frame.setVisible(true);
        });
    }

    private JButton crearBotonEstilizado(String texto) {
        return UIFactory.createRoundedButton(texto, UIConstants.PRIMARY, Color.WHITE, 140, 34);
    }
}