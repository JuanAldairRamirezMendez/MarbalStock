package vista;

import controlador.InventarioController;
import controlador.ReporteController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
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
        setSize(1100, 620);
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
        final JLabel title = new JLabel("MENÚ PRINCIPAL", SwingConstants.CENTER);
        title.setForeground(UIConstants.PANEL_BG);
        title.setFont(UIConstants.SECTION_FONT);
        header.add(title, BorderLayout.CENTER);

        // Encabezado simple (solo un rótulo "MENÚ PRINCIPAL")
        // evitamos duplicar el título con una 'pill' adicional

        // Barra lateral izquierda (menú simplificado y moderno)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIConstants.BACKGROUND);
        sidebar.setBorder(BorderFactory.createEmptyBorder(16, 12, 12, 12));
        sidebar.setPreferredSize(new Dimension(220, 0));

        // Logo / título compacto (usar icono generado para evitar recorte)
        JLabel logo = new JLabel(createLogoIcon(48));
        logo.setPreferredSize(new Dimension(64, 64));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel menuLabel = new JLabel("MARBAL - Repartición");
        menuLabel.setFont(UIConstants.SECTION_FONT);
        menuLabel.setForeground(UIConstants.TEXT_PRIMARY);
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuLabel.setBorder(BorderFactory.createEmptyBorder(8,0,18,0));

        sidebar.add(logo);
        sidebar.add(menuLabel);

        // Botones principales (solo lo necesario para reparto y control)
        JButton btnProductos = UIFactory.createRoundedButton("Productos", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 180, 36);
        JButton btnAsignaciones = UIFactory.createRoundedButton("Asignaciones", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 180, 36);
        JButton btnIngresar = UIFactory.createRoundedButton("Ingreso / Consumo", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 180, 36);
        JButton btnReportes = UIFactory.createRoundedButton("Reportes de Repartición", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 180, 36);
        JButton btnMovimientos = UIFactory.createRoundedButton("Movimientos", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 180, 36);
        JButton btnCerrarSesion = UIFactory.createRoundedButton("Cerrar Sesión", UIConstants.SECONDARY_BUTTON, UIConstants.TEXT_PRIMARY, 140, 30);

        // Forzar alineación y tamaño uniforme para una columna ordenada
        JButton[] mainButtons = new JButton[]{btnProductos, btnAsignaciones, btnIngresar, btnReportes, btnMovimientos};
        for (JButton b : mainButtons) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(180, 36));
        }
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrarSesion.setMaximumSize(new Dimension(160, 30));
        btnCerrarSesion.setMaximumSize(new Dimension(160, 30));

        // Acciones principales
        btnIngresar.addActionListener(e -> { AgregarProductoDialog dlg = new AgregarProductoDialog(this, inventarioController); dlg.setVisible(true); });
        btnProductos.addActionListener(e -> new GestionarProductosFrame(inventarioController).setVisible(true));
        btnAsignaciones.addActionListener(e -> new AsignacionFrame(inventarioController).setVisible(true));
        btnReportes.addActionListener(e -> new ReporteFrame(reporteController).setVisible(true));
        btnMovimientos.addActionListener(e -> new MovimientosFrame(inventarioController).setVisible(true));
        btnCerrarSesion.addActionListener(e -> { dispose(); new LoginFrame(null).setVisible(true); });

        // (Admin button removed to avoid interface errors)

        // Añadir botones con separación y alineación (columna centrada en el sidebar)
        sidebar.add(Box.createRigidArea(new Dimension(0,6)));
        sidebar.add(btnProductos);
        sidebar.add(Box.createRigidArea(new Dimension(0,8)));
        sidebar.add(btnAsignaciones);
        sidebar.add(Box.createRigidArea(new Dimension(0,8)));
        sidebar.add(btnIngresar);
        sidebar.add(Box.createRigidArea(new Dimension(0,8)));
        sidebar.add(btnReportes);
        sidebar.add(Box.createRigidArea(new Dimension(0,8)));
        sidebar.add(btnMovimientos);
        sidebar.add(Box.createVerticalGlue());
        JPanel footer = new JPanel();
        footer.setBackground(UIConstants.BACKGROUND);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        footer.add(btnCerrarSesion);
        sidebar.add(footer);

        panelPrincipal.add(header, BorderLayout.NORTH);
        panelPrincipal.add(sidebar, BorderLayout.WEST);

        // gran área central turquesa
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(UIConstants.PRIMARY);
        panelPrincipal.add(mainArea, BorderLayout.CENTER);
        // No añadimos un segundo botón de "Cerrar Sesión" en el bottom: ya está en el sidebar

        add(panelPrincipal);

        // Depuración: listar jerarquía de componentes para detectar duplicados
        SwingUtilities.invokeLater(() -> {
            System.out.println("[DEBUG] Component hierarchy for MenuPrincipal:");
            dumpComponentHierarchy(this, "");

            // Eliminar cualquier JLabel con texto "MENÚ PRINCIPAL" que no sea el título principal
            java.util.List<JLabel> duplicates = new java.util.ArrayList<>();
            collectLabelsWithText(this, "MENÚ PRINCIPAL", duplicates);
            for (JLabel lbl : duplicates) {
                if (lbl != title) {
                    Container p = lbl.getParent();
                    if (p != null) {
                        p.remove(lbl);
                        System.out.println("[DEBUG] Removed duplicate label from " + p.getClass().getName());
                    } else {
                        lbl.setVisible(false);
                        System.out.println("[DEBUG] Hid duplicate label (no parent)");
                    }
                }
            }
            this.revalidate();
            this.repaint();
        });
    }

    private JButton crearBoton(String texto) {
        return UIFactory.createRoundedButton(texto, UIConstants.PRIMARY, Color.WHITE, 250, 40);
    }

    // Método auxiliar para imprimir jerarquía de componentes (debug)
    private void dumpComponentHierarchy(Component comp, String indent) {
        if (comp == null) return;
        System.out.println(indent + comp.getClass().getName() + " - " + comp.toString());
        if (comp instanceof Container) {
            Component[] children = ((Container) comp).getComponents();
            for (Component c : children) {
                dumpComponentHierarchy(c, indent + "  ");
            }
        }
    }

    // Coleccionar labels con texto específico
    private void collectLabelsWithText(Component comp, String text, java.util.List<JLabel> out) {
        if (comp == null) return;
        if (comp instanceof JLabel) {
            JLabel l = (JLabel) comp;
            if (text.equals(l.getText())) out.add(l);
        }
        if (comp instanceof Container) {
            for (Component c : ((Container) comp).getComponents()) collectLabelsWithText(c, text, out);
        }
    }

    // Mostrar diálogo simple y modal para acciones admin
    private void showAdminDialog(String title, String message) {
        JDialog dlg = new JDialog(this, "Admin - " + title, Dialog.ModalityType.APPLICATION_MODAL);
        JPanel content = new JPanel(new BorderLayout(8,8));
        content.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        JLabel lbl = new JLabel(message);
        content.add(lbl, BorderLayout.CENTER);
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> dlg.dispose());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        south.add(ok);
        content.add(south, BorderLayout.SOUTH);
        dlg.setContentPane(content);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setVisible(true);
        // after close, ensure repaint to avoid visual artifacts
        this.revalidate();
        this.repaint();
    }

    // Crear un icono simple (caja) programáticamente para el logo
    private ImageIcon createLogoIcon(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // fondo transparente
            g.setComposite(AlphaComposite.Src);
            g.setColor(new Color(0,0,0,0));
            g.fillRect(0,0,size,size);

            // dibujar caja estilizada en color oscuro
            int pad = Math.max(4, size/10);
            int w = size - pad*2;
            int h = size - pad*2;
            g.setColor(new Color(60,60,60));
            Stroke old = g.getStroke();
            g.setStroke(new BasicStroke(Math.max(2, size/24), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            // contorno
            g.drawRoundRect(pad, pad, w, h, 6, 6);
            // tapa superior (perspectiva)
            g.drawLine(pad, pad+6, pad + w/2, pad);
            g.drawLine(pad + w/2, pad, pad + w, pad+6);
            // relleno ligero
            g.setColor(new Color(255,140,0,40));
            g.fillRoundRect(pad+2, pad+2, w-4, h-4, 6, 6);
            g.setStroke(old);
        } finally {
            g.dispose();
        }
        return new ImageIcon(img);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal("ADMINISTRADOR").setVisible(true));
    }
}