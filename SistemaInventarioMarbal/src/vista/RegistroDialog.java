package vista;

import controlador.UsuarioController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


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
        setSize(420, 520);
        setLocationRelativeTo(parent);
    }

    private void initialize() {
        // Diseño similar al login: encabezado turquesa y formulario centrado
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(UIConstants.BACKGROUND);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(UIConstants.PRIMARY);
        encabezado.setPreferredSize(new Dimension(360, 56));
        JLabel titulo = new JLabel("REGISTRO", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(UIConstants.SECTION_FONT);
        encabezado.add(titulo, BorderLayout.CENTER);

        JPanel centro = new JPanel();
        centro.setBackground(UIConstants.PANEL_BG);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBorder(BorderFactory.createEmptyBorder(14, 18, 12, 18));

        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setForeground(UIConstants.PRIMARY);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblNombre);
        centro.add(Box.createRigidArea(new Dimension(0,6)));
        txtNombre = new JTextField(); estilizarCampo(txtNombre); txtNombre.setMaximumSize(new Dimension(280,34)); centro.add(txtNombre);
        centro.add(Box.createRigidArea(new Dimension(0,10)));

        JLabel lblUser = new JLabel("Usuario");
        lblUser.setForeground(UIConstants.PRIMARY);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblUser);
        centro.add(Box.createRigidArea(new Dimension(0,6)));
        txtUsername = new JTextField(); estilizarCampo(txtUsername); txtUsername.setMaximumSize(new Dimension(280,34)); centro.add(txtUsername);
        centro.add(Box.createRigidArea(new Dimension(0,10)));

        JLabel lblPw = new JLabel("Contraseña");
        lblPw.setForeground(UIConstants.PRIMARY);
        lblPw.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPw.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblPw);
        centro.add(Box.createRigidArea(new Dimension(0,6)));
        txtPassword = new JPasswordField(); estilizarCampo(txtPassword); txtPassword.setMaximumSize(new Dimension(280,34)); centro.add(txtPassword);
        centro.add(Box.createRigidArea(new Dimension(0,10)));

        JLabel lblPw2 = new JLabel("Confirmar contraseña");
        lblPw2.setForeground(UIConstants.PRIMARY);
        lblPw2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPw2.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblPw2);
        centro.add(Box.createRigidArea(new Dimension(0,6)));
        txtPassword2 = new JPasswordField(); estilizarCampo(txtPassword2); txtPassword2.setMaximumSize(new Dimension(280,34)); centro.add(txtPassword2);

        centro.add(Box.createRigidArea(new Dimension(0,16)));
        JButton btnOk = UIFactory.createRoundedButton("Registrarse", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 140, 36);
        btnOk.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(btnOk);
        centro.add(Box.createRigidArea(new Dimension(0,8)));
        JButton btnCancel = UIFactory.createRoundedButton("Cancelar", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 120, 36);
        btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(btnCancel);

        btnOk.addActionListener(e -> onRegistrar());
        btnCancel.addActionListener(e -> dispose());

        panelPrincipal.add(encabezado, BorderLayout.NORTH);
        panelPrincipal.add(centro, BorderLayout.CENTER);
        getContentPane().add(panelPrincipal);
    }

    // helper to reuse styling method from LoginFrame
    private void estilizarCampo(JTextField campo) {
        campo.setOpaque(true);
        campo.setBackground(new Color(250,250,252));
        campo.setBorder(new RoundedBorder(10, UIConstants.TEXT_SECONDARY));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(240, 34));
    }

    private void onRegistrar() {
        String nombre = txtNombre.getText().trim();
        String user = txtUsername.getText().trim();
        String pw = new String(txtPassword.getPassword());
        String pw2 = new String(txtPassword2.getPassword());
        String rol = "OPERARIO";
        if (user.isEmpty() || pw.isEmpty() || !pw.equals(pw2)) {
            JOptionPane.showMessageDialog(this, "Complete los campos y confirme la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean ok = usuarioController.registrarUsuario(user, nombre, pw, rol);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
            dispose();
            return;
        }

        String reason = usuarioController.getLastErrorMessage();
        if (reason == null || reason.isEmpty()) {
            reason = "No se pudo registrar el usuario. El username puede existir.";
        }

        // Si la causa es falta de conexión, ofrecer crear archivo de configuración y esquema
        if (reason.contains("No se pudo conectar") || reason.toLowerCase().contains("no se pudo conectar")) {
            int resp = JOptionPane.showConfirmDialog(this,
                    "No se pudo conectar a la base de datos. ¿Desea configurar la conexión ahora?",
                    "Configurar DB",
                    JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                boolean setup = promptAndCreateDbConfig();
                if (setup) {
                    // reintentar registro
                    boolean ok2 = usuarioController.registrarUsuario(user, nombre, pw, rol);
                    if (ok2) {
                        JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
                        dispose();
                        return;
                    } else {
                        String r2 = usuarioController.getLastErrorMessage();
                        if (r2 == null || r2.isEmpty()) r2 = "No se pudo registrar el usuario tras configurar la DB.";
                        JOptionPane.showMessageDialog(this, r2, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se completó la configuración. Registro cancelado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, reason, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, reason, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private String testConnection(String host, String port, String db, String user, String pass, boolean admin) {
        String url;
        if (admin) {
            // conectar a la base administrativa 'postgres'
            url = String.format("jdbc:postgresql://%s:%s/postgres", host, port);
        } else {
            url = String.format("jdbc:postgresql://%s:%s/%s", host, port, db);
        }
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            return null;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private boolean promptAndCreateDbConfig() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtHost = new JTextField("127.0.0.1", 15);
        JTextField txtPort = new JTextField("5440", 6);
        JTextField txtDb = new JTextField("inventario_marbal", 12);
        JTextField txtUser = new JTextField("marbal", 10);
        JPasswordField txtPass = new JPasswordField("TuPassSegura123", 10);

        c.gridx=0; c.gridy=0; p.add(new JLabel("Host:"), c);
        c.gridx=1; p.add(txtHost, c);
        c.gridx=0; c.gridy=1; p.add(new JLabel("Puerto:"), c);
        c.gridx=1; p.add(txtPort, c);
        c.gridx=0; c.gridy=2; p.add(new JLabel("Base de datos:"), c);
        c.gridx=1; p.add(txtDb, c);
        c.gridx=0; c.gridy=3; p.add(new JLabel("Usuario:"), c);
        c.gridx=1; p.add(txtUser, c);
        c.gridx=0; c.gridy=4; p.add(new JLabel("Contraseña:"), c);
        c.gridx=1; p.add(txtPass, c);

        int res = JOptionPane.showConfirmDialog(this, p, "Configurar conexión a PostgreSQL", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return false;

        String host = txtHost.getText().trim();
        String port = txtPort.getText().trim();
        String db = txtDb.getText().trim();
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        // Probar conexión administrativa al servidor antes de escribir el archivo
        String adminTest = testConnection(host, port, db, user, pass, true);
        if (adminTest != null) {
            int ch = JOptionPane.showConfirmDialog(this, "No se pudo conectar al servidor PostgreSQL:\n" + adminTest + "\n¿Desea reintentar?", "Error de conexión", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (ch == JOptionPane.YES_OPTION) {
                return promptAndCreateDbConfig();
            }
            return false;
        }

        try {
            // crear directorio y archivo de propiedades
            File cfgDir = new File("recursos/config");
            cfgDir.mkdirs();
            File cfgFile = new File(cfgDir, "db.properties");
            try (FileWriter fw = new FileWriter(cfgFile)) {
                fw.write("db.type=postgresql\n");
                fw.write(String.format("db.url=jdbc:postgresql://%s:%s/%s\n", host, port, db));
                fw.write("db.user=" + user + "\n");
                fw.write("db.password=" + pass + "\n");
                fw.write("db.driver=org.postgresql.Driver\n");
            }

            // intentar crear la base de datos conectando a la BD administrativa 'postgres'
            String adminUrl = String.format("jdbc:postgresql://%s:%s/postgres", host, port);
            try (Connection conn = DriverManager.getConnection(adminUrl, user, pass); Statement st = conn.createStatement()) {
                try {
                    st.executeUpdate("CREATE DATABASE " + db);
                } catch (Exception ignore) {
                    // si ya existe o falla por permisos, continuar
                }
            }

            // crear tabla usuarios en la BD objetivo
            String targetUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, db);
            try (Connection conn2 = DriverManager.getConnection(targetUrl, user, pass); Statement st2 = conn2.createStatement()) {
                st2.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios (" +
                        "id SERIAL PRIMARY KEY, " +
                        "nombre VARCHAR(100), " +
                        "username VARCHAR(100) UNIQUE, " +
                        "password_hash VARCHAR(255), " +
                        "salt VARCHAR(255), " +
                        "rol VARCHAR(50), " +
                        "activo SMALLINT DEFAULT 1)");
            }

            // probar conexión a la base de datos creada
            String targetTest = testConnection(host, port, db, user, pass, false);
            if (targetTest != null) {
                JOptionPane.showMessageDialog(this, "La base de datos fue creada, pero no se pudo conectar a ella: " + targetTest, "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Conexión a la base de datos OK.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error configurando la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
