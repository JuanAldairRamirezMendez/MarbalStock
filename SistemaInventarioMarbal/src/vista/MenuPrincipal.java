package vista;

import controlador.InventarioController;
import controlador.ReporteController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import vista.RoundedBorder;

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

        setTitle("Sistema de Inventario MARBAL - Menú Principal");
        setSize(780, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initialize();
    }

    private void initialize() {
        // Panel principal con estilo similar a las capturas
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(UIConstants.BACKGROUND);

        // Encabezado
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConstants.HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        JLabel title = new JLabel("MENÚ PRINCIPAL", SwingConstants.CENTER);
        title.setForeground(UIConstants.PANEL_BG);
        title.setFont(UIConstants.SECTION_FONT);
        header.add(title, BorderLayout.CENTER);

        // Barra tipo 'pill' debajo del encabezado (estilo visual)
        JPanel pillBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        pillBar.setBackground(UIConstants.BACKGROUND);
        JLabel pillLabel = new JLabel("  MENÚ PRINCIPAL  ", SwingConstants.CENTER);
        pillLabel.setOpaque(true);
        pillLabel.setBackground(UIConstants.PRIMARY);
        pillLabel.setForeground(UIConstants.PANEL_BG);
        pillLabel.setFont(UIConstants.SECTION_FONT);
        pillLabel.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        pillBar.add(pillLabel);
        // Agrupar header + pill
        JPanel headerWrap = new JPanel(new BorderLayout());
        headerWrap.setBackground(UIConstants.BACKGROUND);
        headerWrap.add(header, BorderLayout.NORTH);
        headerWrap.add(pillBar, BorderLayout.SOUTH);

        // Panel de botones 2x3
        JPanel botonesGrid = new JPanel(new GridLayout(2, 3, 10, 10));
        botonesGrid.setBackground(UIConstants.BACKGROUND);
        botonesGrid.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton btnIngresar = crearBoton("➕  Ingresar Productos");
        JButton btnGestionar = crearBoton("⚙️  Gestionar Productos");
        JButton btnResumen = crearBoton("📊  Ver Resumen");
        JButton btnReportes = crearBoton("🧾  Reportes");
        JButton btnMovimientos = crearBoton("📦  Movimientos");
        JButton btnCerrarSesion = crearBoton("Cerrar Sesión");

        btnIngresar.addActionListener(e -> new ProductoFrame(inventarioController).setVisible(true));
        btnGestionar.addActionListener(e -> new ProductoFrame(inventarioController).setVisible(true));
        btnResumen.addActionListener(e -> JOptionPane.showMessageDialog(this, "Resumen (pendiente)"));
        btnReportes.addActionListener(e -> new ReporteFrame(reporteController).setVisible(true));
        btnMovimientos.addActionListener(e -> new MovimientosFrame(inventarioController).setVisible(true));
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new LoginFrame(null).setVisible(true);
        });

        botonesGrid.add(btnIngresar);
        botonesGrid.add(btnGestionar);
        botonesGrid.add(btnResumen);
        botonesGrid.add(btnReportes);
        botonesGrid.add(btnMovimientos);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(UIConstants.BACKGROUND);
        // Botón de cerrar sesión más pequeño y a la izquierda
        btnCerrarSesion.setPreferredSize(new Dimension(120, 28));
        btnCerrarSesion.setBackground(UIConstants.SECONDARY_BUTTON);
        btnCerrarSesion.setForeground(UIConstants.TEXT_PRIMARY);
        bottom.add(btnCerrarSesion);

        panelPrincipal.add(headerWrap, BorderLayout.NORTH);
        panelPrincipal.add(botonesGrid, BorderLayout.CENTER);
        panelPrincipal.add(bottom, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JButton crearBoton(String texto) {
        return UIFactory.createRoundedButton(texto, UIConstants.PRIMARY, Color.WHITE, 250, 40);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal("ADMINISTRADOR").setVisible(true));
    }
}