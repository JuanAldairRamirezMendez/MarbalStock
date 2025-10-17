package controlador;

import java.util.ArrayList;
import modelo.Producto;

/**
 * InventarioController - Controlador para gestión del inventario
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
 * - Probar conexión a BD (hecha por Juan)
 * - Realizar pruebas finales del sistema
 * 
 * DESCRIPCIÓN:
 * Este controlador gestiona las operaciones CRUD sobre productos del
 * inventario, implementando la lógica de negocio para:
 * - Registrar consumo diario de productos (RF01)
 * - Editar o eliminar registros de productos (RF08)
 * - Controlar stock y generar alertas (RF02)
 * 
 * PATRÓN DE DISEÑO: MVC (Model-View-Controller)
 * - Modelo: Producto.java (Keila Mateo)
 * - Vista: ProductoFrame.java (Diego García)
 * - Controlador: InventarioController.java (Diego García)
 * 
 * INTEGRACIÓN:
 * Este controlador se conecta con:
 * - ConexionBD.java (Juan Ramírez) para persistencia de datos
 * - ProductoFrame.java (Diego García) para interfaz gráfica
 * - OrdenCompraController.java (Erick Estrada) para órdenes automáticas
 * 
 * CASOS DE USO RELACIONADOS:
 * - RF01: Registrar consumo diario de productos
 * - RF02: Generar alerta de stock bajo
 * - RF08: Editar o eliminar registros de productos
 * 
 * FECHA: Octubre 2025
 * 
 * @author Diego García
 * @version 1.0
 */
public class InventarioController {
    private ArrayList<Producto> productos;

    public InventarioController() {
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void eliminarProducto(int id) {
        productos.removeIf(producto -> producto.getId() == id);
    }

    public void modificarProducto(Producto productoModificado) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == productoModificado.getId()) {
                productos.set(i, productoModificado);
                break;
            }
        }
    }

    public ArrayList<Producto> listarProductos() {
        return productos;
    }
}