# Guía de Estilos UI — MARBAL Stock

Resumen compacto de colores, tipografías y tamaños para mantener consistencia visual en la aplicación.

## Paleta de colores
- Primario (botones/accents): `#4885ED` (RGB 72,133,237)
- Primario oscuro (borde/hover pressed): `#3C78D7` (RGB 60,120,215)
- Secundario (header): `#165384` (RGB 22,83,132)
- Fondo general: `#F5F7FA` (RGB 245,247,250)
- Fondo tarjetas/paneles: `#FFFFFF` (blanco)
- Texto principal: `#222222` (oscuro)
- Texto secundario: `#6B7280` (gris)

## Tipografías
- Fuente principal: `Segoe UI`, fallback `Arial`.
- Tamaños recomendados:
  - Títulos grandes/header: 18–22 px (ej. `new Font("Segoe UI", Font.BOLD, 20)`)
  - Títulos/Secciones: 14–16 px
  - Botones: 13–14 px (negrita)
  - Texto de tabla/campos: 12–13 px

## Componentes
- Botón (estándar):
  - Tamaño recomendado: `250 x 40` (para menú principal) o `~140 x 34` para acciones secundarias
  - Radio de borde: `14–16 px` (esquinas redondeadas)
  - Color fondo: primario `#4885ED`, texto blanco
  - Borde: 1px con color primario oscuro
  - Efecto hover: `brighter()` del color base
  - Efecto pressed: `darker()` del color base

- Input / TextField:
  - Fondo blanco, borde inferior gris fino (`MatteBorder` 1px)
  - Altura: ~28–34 px

- Tablas:
  - Header: `Segoe UI Bold 14`, fondo `#F0F0F0`
  - Celdas: `Segoe UI 13`, filas de 22–28 px de alto

## Reglas de uso
- Mantener contraste suficiente entre texto y fondo (ratio WCAG mínimo recomendado para producción).
- Reutilizar `UIConstants` (clase Java) para colores y tamaños desde el código.
- Usar `UIFactory.createRoundedButton(...)` para crear botones con el estilo estándar y mantener consistencia.

## Ejemplos (Java)
```java
// Crear botón primario (ancho fijo 250, alto 40)
JButton btn = UIFactory.createRoundedButton("Ingresar Productos", UIConstants.PRIMARY, UIConstants.TEXT_ON_PRIMARY, 250, 40);

// Crear botón secundario pequeño
JButton small = UIFactory.createRoundedButton("Guardar", UIConstants.PRIMARY, UIConstants.TEXT_ON_PRIMARY, 140, 34);

// Usar constantes de fuente
label.setFont(UIConstants.TITLE_FONT);
table.getTableHeader().setFont(UIConstants.TABLE_HEADER_FONT);
```

## Notas
- Esta guía es un punto de partida; ajustar valores para accesibilidad.
- Si se adoptan iconos gráficos (PNG/SVG) ubicarlos en `recursos/ui/icons/` y referenciarlos desde `UIFactory`.

---
Guía generada automáticamente por el equipo de desarrollo para mantener coherencia visual.
