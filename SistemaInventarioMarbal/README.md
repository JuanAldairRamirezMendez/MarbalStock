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
3. El controlador nunca debe contener código gráfico.
4. La vista solo llama funciones del controlador.

---

## 8. DOCUMENTO LISTO PARA PEGAR (README + INFORME)

### 8.1 README.md (Para GitHub y entrega en clase)

```markdown
# Sistema de Inventario MARBALSTOCK

Sistema de gestión de inventario desarrollado para **Inversiones Comerciales Marbal E.I.R.L.**, utilizando Java y arquitectura **MVC (Modelo - Vista - Controlador)**.

---
## Descripción del Proyecto
Este sistema permite gestionar productos, stock y movimientos de inventario de manera organizada, reduciendo errores y mejorando el control interno del almacén.

---
## Tecnologías Utilizadas
- Java 17
- MySQL / PostgreSQL
- Apache Maven
- Java Swing (Interfaz gráfica)
- JDBC

---
## Estructura del Proyecto (MVC)
```

src/
├── conexion/     -> Conexión a base de datos
├── modelo/       -> Clases de negocio
├── controlador/  -> Lógica del sistema
└── vista/        -> Interfaces gráficas

````

---
## Requisitos Previos
Antes de ejecutar, asegúrese de tener instalado:
- JDK 17+
- MySQL o PostgreSQL
- Apache Maven
- PowerShell (Windows)

Verificar:
```bash
java -version
mvn -version
````

---

## Instalación y Configuración

1. Clonar repositorio:

```bash
git clone https://github.com/tu-usuario/marbalstock.git
```

2. Crear base de datos:

```sql
CREATE DATABASE marbalstock;
```

3. Ejecutar script:
   Ubicado en: `recursos/bd/script_bd.sql`

4. Configurar conexión:
   En:

```
src/conexion/ConexionBD.java
```

Ejemplo:

```java
private static final String URL = "jdbc:mysql://localhost:3306/marbalstock";
private static final String USER = "root";
private static final String PASSWORD = "";
```

---

## Ejecución del Sistema

Desde consola:

```bash
mvn clean package
java -jar target/marbalstock.jar
```

O desde el IDE ejecutando:
`vista.LoginFrame`

---

## Funcionalidades Principales

* Registro de productos
* Gestión de stock
* Control de entradas y salidas
* Reportes básicos
* Login de usuarios

---

## Limitaciones

* No soporta uso multiusuario en red
* No incluye reportes PDF avanzados
* No maneja múltiples almacenes

---

## Autor

Poll M-L
Estudiante de Ingeniería de Sistemas
UTP



