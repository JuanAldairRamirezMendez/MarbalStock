# Sistema Inventario Marbal

Este proyecto es un sistema de gestión de inventario que permite a los usuarios gestionar productos, proveedores, clientes y ventas de manera eficiente. A continuación se detallan las características y la estructura del proyecto.

## Estructura del Proyecto

```
SistemaInventarioMarbal
├── src
│   ├── conexion
│   │   └── ConexionBD.java
│   ├── modelo
│   │   ├── Producto.java
│   │   ├── Proveedor.java
│   │   ├── Usuario.java
│   │   ├── Cliente.java
│   │   ├── Venta.java
│   │   ├── OrdenCompra.java
│   │   └── Reporte.java
│   ├── controlador
│   │   ├── InventarioController.java
│   │   ├── OrdenCompraController.java
│   │   ├── UsuarioController.java
│   │   └── ReporteController.java
│   └── vista
│       ├── LoginFrame.java
│       ├── MenuPrincipal.java
│       └── ProductoFrame.java
├── recursos
│   └── bd
│       └── script_bd.sql
└── README.md
```
# Documentación y Justificación de Patrones de Arquitectura: MVC y Variantes

## 1. Introducción
Este documento unifica la **documentación, justificación, ejemplos de código y guías de uso** del patrón MVC y sus variantes, además de un bloque listo para copiar y pegar en un proyecto (README + Informe Técnico). El objetivo es que todo se encuentre en un solo lugar para facilitar la entrega académica y su implementación práctica.

## 2. Marco Teórico: Patrón MVC

El patrón **Modelo–Vista–Controlador (MVC)** es un patrón de arquitectura que separa la aplicación en tres capas:

* **Modelo (Model):** Contiene los datos y la lógica de negocio.
* **Vista (View):** Contiene la interfaz de usuario (lo que ve el usuario).
* **Controlador (Controller):** Actúa como intermediario entre el modelo y la vista.

### 2.1 Flujo de funcionamiento

1. El usuario interactúa con la Vista.
2. El Controlador recibe el evento.
3. El Controlador consulta o modifica el Modelo.
4. El Modelo devuelve los datos.
5. La Vista se actualiza.

Esto permite una mejor organización, mantenimiento y escalabilidad del sistema.

## 3. Justificación de Elección de MVC

Se eligió MVC para el proyecto **MARBALSTOCK** debido a:

1. Facilita la separación de responsabilidades.
2. Permite modificar la interfaz sin cambiar la lógica del negocio.
3. Facilita el trabajo en equipo.
4. Mejora la mantenibilidad.
5. Es un estándar común en sistemas académicos y empresariales.

## 4. Variantes del Patrón MVC

### 4.1 MVP (Model View Presenter)

* El Presenter actúa como intermediario.
* La Vista es más pasiva.
* Muy usado en aplicaciones móviles.

### 4.2 MVVM (Model View ViewModel)

* Utiliza data binding.
* Muy usado en WPF, Angular, Vue.
* Reduce la dependencia directa entre capas.

### 4.3 MVC en Escritorio (Java Swing)

Es la variante usada en MARBALSTOCK.

## 5. Estructura del Proyecto MVC

```
src/
 ├── conexion/     -> Conexión con BD
 ├── modelo/       -> Clases entidad
 ├── controlador/  -> Lógica del sistema
 └── vista/        -> Interfaces gráficas
```

---

## 6. Ejemplo de Código (MVC en Java)

### 6.1 Modelo

```java
public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }
    // Getters y Setters
}
```

### 6.2 Controlador

```java
public class ProductoController {
    private ProductoDAO dao;

    public ProductoController() {
        this.dao = new ProductoDAO();
    }

    public void registrarProducto(Producto p) {
        dao.insertar(p);
    }
}
```

### 6.3 Vista

```java
public class ProductoVista extends JFrame {
    JButton btnGuardar = new JButton("Guardar");

    public ProductoVista() {
        btnGuardar.addActionListener(e -> {
            ProductoController controller = new ProductoController();
            Producto p = new Producto(1, "Mouse", 50.0, 10);
            controller.registrarProducto(p);
        });
    }
}
```

---

## 7. Guía de Uso de MVC en tu Proyecto

1. Nunca mezclar lógica con interfaz.
2. La base de datos se accede solo desde el Modelo o DAO.
# Sistema Inventario Marbal

Proyecto Java (Swing) para la gestión de inventario desarrollado para Inversiones Comerciales Marbal E.I.R.L.

## Resumen rápido
- Aplicación de escritorio (Java Swing) con patrón MVC.
- Persistencia por JDBC (MySQL configurado por defecto en `recursos/bd/script_bd.sql`).

## Requisitos
- JDK 17+ instalado (el proyecto se probó con JDK 25 localmente).
- Apache Maven (para build y tests).

## Archivos importantes
- `src/` : código fuente (se conservó la estructura original del proyecto).
- `recursos/bd/script_bd.sql` : script de creación de base de datos y datos precargados.
- `pom.xml` : archivo de build Maven (añadido para compilar, testear y empacar un JAR con dependencias).
- `.github/workflows/ci.yml` : workflow de CI que construye, ejecuta tests y sube el JAR como artifact.

## Compilar y generar el JAR (local)
1. Abrir PowerShell en la carpeta del proyecto:

```powershell
cd 'C:\Users\aldai\Downloads\proyectos\MarbalStock\SistemaInventarioMarbal'
mvn -B -DskipTests=false clean package
```

2. Al terminar el build el JAR empaquetado ("shaded") estará en:

```
target\SistemaInventarioMarbal-1.0.0-shaded.jar
```

## Ejecutar la aplicación (JAR)

```powershell
& 'C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot\bin\java.exe' -jar .\target\SistemaInventarioMarbal-1.0.0-shaded.jar
```

Notas:
- El JAR incluye las dependencias (MySQL Connector incluido). No hace falta copiar manualmente el connector.
- La aplicación es una GUI Swing; asegúrate de ejecutar en un entorno con servidor X/GUI (Windows, macOS, Linux con entorno gráfico).

## Pruebas (tests)
- Se añadió un test mínimo (`SecurityUtilTest`) que se ejecuta con `mvn test` durante el build.

## Integración continua (GitHub Actions)
- El workflow está en `.github/workflows/ci.yml` y hace:
    - Checkout del repo
    - Setup JDK 17
    - `mvn -B -DskipTests=false clean package`
    - Sube `target/*.jar` como artifact (nombre: `sistema-marbal-jar`)

### Para activar CI en GitHub
1. Commit y push de los cambios (incluyendo `pom.xml` y `.github/workflows/ci.yml`) a la rama `main`.
2. En la pestaña `Actions` del repositorio verás el job ejecutándose; al completarse, abre el run y descarga el artifact `sistema-marbal-jar`.

## Recomendaciones y pasos siguientes
- Seguridad: cambiar SHA-256 simple por PBKDF2/BCrypt/Argon2 antes de poner en producción.
- Tests: ampliar con tests de integración (H2 en memoria) para `ConexionBD`, `UsuarioController` e `InventarioController`.
- Si quieres que el CI use tu versión local de Java (ej. JDK 25), puedo actualizar el workflow para usarla (algunos runners disponen solo de LTS por defecto).

## Contacto
- Si quieres que actualice el `README` con comandos adicionales, tests o que cree releases automáticas, dime y lo hago.
```sql

CREATE DATABASE marbalstock;

```

