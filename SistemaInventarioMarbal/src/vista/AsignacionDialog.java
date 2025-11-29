package vista;

import modelo.ProductoAsignacion;

import javax.swing.*;
import java.awt.*;

public class AsignacionDialog extends JDialog {
    private JTextField txtClienteId;
    private JTextField txtProductoId;
    private JTextField txtAsignacionAnual;
    private ProductoAsignacion resultado = null;

    public AsignacionDialog(Frame owner) {
        super(owner, "Crear / Editar Asignación", true);
        initialize();
    }

    private void initialize() {
        setSize(350, 220);
        setLocationRelativeTo(getOwner());
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; p.add(new JLabel("Cliente ID:"), c);
        c.gridx = 1; txtClienteId = new JTextField(); p.add(txtClienteId, c);

        c.gridx = 0; c.gridy = 1; p.add(new JLabel("Producto ID:"), c);
        c.gridx = 1; txtProductoId = new JTextField(); p.add(txtProductoId, c);

        c.gridx = 0; c.gridy = 2; p.add(new JLabel("Asignación anual:"), c);
        c.gridx = 1; txtAsignacionAnual = new JTextField(); p.add(txtAsignacionAnual, c);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("Guardar");
        JButton btnCancel = new JButton("Cancelar");
        botones.add(btnOk); botones.add(btnCancel);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; p.add(botones, c);

        btnOk.addActionListener(e -> onGuardar());
        btnCancel.addActionListener(e -> onCancelar());

        setContentPane(p);
    }

    private void onGuardar() {
        try {
            int clienteId = Integer.parseInt(txtClienteId.getText().trim());
            int productoId = Integer.parseInt(txtProductoId.getText().trim());
            double asign = Double.parseDouble(txtAsignacionAnual.getText().trim());
            resultado = new ProductoAsignacion(0, clienteId, productoId, asign, "", true);
            setVisible(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Valores inválidos: " + ex.getMessage());
        }
    }

    private void onCancelar() {
        resultado = null;
        setVisible(false);
    }

    public void setValores(modelo.ProductoAsignacion a) {
        if (a == null) return;
        txtClienteId.setText(String.valueOf(a.getClienteId()));
        txtProductoId.setText(String.valueOf(a.getProductoId()));
        txtAsignacionAnual.setText(String.valueOf(a.getAsignacionAnual()));
    }

    public ProductoAsignacion getResultado() { return resultado; }
}
