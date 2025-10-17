# REPARTICIÓN DE TAREAS - SISTEMA DE INVENTARIO MARBAL

## SISTEMA DE INVENTARIO - Inversiones Comerciales Marbal E.I.R.L.
**Proyecto Académico - Análisis y Diseño de Sistemas de Información**  
**Sección 40833**  
**Docente:** Bances Saavedra, David Enrique  
**Fecha:** Octubre 2025

---

## 🎯 OBJETIVO GENERAL DEL PROYECTO

Desarrollar un sistema de inventario en Java que permita a la empresa Inversiones Comerciales Marbal E.I.R.L. controlar el consumo diario de insumos, reemplazando el uso de Excel para garantizar mayor precisión, eficiencia y trazabilidad con los productos contratados.

---

## 👥 INTEGRANTES Y RESPONSABILIDADES

### 1. **Diego García Navarro (U23247615)**
**ROL:** Líder de Integración / Coordinador

#### 📁 Carpetas Asignadas:
- `controlador/`
- `vista/`

#### 📄 Archivos Asignados:
- `MenuPrincipal.java`
- `ProductoFrame.java`
- `InventarioController.java`

#### ✅ Responsabilidades:
- Integrar Modelo, Vista y Controlador (Patrón MVC)
- Asegurar funcionalidad de botones y eventos
- Probar conexión a BD (implementada por Juan)
- Realizar pruebas finales del sistema
- Implementar navegación entre módulos
- Controlar acceso a funciones según rol de usuario

#### 🔧 Algoritmos Clave:
- Integración MVC completa
- Gestión de eventos de interfaz gráfica
- Control de flujo de navegación del sistema

---

### 2. **Keila Mateo Luis (U23262823)**
**ROL:** Desarrollo de Modelo - Entidades

#### 📁 Carpeta Asignada:
- `modelo/`

#### 📄 Archivos Asignados:
- `Producto.java`
- `Proveedor.java`
- `Cliente.java`
- `Venta.java`
- `Usuario.java` (colaboración con Erick)
- `OrdenCompra.java` (colaboración con Erick)
- `Reporte.java` (colaboración con Rufo)

#### ✅ Responsabilidades:
- Crear atributos, constructores, getters y setters para todas las entidades
- Implementar algoritmo de clasificación de productos (contratado/adicional)
- Calcular precio final y ganancia por venta
- Validar stock y tipo de producto
- Definir relaciones entre entidades

#### 🔧 Algoritmos Clave:
- Clasificación de productos (CONTRATADO vs ADICIONAL)
- Cálculo de precio final con márgenes de ganancia
- Validación de stock y niveles críticos
- Algoritmos de negocio para entidades

---

### 3. **Juan Aldair Ramírez Méndez (U20201597)**
**ROL:** Conexión a Base de Datos + Script SQL

#### 📁 Carpetas Asignadas:
- `conexion/`
- `recursos/bd/`

#### 📄 Archivos Asignados:
- `ConexionBD.java`
- `script_bd.sql`

#### ✅ Responsabilidades:
- Programar conexión JDBC a MySQL
- Probar conexión desde clase test
- Crear tablas: producto, usuario, venta, proveedor, orden_compra, cliente, reporte
- Definir claves foráneas y relaciones entre tablas
- Documentar parámetros de conexión (usuario, contraseña, puerto)
- Implementar patrón Singleton para la conexión

#### 🔧 Algoritmos Clave:
- Conexión JDBC con MySQL
- Gestión de transacciones
- Script SQL completo con DDL y DML
- Manejo de excepciones de BD

#### 📊 Base de Datos:
- **Motor:** MySQL
- **Puerto:** 3306
- **Esquema:** marbal_inventario
- **Arquitectura:** Cliente-servidor

---

### 4. **Erick Jesús Estrada Cárdenas (U22302925)**
**ROL:** Controladores de Lógica y Seguridad

#### 📁 Carpeta Asignada:
- `controlador/`

#### 📄 Archivos Asignados:
- `UsuarioController.java`
- `OrdenCompraController.java`

#### ✅ Responsabilidades:
- Implementar encriptación de contraseñas (SHA-256)
- Programar autenticación de usuario (login)
- Crear generación automática de órdenes de compra (stock < 5)
- Validar roles (Administrador, Operario)
- Gestionar permisos de acceso a módulos

#### 🔧 Algoritmos Clave:
- **Encriptación SHA-256** para contraseñas
- **Autenticación de usuarios** con validación de roles
- **Generación automática de órdenes de compra** cuando stock < 5
- Control de sesión y auditoría de accesos

#### 🔐 Seguridad:
- Contraseñas encriptadas (nunca en texto plano)
- Validación de roles: ADMINISTRADOR vs OPERARIO
- Límite de intentos fallidos de login
- Registro de auditoría de accesos

---

### 5. **Rufo Piero Ferrel Julca (U23231492)**
**ROL:** Interfaz Gráfica - Login y Reportes

#### 📁 Carpetas Asignadas:
- `vista/`
- `controlador/`

#### 📄 Archivos Asignados:
- `LoginFrame.java`
- `ReporteController.java`

#### ✅ Responsabilidades:
- Diseñar formulario de inicio de sesión (login)
- Conectar con UsuarioController para validar usuario
- Crear controlador para reportes de inventario y ventas
- Mostrar reportes en JTable o JTextArea
- Exportar reportes a formato imprimible (PDF)

#### 🔧 Algoritmos Clave:
- **Login visual** con validación de credenciales
- **Generación de reportes** (Inventario, Ventas, Consumo, Órdenes)
- Visualización de datos en JTable/JTextArea
- Exportación a PDF para firma

#### 📊 Tipos de Reportes:
1. **Reporte de Inventario:** Stock actual, productos críticos, valorización
2. **Reporte de Ventas:** Por período, cliente, producto
3. **Reporte de Consumo Diario:** Registro de consumos
4. **Reporte de Órdenes de Compra:** Pendientes, enviadas, completadas

---

## 📋 REQUERIMIENTOS FUNCIONALES (RF)

| ID | Requerimiento | Responsable |
|---|---|---|
| **RF01** | Registrar consumo diario de productos | Keila (Modelo), Diego (Vista/Controller) |
| **RF02** | Generar alerta de stock bajo | Keila (Modelo), Erick (Lógica) |
| **RF03** | Clasificar producto como contratado o adicional | Keila (Modelo) |
| **RF04** | Generar orden de compra automática (stock < 5) | Erick (Controller) |
| **RF05** | Generar reportes de ventas o inventario | Rufo (Controller) |
| **RF06** | Validar límite diario de consumo | Keila (Modelo), Diego (Controller) |
| **RF07** | Gestionar usuarios y permisos | Erick (Controller) |
| **RF08** | Editar o eliminar registros de productos | Diego (Controller/Vista) |
| **RF09** | Centralizar información en base de datos relacional | Juan (BD) |
| **RF10** | Permitir impresión de reportes con formato para firma | Rufo (Reportes) |

---

## 🏗️ ARQUITECTURA DEL SISTEMA

### **Patrón de Diseño:** MVC (Model-View-Controller)

```
┌─────────────────────────────────────────────────────────────┐
│                    ARQUITECTURA MVC                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  MODELO (Keila Mateo)                                      │
│  ├── Producto.java                                         │
│  ├── Proveedor.java                                        │
│  ├── Cliente.java                                          │
│  ├── Venta.java                                            │
│  ├── Usuario.java                                          │
│  ├── OrdenCompra.java                                      │
│  └── Reporte.java                                          │
│                                                             │
│  VISTA (Diego García + Rufo Ferrel)                        │
│  ├── MenuPrincipal.java (Diego)                            │
│  ├── ProductoFrame.java (Diego)                            │
│  └── LoginFrame.java (Rufo)                                │
│                                                             │
│  CONTROLADOR (Diego + Erick + Rufo)                        │
│  ├── InventarioController.java (Diego)                     │
│  ├── UsuarioController.java (Erick)                        │
│  ├── OrdenCompraController.java (Erick)                    │
│  └── ReporteController.java (Rufo)                         │
│                                                             │
│  CONEXIÓN BD (Juan Ramírez)                                │
│  ├── ConexionBD.java                                       │
│  └── script_bd.sql                                         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔄 FLUJOS DE TRABAJO PRINCIPALES

### **1. Flujo de Autenticación:**
1. Usuario abre sistema → `LoginFrame.java` (Rufo)
2. Ingresa credenciales → `UsuarioController.autenticar()` (Erick)
3. Valida con SHA-256 → `ConexionBD.query()` (Juan)
4. Si válido → Redirige a `MenuPrincipal.java` (Diego)
5. Control de acceso por rol → Muestra opciones permitidas

### **2. Flujo de Registro de Producto:**
1. Usuario selecciona "Gestión de Inventario" en menú (Diego)
2. Abre `ProductoFrame.java` → Ingresa datos (Diego)
3. Valida formulario → Llama `InventarioController.agregarProducto()` (Diego)
4. Crea objeto `Producto.java` → Clasifica tipo (Keila)
5. Persiste en BD → `ConexionBD.ejecutarInsert()` (Juan)
6. Muestra confirmación → Actualiza tabla

### **3. Flujo de Alerta de Stock y Orden Automática:**
1. Sistema monitorea stock en tiempo real → `InventarioController` (Diego)
2. Detecta stock < 5 → Genera alerta visual (Diego)
3. Notifica a `OrdenCompraController.generarOrdenAutomatica()` (Erick)
4. Crea `OrdenCompra.java` con proveedor asignado (Keila)
5. Almacena en BD → Notifica al encargado (Juan)

### **4. Flujo de Generación de Reportes:**
1. Usuario selecciona "Reportes" en menú (Diego)
2. Elige tipo de reporte → `ReporteController.generarReporte()` (Rufo)
3. Consulta datos en BD → `ConexionBD.query()` (Juan)
4. Procesa y formatea datos → Crea `Reporte.java` (Keila)
5. Muestra en JTable/JTextArea → Opción de exportar a PDF (Rufo)

---

## 📊 CONTEXTO DE NEGOCIO

### **Empresa:** Inversiones Comerciales Marbal E.I.R.L.
- **RUC:** 20552534124
- **Actividad:** Venta y distribución de materiales de construcción, alimentos y bebidas al por mayor

### **Problema Actual:**
- ❌ Gestión manual en Excel y papel
- ❌ Falta de precisión en control de stock
- ❌ Pérdida de oportunidades de venta
- ❌ Procesos de reposición ineficientes
- ❌ Limitaciones para generar reportes confiables
- ❌ Alto riesgo de errores humanos

### **Solución Propuesta:**
- ✅ Sistema de inventario digital en Java
- ✅ Base de datos MySQL centralizada
- ✅ Información en tiempo real
- ✅ Alertas automáticas de stock bajo
- ✅ Órdenes de compra automáticas
- ✅ Reportes confiables para toma de decisiones

---

## 🛠️ TECNOLOGÍAS UTILIZADAS

- **Lenguaje:** Java (JDK 8 o superior)
- **GUI:** Java Swing
- **Base de Datos:** MySQL
- **Conexión:** JDBC
- **Patrón:** MVC
- **Encriptación:** SHA-256
- **IDE:** Eclipse / NetBeans / IntelliJ IDEA

---

## 📅 CRONOGRAMA DE ENTREGA

Este documento fue creado como parte del **Avance 02** del proyecto.

**Próximos pasos:**
- Implementación completa de cada módulo por responsable
- Pruebas de integración (Diego)
- Pruebas de conexión BD (Juan)
- Validación de seguridad (Erick)
- Revisión de interfaces (Rufo)
- Documentación final

---

## 📞 CONTACTO

Para consultas sobre este proyecto:
- **Docente:** Bances Saavedra, David Enrique
- **Curso:** Análisis y Diseño de Sistemas de Información
- **Sección:** 40833
- **Fecha:** Octubre 2025

---

**Elaborado por:** Equipo de Desarrollo - Sistema de Inventario Marbal  
**Última actualización:** 17 de Octubre de 2025
