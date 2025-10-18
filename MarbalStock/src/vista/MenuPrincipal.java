/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import conexion.ConexionBD;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * ---------------------------------------------------------------
 * CLASE: MenuPrincipal
 * INTEGRANTE: Diego García
 * ---------------------------------------------------------------
 * Esta clase representa la ventana principal del sistema.
 * Desde aquí el usuario puede:
 *  - Acceder a los módulos (por ejemplo: Productos)
 *  - Probar la conexión a la base de datos
 *  - Cerrar el sistema
 * Se integra con:
 *  - vista.ProductoFrame  → ventana del módulo de productos
 *  - conexion.ConexionBD  → clase que maneja la conexión MySQL
 * ---------------------------------------------------------------
 */
public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        initUI();
    }

    //Método que configura la interfaz del menú principal
    private void initUI() {
        setTitle("Menú Principal - Sistema Inventario Marbal");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Barra de menú
        JMenuBar menuBar = new JMenuBar();
         // Menú principal "Archivo"
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem menuSalir = new JMenuItem(new AbstractAction("Salir") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        menuArchivo.add(menuSalir);
        
        // Menú "Módulos"
        JMenu menuModulo = new JMenu("Módulos");
        JMenuItem menuProductos = new JMenuItem(new AbstractAction("Productos") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir la ventana de productos
                ProductoFrame productoFrame = new ProductoFrame();
                productoFrame.setVisible(true);
            }
        });
        menuModulo.add(menuProductos);
        
        // Menú "Herramientas" → sirve para probar conexión a la BD
        JMenu menuHerramientas = new JMenu("Herramientas");
        JMenuItem menuProbarBD = new JMenuItem(new AbstractAction("Probar Conexión a BD") {
            @Override
            public void actionPerformed(ActionEvent e) {
                testConexionBD();
            }
        });
        menuHerramientas.add(menuProbarBD);

         // Agregar menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuModulo);
        menuBar.add(menuHerramientas);

        setJMenuBar(menuBar);

        // Contenido simple
        JLabel lbl = new JLabel("<html><center>Sistema de Inventario - Marbal<br>Usuario: (ingrese desde Login)</center></html>", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 18));
        add(lbl, BorderLayout.CENTER);
    }

    private void testConexionBD() {
        // Intenta conectar usando conexion.ConexionBD 
        try {
            if (ConexionBD.conectar() != null) {
                JOptionPane.showMessageDialog(this, "✅ Conexión a la base de datos exitosa.", "Conexión BD", JOptionPane.INFORMATION_MESSAGE);
                ConexionBD.desconectar();
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo conectar (ConexionBD devolvió null). Revisa parámetros.", "Conexión BD", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error al probar conexión:\n" + ex.getMessage(), "Conexión BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Lanzador rápido para pruebas
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal mp = new MenuPrincipal();
            mp.setVisible(true);
        });
    }
}
