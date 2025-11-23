package modelo;

public class ProductoTestRunner {
    public static void main(String[] args) {
        int failures = 0;

        // testConstructorAndGetters
        try {
            Producto p = new Producto(1, "Tornillo", 0.5, 10);
            if (p.getId() != 1) throw new AssertionError("id esperado 1");
            if (!"Tornillo".equals(p.getNombre())) throw new AssertionError("nombre esperado Tornillo");
            if (Math.abs(p.getPrecio() - 0.5) > 1e-9) throw new AssertionError("precio esperado 0.5");
            if (p.getStock() != 10) throw new AssertionError("stock esperado 10");
            System.out.println("testConstructorAndGetters: OK");
        } catch (AssertionError e) {
            System.err.println("testConstructorAndGetters: FAIL - " + e.getMessage());
            failures++;
        } catch (Exception e) {
            System.err.println("testConstructorAndGetters: ERROR - " + e.getMessage());
            failures++;
        }

        // testSettersAndHayStock
        try {
            Producto p = new Producto(2, "Tuerca", 0.2, 0);
            if (p.hayStock()) throw new AssertionError("esperado sin stock");
            p.setStock(5);
            if (!p.hayStock()) throw new AssertionError("esperado con stock");
            p.setPrecio(0.25);
            if (Math.abs(p.getPrecio() - 0.25) > 1e-9) throw new AssertionError("precio esperado 0.25");
            p.setNombre("Tuerca M5");
            if (!"Tuerca M5".equals(p.getNombre())) throw new AssertionError("nombre esperado Tuerca M5");
            System.out.println("testSettersAndHayStock: OK");
        } catch (AssertionError e) {
            System.err.println("testSettersAndHayStock: FAIL - " + e.getMessage());
            failures++;
        } catch (Exception e) {
            System.err.println("testSettersAndHayStock: ERROR - " + e.getMessage());
            failures++;
        }

        if (failures == 0) {
            System.out.println("ALL TESTS PASSED");
            System.exit(0);
        } else {
            System.err.println(failures + " tests failed");
            System.exit(1);
        }
    }
}
