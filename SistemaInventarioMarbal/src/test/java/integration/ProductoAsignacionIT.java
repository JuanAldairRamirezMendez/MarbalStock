import controlador.InventarioController;
import modelo.Producto;
import modelo.ProductoAsignacion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductoAsignacionIT {

    @Test
    public void crearYListarAsignacion() {
        InventarioController ctrl = new InventarioController();
        // crear producto de prueba
        Producto p = new Producto(0, "IT-TEST", "Producto IT Test", "OTROS", 50, 5, 1.0, 5.0, 0);
        boolean created = ctrl.agregarProducto(p);
        assertTrue(created, "No se pudo crear producto de prueba");
        assertTrue(p.getId() > 0 || p.getCodigo() != null);

        int productoId = p.getId();
        int clienteId = 9999;
        ProductoAsignacion a = new ProductoAsignacion(0, clienteId, productoId, 1200.0, "", true);
        boolean ok = ctrl.crearAsignacion(a);
        assertTrue(ok, "No se pudo crear asignación");

        boolean found = false;
        for (ProductoAsignacion x : ctrl.listarAsignaciones()) {
            if (x.getClienteId() == clienteId && x.getProductoId() == productoId) { found = true; break; }
        }
        assertTrue(found, "Asignación no listada después de creación");
    }
}
