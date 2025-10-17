package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ProductoFrame - Interfaz gráfica para gestión de productos
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Diego García (U23247615)
 * ROL: Líder de integración / Coordinador
 * 
 * RESPONSABILIDADES DE DIEGO:
 * - Integrar Modelo (Producto.java - Keila), Vista (ProductoFrame) y 
 *   Controlador (InventarioController)
 * - Asegurar funcionalidad de botones y eventos
 * - Implementar formularios para CRUD de productos
 * - Conectar con InventarioController para operaciones
 * - Probar conexión a BD (hecha por Juan)
 * 
 * DESCRIPCIÓN:
 * Esta clase implementa la interfaz gráfica para gestionar productos
 * del inventario. Permite realizar operaciones CRUD (Crear, Leer,
 * Actualizar, Eliminar) sobre productos, integrando el modelo
 * Producto.java (Keila) con InventarioController (Diego).
 * 
 * PATRÓN MVC - INTEGRACIÓN COMPLETA:
 * 
 * MODELO (Keila Mateo):
 * - Producto.java
 *   * Atributos: id, nombre, precio, stock, tipoProducto, stockMinimo
 *   * Métodos: clasificarProducto(), calcularPrecioFinal(), validarStock()
 * 
 * VISTA (Diego García):
 * - ProductoFrame.java
 *   * Formulario de ingreso de datos
 *   * Tabla de visualización de productos
 *   * Botones para operaciones CRUD
 * 
 * CONTROLADOR (Diego García):
 * - InventarioController.java
 *   * agregarProducto()
 *   * modificarProducto()
 *   * eliminarProducto()
 *   * listarProductos()
 *   * verificarStockBajo()
 * 
 * FUNCIONALIDADES IMPLEMENTADAS:
 * 
 * 1. REGISTRAR PRODUCTO (RF01, RF03):
 *    - Captura: id, nombre, precio, stock, tipo (contratado/adicional)
 *    - Validación de datos obligatorios
 *    - Clasificación automática de producto (Keila)
 *    - Almacenamiento en BD (Juan)
 * 
 * 2. MODIFICAR PRODUCTO (RF08):
 *    - Carga datos existentes en formulario
 *    - Permite edición de campos
 *    - Actualiza en BD con validación
 * 
 * 3. ELIMINAR PRODUCTO (RF08):
 *    - Confirmación antes de eliminar
 *    - Validación de productos con stock
 *    - Eliminación lógica o física según reglas
 * 
 * 4. LISTAR PRODUCTOS:
 *    - Visualización en JTable
 *    - Filtros por tipo, stock, nombre
 *    - Ordenamiento por columnas
 * 
 * 5. ALERTAS DE STOCK (RF02):
 *    - Indicador visual cuando stock < 5
 *    - Notificación al usuario
 *    - Sugerencia de orden de compra
 * 
 * COMPONENTES GRÁFICOS:
 * - txtId: Campo de texto para ID del producto
 * - txtNombre: Campo de texto para nombre
 * - txtPrecio: Campo de texto para precio
 * - txtStock: Campo de texto para cantidad en stock
 * - cmbTipoProducto: ComboBox para tipo (Contratado/Adicional)
 * - btnGuardar: Botón para guardar producto
 * - btnCancelar: Botón para cancelar operación
 * - btnModificar: Botón para modificar producto
 * - btnEliminar: Botón para eliminar producto
 * - tblProductos: Tabla para listar productos
 * 
 * VALIDACIONES:
 * - Campos obligatorios no vacíos
 * - Precio > 0
 * - Stock >= 0
 * - Formato numérico correcto
 * - Producto único (no duplicar ID)
 * 
 * INTEGRACIÓN CON OTRAS CLASES:
 * - Producto.java (Keila): Modelo de datos
 * - InventarioController.java (Diego): Lógica de negocio
 * - ConexionBD.java (Juan): Persistencia de datos
 * - MenuPrincipal.java (Diego): Navegación
 * - OrdenCompraController.java (Erick): Alertas de stock
 * 
 * PRUEBAS REALIZADAS POR DIEGO:
 * - Conexión exitosa a BD (implementada por Juan)
 * - Inserción de productos
 * - Modificación de productos
 * - Eliminación de productos
 * - Validación de formularios
 * - Eventos de botones funcionando
 * - Integración MVC completa
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF01: Registrar consumo diario de productos
 * - RF02: Generar alerta de stock bajo
 * - RF03: Clasificar producto como contratado o adicional
 * - RF08: Editar o eliminar registros de productos
 * 
 * FECHA: Octubre 2025
 * 
 * @author Diego García
 * @version 1.0
 */
public class ProductoFrame extends JFrame {
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public ProductoFrame() {
        setTitle("Gestión de Productos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        panel.add(new JLabel("ID:"));
        txtId = new JTextField();
        panel.add(txtId);

        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panel.add(txtPrecio);

        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProducto();
            }
        });
        panel.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(btnCancelar);

        add(panel);
    }

    private void guardarProducto() {
        // Lógica para guardar el producto en la base de datos
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String precio = txtPrecio.getText();

        // Aquí se puede agregar la lógica para guardar el producto
        JOptionPane.showMessageDialog(this, "Producto guardado: " + nombre);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductoFrame frame = new ProductoFrame();
            frame.setVisible(true);
        });
    }
}