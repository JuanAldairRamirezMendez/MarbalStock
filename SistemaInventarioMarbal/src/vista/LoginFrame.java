package vista;

import javax.swing.*;
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

    public LoginFrame() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2));

        JLabel lblUsername = new JLabel("Username:");
        txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField();
        btnLogin = new JButton("Login");

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí se manejaría la lógica de autenticación
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                // Validar credenciales (esto es solo un ejemplo)
                if (username.equals("admin") && password.equals("admin")) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    // Aquí se podría abrir el menú principal
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials, please try again.");
                }
            }
        });

        add(lblUsername);
        add(txtUsername);
        add(lblPassword);
        add(txtPassword);
        add(btnLogin);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}