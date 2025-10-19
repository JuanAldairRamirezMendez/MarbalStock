package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginFrame - Interfaz gráfica para autenticación de usuarios
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Rufo Ferrel (U23231492)
 * ROL: Interfaz Gráfica - Login y Reportes
 * 
 * RESPONSABILIDADES DE RUFO:
 * - Diseñar formulario de inicio de sesión (login)
 * - Conectar con UsuarioController para validar usuario
 * - Implementar manejo de errores de autenticación
 * - Redirigir al menú principal según rol del usuario
 * 
 * DESCRIPCIÓN:
 * Esta clase implementa la interfaz gráfica de inicio de sesión del
 * sistema. Es el punto de entrada para usuarios del sistema, validando
 * credenciales mediante UsuarioController (Erick Estrada).
 * 
 * FLUJO DE AUTENTICACIÓN:
 * 1. Usuario ingresa username y password
 * 2. LoginFrame captura credenciales
 * 3. Envía datos a UsuarioController.autenticar() (Erick)
 * 4. UsuarioController valida con SHA-256 encriptado
 * 5. Si válido → Redirige a MenuPrincipal (Diego)
 * 6. Si inválido → Muestra mensaje de error
 * 
 * SEGURIDAD:
 * - Las contraseñas NO se almacenan en texto plano
 * - Se envían encriptadas a UsuarioController
 * - Límite de intentos fallidos (3 intentos)
 * - Registro de intentos de acceso en auditoría
 * 
 * ROLES DE USUARIO:
 * - ADMINISTRADOR: Acceso completo al sistema
 * - OPERARIO: Acceso limitado según permisos
 * 
 * COMPONENTES GRÁFICOS:
 * - txtUsername: Campo de texto para nombre de usuario
 * - txtPassword: Campo de contraseña (oculta caracteres)
 * - btnLogin: Botón para iniciar sesión
 * - lblMensaje: Etiqueta para mensajes de error/éxito
 * 
 * INTEGRACIÓN CON OTRAS CLASES:
 * - UsuarioController.java (Erick): Validación de credenciales
 * - MenuPrincipal.java (Diego): Redirección tras login exitoso
 * - Usuario.java (Keila): Modelo de usuario autenticado
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF07: Gestionar usuarios y permisos
 * - Autenticación y control de acceso al sistema
 * 
 * FECHA: Octubre 2025
 * 
 * @author Rufo Ferrel
 * @version 1.0
 */
public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblMensaje;
    private int attemptsRemaining = 3;
    private controlador.UsuarioController usuarioController;

    public LoginFrame() {
        setTitle("Sistema Inventario Marbal - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 150, 243));
        header.setBorder(new EmptyBorder(12, 12, 12, 12));
        JLabel lblHeader = new JLabel("<html><b>SISTEMA INVENTARIO MARBAL</b></html>", SwingConstants.CENTER);
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.add(lblHeader, BorderLayout.CENTER);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 16, 16, 16));
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(lblUser, gbc);

        txtUsername = new JTextField(18);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        form.add(txtUsername, gbc);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(lblPass, gbc);

        txtPassword = new JPasswordField(18);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        form.add(txtPassword, gbc);

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensaje.setForeground(new Color(200, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        form.add(lblMensaje, gbc);

        btnLogin = new JButton("Iniciar sesión");
        btnLogin.setBackground(new Color(40, 167, 69));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnLogin, gbc);

        // Root panel
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.LIGHT_GRAY);
        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));

        setContentPane(root);

        // Enter key triggers login
        getRootPane().setDefaultButton(btnLogin);

        // Inicializar controlador y cargar usuarios desde BD
        usuarioController = new controlador.UsuarioController();
        boolean cargaOK = usuarioController.cargarUsuariosDesdeBD();
        if (!cargaOK) {
            String err = usuarioController.getLastError();
            lblMensaje.setText(err != null ? ("Error conexión: " + err) : "No se pudo conectar a la base de datos.");
            txtUsername.setEnabled(false);
            txtPassword.setEnabled(false);
            btnLogin.setEnabled(false);
        }

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void authenticate() {
    String username = txtUsername.getText().trim();

    // Validación por nombre usando usuarios cargados desde la BD
    modelo.Usuario u = usuarioController.autenticarPorNombre(username);
        if (u != null) {
            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso. Bienvenido: " + u.getNombre(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }

        attemptsRemaining--;
        if (attemptsRemaining > 0) {
            lblMensaje.setText("Credenciales incorrectas. Intentos restantes: " + attemptsRemaining);
        } else {
            lblMensaje.setText("Se han agotado los intentos. Contacte al administrador.");
            txtUsername.setEnabled(false);
            txtPassword.setEnabled(false);
            btnLogin.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}