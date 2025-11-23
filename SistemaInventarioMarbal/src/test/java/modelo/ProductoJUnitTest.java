package modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoJUnitTest {

    @Test
    void constructorAndGetters() {
        Producto p = new Producto(1, "Tornillo", 0.5, 10);
        assertEquals(1, p.getId());
        assertEquals("Tornillo", p.getNombre());
        assertEquals(0.5, p.getPrecio(), 1e-9);
        assertEquals(10, p.getStock());
    }

    @Test
    void settersAndHayStock() {
        Producto p = new Producto(2, "Tuerca", 0.2, 0);
        assertFalse(p.hayStock());
        p.setStock(5);
        assertTrue(p.hayStock());
        p.setPrecio(0.25);
        assertEquals(0.25, p.getPrecio(), 1e-9);
        p.setNombre("Tuerca M5");
        assertEquals("Tuerca M5", p.getNombre());
    }
}
