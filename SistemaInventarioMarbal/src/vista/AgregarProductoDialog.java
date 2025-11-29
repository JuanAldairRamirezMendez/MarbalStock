package vista;

import controlador.InventarioController;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AgregarProductoDialog extends JDialog {
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtProveedor;
    private JCheckBox chkConsumoCliente;
    private JTextField txtClienteId;
    private JTextField txtCantidad;
    private JTextField txtFecha;

    private InventarioController inventarioController;

    private JTable tablaProductos;
    private DefaultTableModel tablaModel;

    public AgregarProductoDialog(Frame owner, InventarioController inventarioController) {
        super(owner, "Ingreso de Productos", true);
        this.inventarioController = inventarioController;
        initComponents();
        setSize(820, 420);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIConstants.PANEL_BG);

        // Encabezado al estilo Reportes (barra naranja y botón a la derecha)
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConstants.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        JLabel headerTitle = new JLabel("MARBAL - INGRESO DE PRODUCTOS", SwingConstants.CENTER);
        headerTitle.setForeground(UIConstants.PANEL_BG);
        headerTitle.setFont(UIConstants.SECTION_FONT);
        header.add(headerTitle, BorderLayout.CENTER);
        JButton btnHeaderGuardar = UIFactory.createRoundedButton("Guardar", UIConstants.PRIMARY, UIConstants.PANEL_BG, 110, 34);
        btnHeaderGuardar.setToolTipText("Guardar producto");
        btnHeaderGuardar.addActionListener(e -> onGuardar());
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        headerRight.setOpaque(false);
        headerRight.add(btnHeaderGuardar);
        header.add(headerRight, BorderLayout.EAST);
        main.add(header, BorderLayout.NORTH);

        // panel izquierdo: formulario
        JPanel left = new JPanel(new GridBagLayout());
        left.setBackground(new Color(200, 250, 250));
        left.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.anchor = GridBagConstraints.WEST;

        c.gridx=0; c.gridy=0; left.add(new JLabel("CODIGO"), c);
        c.gridx=1; txtCodigo = new JTextField("1",10); left.add(txtCodigo, c);

        c.gridx=0; c.gridy=1; left.add(new JLabel("NOMBRE"), c);
        c.gridx=1; txtNombre = new JTextField(20); left.add(txtNombre, c);

        c.gridx=0; c.gridy=2; left.add(new JLabel("PROVEEDOR"), c);
        c.gridx=1; txtProveedor = new JTextField(18); left.add(txtProveedor, c);
        c.gridx=2; JButton btnBuscar = UIFactory.createRoundedButton("Buscar", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 80, 28); left.add(btnBuscar, c);

        c.gridx=0; c.gridy=3; left.add(new JLabel("CANTIDAD"), c);
        c.gridx=1; txtCantidad = new JTextField(8); left.add(txtCantidad, c);

        c.gridx=0; c.gridy=4; chkConsumoCliente = new JCheckBox("Registrar como consumo para cliente"); chkConsumoCliente.setBackground(new Color(200,250,250)); c.gridwidth=2; left.add(chkConsumoCliente, c); c.gridwidth=1;
        c.gridx=0; c.gridy=5; left.add(new JLabel("Cliente ID (si aplica):"), c);
        c.gridx=1; txtClienteId = new JTextField(8); left.add(txtClienteId, c);

        c.gridx=0; c.gridy=6; left.add(new JLabel("FECHA"), c);
        c.gridx=1; txtFecha = new JTextField(10); left.add(txtFecha, c);

        // Inicialmente los campos de consumo por cliente están deshabilitados
        txtClienteId.setEnabled(false);
        txtFecha.setEnabled(false);
        chkConsumoCliente.addItemListener(ev -> {
            boolean sel = chkConsumoCliente.isSelected();
            txtClienteId.setEnabled(sel);
            txtFecha.setEnabled(sel);
        });

        // botón Guardar original (lo mantenemos pero lo ocultamos porque ahora tenemos botón en header)
        c.gridx=0; c.gridy=7; c.gridwidth=3; c.anchor = GridBagConstraints.CENTER;
        JButton btnGuardar = UIFactory.createRoundedButton("Guardar", UIConstants.PRIMARY, UIConstants.PANEL_BG, 120, 36);
        left.add(btnGuardar, c);
        // ocultar el botón central para no duplicar la acción visualmente (mantener estructura)
        btnGuardar.setVisible(false);

        // panel derecho: tabla
        String[] cols = new String[]{"Codigo","Descripción","Stock Actual"};
        tablaModel = new DefaultTableModel(cols,0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaProductos = new JTable(tablaModel);
        tablaProductos.setFillsViewportHeight(true);
        JScrollPane sp = new JScrollPane(tablaProductos);

        // dividir horizontalmente
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, sp);
        split.setResizeWeight(0.45);
        split.setDividerSize(6);

        main.add(split, BorderLayout.CENTER);
        setContentPane(main);

        // acciones
        btnGuardar.addActionListener(e -> onGuardar());
        btnBuscar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Buscar proveedor (pendiente)"));
    }

    private void onGuardar() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String proveedor = txtProveedor.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();
        String fecha = txtFecha.getText().trim();

        // Validaciones básicas
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del producto.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double cantidadDouble = 0.0;
        try { cantidadDouble = Double.parseDouble(cantidadStr); } catch (Exception ex) { cantidadDouble = -1; }
        if (cantidadDouble <= 0) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida (> 0).", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Si se marca como consumo para cliente, validar y registrar con inventarioController
        if (chkConsumoCliente != null && chkConsumoCliente.isSelected()) {
            int clienteId = 0; try { clienteId = Integer.parseInt(txtClienteId.getText().trim()); } catch (Exception ex) { clienteId = 0; }
            if (clienteId <= 0) { JOptionPane.showMessageDialog(this, "Ingrese un Cliente ID válido.", "Validación", JOptionPane.WARNING_MESSAGE); return; }

            // El producto debe existir para registrar consumo (usar el código como id cuando aplique)
            int productoId = -1;
            try { productoId = Integer.parseInt(codigo); } catch (Exception ex) { productoId = -1; }
            if (productoId <= 0) {
                JOptionPane.showMessageDialog(this, "Para registrar consumo debe indicar el ID numérico del producto en el campo CODIGO.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (inventarioController != null) {
                // validar que el producto exista
                Producto existente = inventarioController.obtenerProductoPorId(productoId);
                if (existente == null) {
                    JOptionPane.showMessageDialog(this, "Producto con ID " + productoId + " no encontrado.", "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                InventarioController.ValidationResult res = inventarioController.registrarConsumoParaCliente(clienteId, productoId, cantidadDouble, "Consumo desde GUI", false, "gui");
                if (!res.isOk()) {
                    JOptionPane.showMessageDialog(this, res.getMessage(), "No permitido", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    tablaModel.addRow(new Object[]{productoId, existente.getNombre(), (int)Math.round(cantidadDouble)});
                    JOptionPane.showMessageDialog(this, "Consumo registrado: " + existente.getNombre());
                    // limpiar campos básicos
                    txtCantidad.setText(""); txtClienteId.setText(""); txtFecha.setText("");
                    return;
                }
            }
        }

        // Si no es consumo por cliente, se interpreta como ingreso/alta de producto
        int stock = (int)Math.round(cantidadDouble);
        Producto p = new Producto(0, codigo == null ? "" : codigo, nombre, proveedor == null || proveedor.isEmpty() ? "OTROS" : proveedor, stock, 5, 0.0, 0.0, 0);
        boolean ok = false;
        if (inventarioController != null) {
            try { ok = inventarioController.agregarProducto(p); } catch (Exception ex) { ok = false; }
        }
        if (ok) {
            // mostrar id asignado si fue generado
            tablaModel.addRow(new Object[]{p.getId() > 0 ? p.getId() : codigo, p.getNombre(), p.getStock()});
            JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
            // limpiar campos
            txtCodigo.setText(""); txtNombre.setText(""); txtProveedor.setText(""); txtCantidad.setText("");
        } else {
            // aún así añadir a la tabla en modo local para visual
            tablaModel.addRow(new Object[]{codigo, nombre, stock});
            JOptionPane.showMessageDialog(this, "Producto agregado en vista, no fue persistido (error).", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
