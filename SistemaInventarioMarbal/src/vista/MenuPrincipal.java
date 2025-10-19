package vista;

import controlador.ReporteController;
import java.awt.*;
import javax.swing.*;
import modelo.Usuario;

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
    private JButton btnGestionProductos;
    private JButton btnReportes;
    private JButton btnOrdenesCompra;
    private JButton btnUsuarios;
    private JButton btnCerrarSesion;
    private JLabel lblUsuario;
    private JLabel lblRol;

    private Usuario usuarioActual;

    public MenuPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        inicializarInterfaz();
        configurarEventos();
        aplicarPermisosPorRol();
    }

    private void inicializarInterfaz() {
        setTitle("Sistema de Inventario Marbal - Menú Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de información del usuario
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblUsuario = new JLabel("Usuario: " + usuarioActual.getNombre());
        lblRol = new JLabel("Rol: " + usuarioActual.getRol());
        panelUsuario.add(lblUsuario);
        panelUsuario.add(lblRol);
        add(panelUsuario, BorderLayout.NORTH);

        // Panel de botones principales
        JPanel panelBotones = new JPanel(new GridLayout(4, 2, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnGestionProductos = new JButton("Gestión de Productos");
        btnReportes = new JButton("Reportes del Sistema");
        btnOrdenesCompra = new JButton("Órdenes de Compra");
        btnUsuarios = new JButton("Gestión de Usuarios");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        panelBotones.add(btnGestionProductos);
        panelBotones.add(btnReportes);
        panelBotones.add(btnOrdenesCompra);
        panelBotones.add(btnUsuarios);

        // Espacios vacíos para mantener el layout
        panelBotones.add(new JLabel());
        panelBotones.add(new JLabel());
        panelBotones.add(new JLabel());
        panelBotones.add(btnCerrarSesion);

        add(panelBotones, BorderLayout.CENTER);

        // Panel de estado
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstado.add(new JLabel("Sistema de Inventario Marbal - Conectado"));
        add(panelEstado, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnGestionProductos.addActionListener(e -> abrirGestionProductos());

        btnReportes.addActionListener(e -> abrirReportes());

        btnOrdenesCompra.addActionListener(e -> abrirOrdenesCompra());

        btnUsuarios.addActionListener(e -> abrirGestionUsuarios());

        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private void aplicarPermisosPorRol() {
        // Control de acceso por rol
        if ("Operario".equals(usuarioActual.getRol())) {
            btnUsuarios.setEnabled(false);
            btnOrdenesCompra.setEnabled(false);
        }
    }

    private void abrirGestionProductos() {
        // Integración con ProductoFrame
        ProductoFrame productoFrame = new ProductoFrame();
        productoFrame.setVisible(true);
    }

    private void abrirReportes() {
        try {
            ReporteController rc = new ReporteController();
            java.util.List<java.util.Map<String, Object>> data = rc.generarReporteInventario(5);
            if (data == null || data.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay datos de inventario para exportar.");
                return;
            }
            String nombre = "reporte_inventario_" + java.time.LocalDate.now();
            rc.exportarReporte(data, nombre, "CSV");
            String ruta = new java.io.File(nombre + ".csv").getAbsolutePath();
            JOptionPane.showMessageDialog(this, "Reporte exportado en:\n" + ruta, "Reporte de Inventario", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar/exportar reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirOrdenesCompra() {
        JOptionPane.showMessageDialog(this,
                "Integración con OrdenCompraController (Erick Estrada)\n" +
                        "Módulo de Órdenes de Compra - En desarrollo");
    }

    private void abrirGestionUsuarios() {
        JOptionPane.showMessageDialog(this,
                "Integración con UsuarioController (Erick Estrada)\n" +
                        "Módulo de Gestión de Usuarios - En desarrollo");
    }

    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            dispose();
            JOptionPane.showMessageDialog(null, "Sesión cerrada exitosamente");
            // Regresar al LoginFrame en el hilo de UI
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        }
    }

    public static void main(String[] args) {
        // Para pruebas - crear un usuario de ejemplo
        Usuario usuarioPrueba = new Usuario(1, "Diego García", "Administrador");

        SwingUtilities.invokeLater(() -> {
            MenuPrincipal menu = new MenuPrincipal(usuarioPrueba);
            menu.setVisible(true);
        });
    }
}