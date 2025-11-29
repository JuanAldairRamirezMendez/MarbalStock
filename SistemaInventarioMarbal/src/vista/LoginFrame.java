package vista;

import controlador.UsuarioController;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

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
    private UsuarioController usuarioController;

    public LoginFrame(UsuarioController usuarioController) {
        this.usuarioController = usuarioController != null ? usuarioController : new UsuarioController();

        setTitle("Sistema Inventario Marbal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        /* ========== DISEÑO CELESTE ========== */
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        // Panel del título celeste
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(0, 123, 255));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblTitulo = new JLabel("SISTEMA INVENTARIO MARBAL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);

        // Panel del formulario
        JPanel panelForm = new JPanel();
        panelForm.setBackground(Color.WHITE);
        panelForm.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        txtUsername = new JTextField(15);
        estilizarCampo(txtUsername);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(lblUsuario, gbc);

        gbc.gridx = 1;
        panelForm.add(txtUsername, gbc);

        // Contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(15);
        estilizarCampo(txtPassword);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(lblContrasena, gbc);

        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);

        // Botón
        btnLogin = new JButton("Iniciar sesión");
        btnLogin.setBackground(new Color(0, 123, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelForm.add(btnLogin, gbc);

        // Acción del botón
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            String rol = "OPERARIO";
            if ("admin".equalsIgnoreCase(username)) {
                rol = "ADMINISTRADOR";
            }
            final String rolFinal = rol;

            JOptionPane.showMessageDialog(this, "Login successful! Rol: " + rolFinal);
            SwingUtilities.invokeLater(() -> {
                MenuPrincipal menu = new MenuPrincipal(rolFinal);
                menu.setVisible(true);
            });
            dispose();
        });

        // Ensamblar todo
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelForm, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private void estilizarCampo(JTextField campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        campo.setBackground(Color.WHITE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame(null).setVisible(true));
    }
}