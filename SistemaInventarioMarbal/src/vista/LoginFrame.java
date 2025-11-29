package vista;

import controlador.UsuarioController;
import modelo.Usuario;

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
    private int intentosFallidos = 0;

    public LoginFrame(UsuarioController usuarioController) {
        this.usuarioController = usuarioController != null ? usuarioController : new UsuarioController();

        setTitle("Sistema de Inventario MARBAL - Iniciar Sesión");
        setSize(520, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        /* ========== DISEÑO CELESTE (estilo similar a capturas) ========== */
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(UIConstants.BACKGROUND);

        // Panel del encabezado (azul con nombre MARBAL)
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setBackground(UIConstants.HEADER);
        panelEncabezado.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel lblMarca = new JLabel("MARBAL", SwingConstants.CENTER);
        lblMarca.setFont(UIConstants.TITLE_FONT);
        lblMarca.setForeground(UIConstants.PANEL_BG);
        panelEncabezado.add(lblMarca, BorderLayout.CENTER);

        JLabel lblSub = new JLabel("Sistema de Control de Inventario", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(UIConstants.HEADER_SUB);
        panelEncabezado.add(lblSub, BorderLayout.SOUTH);

        // Panel del formulario
        JPanel panelForm = new JPanel();
        panelForm.setBackground(UIConstants.PANEL_BG);
        panelForm.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        panelForm.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Usuario
        JLabel lblUsuario = new JLabel("Usuario (Email):");
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

        // Botones (usar UIFactory para consistencia)
        btnLogin = UIFactory.createRoundedButton("Iniciar Sesión", UIConstants.PRIMARY, Color.WHITE, 0, 34);
        JButton btnRegistrar = UIFactory.createRoundedButton("Registrarse", UIConstants.SECONDARY_BUTTON, Color.DARK_GRAY, 0, 34);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panelForm.add(btnLogin, gbc);

        gbc.gridx = 1;
        panelForm.add(btnRegistrar, gbc);

        // Acción del botón (autenticación real)
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usuarioController == null) {
                JOptionPane.showMessageDialog(this, "Error interno: controlador no disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario usuario = usuarioController.autenticar(username, password);
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido: " + usuario.getNombre());
                SwingUtilities.invokeLater(() -> {
                    MenuPrincipal menu = new MenuPrincipal(usuario.getRol());
                    menu.setVisible(true);
                });
                dispose();
            } else {
                intentosFallidos++;
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                if (intentosFallidos >= 3) {
                    JOptionPane.showMessageDialog(this, "Ha excedido el número de intentos. Contacte al administrador.", "Bloqueado", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                }
            }
        });

        btnRegistrar.addActionListener(e -> {
            RegistroDialog dlg = new RegistroDialog(this, this.usuarioController);
            dlg.setVisible(true);
        });

        // Ensamblar todo
            panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
        panelPrincipal.add(panelForm, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private void estilizarCampo(JTextField campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.TEXT_SECONDARY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        campo.setBackground(UIConstants.PANEL_BG);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame(new UsuarioController()).setVisible(true));
    }
}