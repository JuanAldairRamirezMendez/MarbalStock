package vista;

import controlador.UsuarioController;

import javax.swing.*;
import java.awt.*;

public class RegistroDialog extends JDialog {
    private JTextField txtNombre;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtPassword2;
    private JComboBox<String> cmbRol;
    private UsuarioController usuarioController;

    public RegistroDialog(JFrame parent, UsuarioController usuarioController) {
        super(parent, "Registro de Usuario", true);
        this.usuarioController = usuarioController != null ? usuarioController : new UsuarioController();
        initialize();
        setSize(420, 320);
        setLocationRelativeTo(parent);
    }

    private void initialize() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(UIConstants.PANEL_BG);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; p.add(new JLabel("Nombre:"), c);
        c.gridx = 1; txtNombre = new JTextField(); p.add(txtNombre, c);

        c.gridx = 0; c.gridy = 1; p.add(new JLabel("Usuario (username):"), c);
        c.gridx = 1; txtUsername = new JTextField(); p.add(txtUsername, c);

        c.gridx = 0; c.gridy = 2; p.add(new JLabel("Contraseña:"), c);
        c.gridx = 1; txtPassword = new JPasswordField(); p.add(txtPassword, c);

        c.gridx = 0; c.gridy = 3; p.add(new JLabel("Confirmar contraseña:"), c);
        c.gridx = 1; txtPassword2 = new JPasswordField(); p.add(txtPassword2, c);

        c.gridx = 0; c.gridy = 4; p.add(new JLabel("Rol:"), c);
        c.gridx = 1; cmbRol = new JComboBox<>(new String[]{"OPERARIO","ADMINISTRADOR"}); p.add(cmbRol, c);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(UIConstants.PANEL_BG);
        JButton btnOk = UIFactory.createRoundedButton("Registrar", UIConstants.PRIMARY, Color.WHITE, 120, 36);
        JButton btnCancel = UIFactory.createRoundedButton("Cancelar", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 100, 36);
        actions.add(btnCancel); actions.add(btnOk);

        btnOk.addActionListener(e -> onRegistrar());
        btnCancel.addActionListener(e -> dispose());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(p, BorderLayout.CENTER);
        getContentPane().add(actions, BorderLayout.SOUTH);
    }

    private void onRegistrar() {
        String nombre = txtNombre.getText().trim();
        String user = txtUsername.getText().trim();
        String pw = new String(txtPassword.getPassword());
        String pw2 = new String(txtPassword2.getPassword());
        String rol = (String) cmbRol.getSelectedItem();
        if (user.isEmpty() || pw.isEmpty() || !pw.equals(pw2)) {
            JOptionPane.showMessageDialog(this, "Complete los campos y confirme la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean ok = usuarioController.registrarUsuario(user, nombre, pw, rol);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
            dispose();
        } else {
            String reason = usuarioController.getLastErrorMessage();
            if (reason == null || reason.isEmpty()) {
                reason = "No se pudo registrar el usuario. El username puede existir.";
            }
            JOptionPane.showMessageDialog(this, reason, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
