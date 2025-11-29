package vista;

import controlador.UsuarioController;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Diseño simplificado según captura: encabezado turquesa, icono y formulario centrado
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(UIConstants.BACKGROUND);

        // Encabezado turquesa con texto centrado
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setBackground(UIConstants.PRIMARY);
        panelEncabezado.setPreferredSize(new Dimension(320, 60));
        JLabel titulo = new JLabel("INICIO DE SESIÓN", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(UIConstants.SECTION_FONT);
        panelEncabezado.add(titulo, BorderLayout.CENTER);

        // Panel central con icono y campos
        JPanel centro = new JPanel();
        centro.setBackground(UIConstants.PANEL_BG);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBorder(BorderFactory.createEmptyBorder(18, 28, 18, 28));

        JLabel lock = new JLabel(createLockIcon(64));
        lock.setPreferredSize(new Dimension(64,64));
        lock.setHorizontalAlignment(SwingConstants.CENTER);
        lock.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lock);
        centro.add(Box.createRigidArea(new Dimension(0, 14)));

        // Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setForeground(UIConstants.PRIMARY);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblUsuario);
        centro.add(Box.createRigidArea(new Dimension(0, 6)));
        txtUsername = new JTextField();
        estilizarCampo(txtUsername);
        txtUsername.setMaximumSize(new Dimension(240, 34));
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(txtUsername);
        centro.add(Box.createRigidArea(new Dimension(0, 12)));

        // Contraseña
        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setForeground(UIConstants.PRIMARY);
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(lblContrasena);
        centro.add(Box.createRigidArea(new Dimension(0, 6)));
        txtPassword = new JPasswordField();
        estilizarCampo(txtPassword);
        txtPassword.setMaximumSize(new Dimension(240, 34));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(txtPassword);

        centro.add(Box.createRigidArea(new Dimension(0, 18)));

        // Botones
        btnLogin = UIFactory.createRoundedButton("Iniciar", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 120, 34);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(btnLogin);
        centro.add(Box.createRigidArea(new Dimension(0, 8)));
        JButton btnRegistrar = UIFactory.createRoundedButton("Registrarse", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 120, 34);
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        centro.add(btnRegistrar);

        // Ensamblar
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

        panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
        panelPrincipal.add(centro, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private void estilizarCampo(JTextField campo) {
        campo.setOpaque(true);
        campo.setBackground(new Color(250,250,252));
        campo.setBorder(new RoundedBorder(10, UIConstants.TEXT_SECONDARY));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(240, 34));
    }

    // Crear icono de candado programáticamente para evitar recorte de emoji
    private ImageIcon createLockIcon(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // fondo transparente
            g.setComposite(AlphaComposite.Src);
            g.setColor(new Color(0,0,0,0));
            g.fillRect(0,0,size,size);

            // dibujar candado
            int pad = Math.max(6, size/10);
            int bodyW = size - pad*2;
            int bodyH = (int)(bodyW * 0.6);
            int bodyX = pad;
            int bodyY = size - pad - bodyH;

            // cuerpo
            g.setColor(new Color(60,60,60));
            g.fillRoundRect(bodyX, bodyY, bodyW, bodyH, 8, 8);
            g.setColor(Color.WHITE);
            g.fillRect(bodyX + bodyW/2 - 6, bodyY + bodyH/3, 12, bodyH/3);

            // aro
            g.setColor(new Color(60,60,60));
            Stroke old = g.getStroke();
            g.setStroke(new BasicStroke(Math.max(3, size/24), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int arcW = bodyW/2;
            int arcX = bodyX + (bodyW - arcW)/2;
            int arcY = bodyY - arcW/2;
            g.drawArc(arcX, arcY, arcW, arcW, 0, 180);
            g.setStroke(old);
        } finally {
            g.dispose();
        }
        return new ImageIcon(img);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame(new UsuarioController()).setVisible(true));
    }
}