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

## Descripción de Archivos

- **src/conexion/ConexionBD.java**: Clase encargada de establecer la conexión con la base de datos, incluyendo métodos para abrir y cerrar la conexión.
  
- **src/modelo/**: Contiene las clases que representan las entidades del sistema:
  - `Producto.java`: Representa un producto en el inventario.
  - `Proveedor.java`: Representa a un proveedor de productos.
  - `Usuario.java`: Representa a un usuario del sistema.
  - `Cliente.java`: Representa a un cliente.
  - `Venta.java`: Representa una venta realizada.
  - `OrdenCompra.java`: Representa una orden de compra.
  - `Reporte.java`: Genera reportes del sistema.

- **src/controlador/**: Contiene las clases que gestionan la lógica del sistema:
  - `InventarioController.java`: Gestiona operaciones relacionadas con el inventario.
  - `OrdenCompraController.java`: Gestiona operaciones relacionadas con las órdenes de compra.
  - `UsuarioController.java`: Gestiona operaciones relacionadas con los usuarios.
  - `ReporteController.java`: Gestiona la generación y exportación de reportes.

- **src/vista/**: Contiene las clases que representan la interfaz gráfica del sistema:
  - `LoginFrame.java`: Interfaz para el inicio de sesión del usuario.
  - `MenuPrincipal.java`: Interfaz del menú principal del sistema.
  - `ProductoFrame.java`: Interfaz para la gestión de productos.

- **recursos/bd/script_bd.sql**: Script SQL para crear y configurar la base de datos necesaria para el sistema.

## Instrucciones de Instalación

1. Clona este repositorio en tu máquina local.
2. Asegúrate de tener Java y un servidor de base de datos configurados.
3. Ejecuta el script `script_bd.sql` para crear la base de datos.
4. Compila y ejecuta el proyecto desde tu entorno de desarrollo.

## Uso

Inicia el sistema y utiliza la interfaz gráfica para gestionar productos, proveedores, clientes y ventas.