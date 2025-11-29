package vista;

import controlador.InventarioController;
import controlador.ReporteController;

import javax.swing.*;
import java.awt.*;

/**
 * MenuPrincipal - Interfaz principal del sistema post-autenticación
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Diego García (U23247615)
 * ROL: Líder de integración / Coordinador
 * 
 * RESPONSABILIDADES DE DIEGO:
 * - Integrar Modelo, Vista y Controlador
 * - Asegurar funcionalidad de botones y eventos
 * - Implementar navegación entre módulos del sistema
 * - Controlar acceso a funciones según rol de usuario
 * - Realizar pruebas finales del sistema
 * 
 * DESCRIPCIÓN:
 * Esta clase implementa el menú principal del sistema, mostrando las
 * opciones disponibles según el rol del usuario autenticado. Es el
 * centro de navegación que integra todos los módulos del sistema.
 * 
 * ARQUITECTURA MVC - INTEGRACIÓN:
 * Diego García es responsable de integrar:
 * 
 * MODELOS (Keila Mateo):
 * - Producto.java
 * - Proveedor.java
 * - Cliente.java
 * - Venta.java
 * - Usuario.java
 * - OrdenCompra.java
 * - Reporte.java
 * 
 * VISTAS (Diego García + Rufo Ferrel):
 * - MenuPrincipal.java (Diego)
 * - ProductoFrame.java (Diego)
 * - LoginFrame.java (Rufo)
 * - [Otras interfaces a implementar]
 * 
 * CONTROLADORES (Diego + Erick + Rufo):
 * - InventarioController.java (Diego)
 * - UsuarioController.java (Erick)
 * - OrdenCompraController.java (Erick)
 * - ReporteController.java (Rufo)
 * 
 * CONEXIÓN BD (Juan Ramírez):
 * - ConexionBD.java (Juan)
 * - script_bd.sql (Juan)
 * 
 * MÓDULOS DEL MENÚ PRINCIPAL:
 * 
 * PARA ADMINISTRADOR:
 * 1. Gestión de Inventario (ProductoFrame - Diego)
 * 2. Gestión de Usuarios (UsuarioController - Erick)
 * 3. Órdenes de Compra (OrdenCompraController - Erick)
 * 4. Reportes (ReporteController - Rufo)
 * 5. Gestión de Clientes
 * 6. Gestión de Proveedores
 * 7. Registro de Ventas
 * 8. Configuración del Sistema
 * 
 * PARA OPERARIO:
 * 1. Consultar Inventario
 * 2. Registrar Consumo Diario (RF01)
 * 3. Ver Alertas de Stock (RF02)
 * 4. Mis Reportes (limitados)
 * 
 * FUNCIONALIDADES CLAVE:
 * - Control de acceso por rol (validado con UsuarioController - Erick)
 * - Navegación entre módulos con eventos de botones
 * - Visualización de alertas de stock bajo en tiempo real
 * - Acceso rápido a funciones frecuentes
 * - Cierre de sesión seguro
 * 
 * PRUEBAS FINALES (Responsabilidad de Diego):
 * - Verificar conexión BD (implementada por Juan)
 * - Validar integración MVC completa
 * - Comprobar funcionalidad de todos los botones
 * - Probar flujos de navegación
 * - Validar permisos por rol
 * 
 * INTEGRACIÓN CON OTRAS CLASES:
 * - LoginFrame.java (Rufo): Recibe control tras autenticación
 * - ProductoFrame.java (Diego): Gestión de productos
 * - InventarioController.java (Diego): Lógica de inventario
 * - UsuarioController.java (Erick): Validación de permisos
 * - ReporteController.java (Rufo): Generación de reportes
 * 
 * CASOS DE USO RELACIONADOS:
 * - Todos los RF del sistema (RF01 - RF10)
 * - Centro de navegación principal
 * 
 * FECHA: Octubre 2025
 * 
 * @author Diego García
 * @version 1.0
 */
public class MenuPrincipal extends JFrame {
    private String rol;
    private InventarioController inventarioController;
    private ReporteController reporteController;

    public MenuPrincipal(String rol) {
        this.rol = rol;
        this.inventarioController = new InventarioController();
        this.reporteController = new ReporteController();

        setTitle("Menú Principal - Sistema Inventario Marbal");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initialize();
    }

    private void initialize() {
        // Panel principal con fondo blanco
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        // Panel del título celeste
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(0, 123, 255));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblTitulo = new JLabel("MENÚ PRINCIPAL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);

        // Panel de botones centrado
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Botones
        JButton btnInventario = crearBoton("Gestión de Inventario");
        JButton btnReportes = crearBoton("Reportes");
        JButton btnUsuarios = crearBoton("Gestión de Usuarios");
        JButton btnCerrarSesion = crearBoton("Cerrar Sesión");

        // Acciones
        btnInventario.addActionListener(e -> new ProductoFrame(inventarioController).setVisible(true));
        btnReportes.addActionListener(e -> new ReporteFrame(reporteController).setVisible(true));
        btnUsuarios.addActionListener(e -> JOptionPane.showMessageDialog(this, "Función Gestión Usuarios (pendiente)"));
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new LoginFrame(null).setVisible(true);
        });

        // Mostrar solo según rol
        gbc.gridy = 0;
        panelBotones.add(btnInventario, gbc);

        gbc.gridy = 1;
        panelBotones.add(btnReportes, gbc);

        if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {
            gbc.gridy = 2;
            panelBotones.add(btnUsuarios, gbc);
        }

        gbc.gridy = 3;
        panelBotones.add(btnCerrarSesion, gbc);

        // Ensamblar todo
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(250, 40));
        boton.setFont(new Font("Arial", Font.PLAIN, 14));
        boton.setBackground(new Color(240, 240, 240));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal("ADMINISTRADOR").setVisible(true));
    }
}