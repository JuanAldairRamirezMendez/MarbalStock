“AÑO DE LA RECUPERACIÓN Y CONSOLIDACIÓN DE LA ECONOMÍA PERUANA”
 
FACULTAD DE INGENIERÍA


TEMA:
Sistema de Inventario para la empresa de Marbal


TAREA ACADÉMICO DE LA ASIGNATURA
Análisis y Diseño de Sistemas de Información- Sección 40833

DOCENTE
Bances Saavedra, David Enrique

INTEGRANTES

Mateo Luis, Keila Roshaura				U23262823
Ramirez Mendez, Juan Aldair 			U20201597
Estrada Cárdenas Erick Jesús             		U22302925
Ferrel Julca, Rufo Piero	           			U23231492
García Navarro, Diego Fabricio           		U23247615






14 DE OCTUBRE DEL 2025
LIMA – PERÚ


Contenido
INTRODUCCIÓN	3
JUSTIFICACIÓN	3
OBJETIVOS GENERALES Y ESPECÍFICOS	4
Objetivo general	4
Objetivos específicos	4
ASPECTOS DE LA ORGANIZACIÓN	4
Visión	4
Misión	5
Descripción de la empresa	5
1.	ÁMBITO DEL PROYECTO	5
1.1.	Área	5
1.2.	Recursos Humanos para la elaboración del Proyecto	5
1.3.	Software	6
1.4.	Hardware	6
2.	FASE DE INICIO	9
2.1.	Modelado de negocio	9
2.1.1.	Modelado del Proceso de Negocio	9
2.1.2.	Modelo de Análisis del negocio:	9
Nota. Elaboración propia(2025)	14
2.1.3.	Recopilación de los requerimientos	14
2.2.	Matriz de requerimientos	17
3.	FASE DE ELABORACIÓN	19
3.1.	Casos de Usos	19
Conclusiones:	20
Referencias bibliográficas:	21







INTRODUCCIÓN
El presente trabajo tiene como propósito recopilar información para el proyecto Sistema de Inventario de Inversiones Comerciales Marbal E.I.R.L.. Hoy en día, la empresa maneja sus productos con hojas de Excel, lo que ocasiona errores y demoras al momento de llevar el control. Para entender mejor esta situación se usaron entrevistas, cuestionarios y observación, lo que permitió conocer las necesidades de la empresa. Con ello, se plantea una solución digital que facilite el control de inventarios, reduzca errores y ofrezca información en tiempo real para mejorar la gestión de la empresa
JUSTIFICACIÓN
La empresa Inversiones Comerciales Marbal E.I.R.L. actualmente administra su inventario con hojas de cálculo en Excel y registros físicos en papel, lo que genera diversas dificultades en la operación diaria. Entre los principales problemas se encuentran:
Falta de precisión en el control de stock, que provoca discrepancias entre lo registrado y el inventario real.
Pérdida de oportunidades de venta por no contar con información en tiempo real sobre la disponibilidad de productos.
Procesos de reposición poco eficientes, que derivan en el desabastecimiento de productos críticos.
Limitaciones para generar reportes confiables que respalden la toma de decisiones estratégicas.
Alto riesgo de errores humanos debido al registro manual de entradas y salidas.
Ante esta situación, la implementación de un Sistema de Inventario digital se presenta como una solución necesaria. Este permitirá automatizar procesos, minimizar errores, optimizar el control de stock y mejorar la eficiencia operativa. Con ello, Marbal podrá contar con información actualizada y confiable en tiempo real, lo que facilitará la toma de decisiones y reforzará su competitividad en el mercado de distribución mayorista.
OBJETIVOS GENERALES Y ESPECÍFICOS
Objetivo general
Desarrollar un sistema de inventario en Java que permita a la empresa controlar el consumo diario de insumos, reemplazando el uso de Excel para poder garantizar mayor precisión, eficiencia y trazabilidad con los productos contratados.
Objetivos específicos
•	Módulo de registro de productos diferenciando entre contratados y adicionales
•	Modulo Calcular la cantidad máxima permitida y generar alertas cuando el stock llegue a niveles críticos.
•	Módulo de gestión para editar o eliminar registros
•	Módulo de reportes y resúmenes, que muestre consumos de producto y stock restante
•	Centralizar toda la información en una base de datos relacional
ASPECTOS DE LA ORGANIZACIÓN
Visión 
Ser la empresa líder en la distribución de materiales de construcción, alimentos y bebidas al por mayor en la región reconocida por la eficiencia, precisión y confiabilidad en la gestión de su cadena de suministro y la satisfacción de sus clientes.
Misión
Brindar soluciones confiables en la comercialización de productos y servicios para diversos sectores, asegurando calidad, eficiencia y compromiso con nuestros clientes. Nuestro propósito es contribuir al desarrollo y sostenibilidad de las empresas a través de una atención personalizada y productos que se adapten a sus necesidades.

Descripción de la empresa
La empresa Inversiones Comerciales Marbal E.I.R.L., identificada con RUC 20552534124, se dedica a la venta y distribución de productos y servicios para el sector industrial y comercial. Su enfoque principal está en proveer artículos de calidad, garantizando la satisfacción de sus clientes con soluciones integrales y confiables. Gracias a su experiencia en el mercado, la empresa se posiciona como un aliado estratégico para pequeñas, medianas y grandes compañías que buscan optimizar sus procesos de abastecimiento.
1.	ÁMBITO DEL PROYECTO
1.1.	Área
El sistema se implementará en el área logística y control de inventarios de la empresa, la cual actualmente gestiona el consumo de insumos mediante una hoja de cálculo de Excel.  
1.2.	Recursos Humanos para la elaboración del Proyecto
Para llevar a cabo el desarrollo del Sistema de Inventario, es necesaria la participación de los siguientes perfiles: 
•	Administrador General de la Empresa: aporta su conocimiento sobre los procesos actuales, las necesidades del negocio y participa en la validación de los requerimientos del sistema. 
•	Equipo de Proyecto: encargado del análisis, diseño y propuesta de la solución tecnológica. Sus funciones incluyen el levantamiento de información, modelado de procesos, diseño del sistema y elaboración de la documentación correspondiente. 
•	Encargado de Almacén: proporciona información clave sobre el manejo físico del inventario, los flujos de entrada y salida de productos, y colabora en la validación de los módulos relacionados con el control de inventario. 
•	Personal de Ventas y Compras: contribuye con su experiencia en los procesos de comercialización y la relación con proveedores y clientes. Además, participa en la validación de los módulos de ventas y compras del sistema. 
•	Docente Asesor: supervisa el proyecto desde una perspectiva académica, brindando orientación metodológica y asegurando la correcta aplicación de buenas prácticas en el análisis y diseño del sistema.
1.3.	Software 
Tipo de Software: Aplicación web o de escritorio (se definirá en fases posteriores, pero se prioriza la accesibilidad). 
Arquitectura: Cliente-servidor (para permitir acceso multiusuario y centralización de datos). 
Base de Datos: Relacional (ej. PostgreSQL, MySQL) para asegurar la integridad y consistencia de los datos de inventario, ventas y compras. 
1.4.	Hardware
A lo largo del proyecto se empleará el siguiente componente, como un ordenador de escritorio o portátil, el cual permitirá desarrollar las actividades relacionadas con la implementación, seguimiento y documentación del sistema. 
Componente: Computadora de escritorio 
Características:
-	Procesador Intel Core i5 o superior 
-	Memoria RAM 4b 
-	Sistema Operativo Windows 10 Pro 
Tareas :
-	Desarrollo y pruebas del sistema 
-	Redacción de la documentación
1.5.	Cronograma de actividades
2.	FASE DE INICIO
2.1.	Modelado de negocio
2.1.1.	Modelado del Proceso de Negocio
Actores del negocio: 
-	Usuario/Operario (quien registra consumos). 
-	Administrador (quien gestiona y valida). 
-	Cliente/Entidad (quien recibe reportes).
Figura 01:
Diagrama del modelado de procesos del negocio

Nota. Elaboración propia(2025)

2.1.2.	Modelo de Análisis del negocio:
Trabajadores del Negocio:
EcuRed (2018) manifiesta “un trabajador del negocio representa un rol que juega una persona (o grupo de personas), una máquina o un sistema automatizado; actuando en el negocio. Son los que realizan las actividades, interactuando con otros trabajadores del negocio y manipulando entidades”.

•	Operario / Usuario: Registra consumos diarios de productos.
•	Administrador: Gestiona productos, usuarios, y valida operaciones.
•	Almacenero: Verifica físicamente el stock y recibe productos.
•	Encargado de Compras: Genera órdenes de compra y evalúa proveedores.
•	Vendedor: Registra ventas y atiende pedidos de clientes.
•	Cliente / Entidad: Recibe reportes y comprobantes.
•	Sistema: Automatiza alertas, cálculos y generación de reportes.

Entidades: 
•	Producto
•	Proveedor
•	Cliente
•	Usuario
•	Consumo
•	Venta
•	OrdenCompra
•	Reporte

Figura 02
Modelo de Entidades y sus Atributos Propuestos para el Sistema de Inventario
 
Nota. Elaboración propia(2025)

Realizaciones del negocio:
 EcuRed (2018) manifiesta “la realización de un caso de uso del negocio consta de una descripción textual por cada caso de uso, un diagrama de actividades y un diagrama de clases del negocio”.
RN1: Registrar consumo diario de productos
RN2: Generar alerta de stock bajo
RN3: Clasificar producto como contratado o adicional
RN4: Generar orden de compra automática
RN5: Generar reporte de ventas o inventario
RN6: Validar límite diario de consumo
RN7: Gestionar usuarios y permisos


Figura 03
Diagrama de Realizaciones de Negocio (Casos de Uso) para el Sistema de Inventario
Nota. Elaboración propia basada en el análisis de requerimientos de Inversiones Comerciales Marbal E.I.R.L. (2025).

Diagrama de actividades:
o	Registrar Consumo Diario
Figura 04
Diagrama de Flujo para el Proceso de Registrar Consumo Diario
Nota. Elaboración propia(2025)

o	Generar Orden de Compra
Figura 05 Diagrama de Secuencia para el Proceso de Generar una Orden de Compra
Nota. Elaboración propia(2025)

o	Generar Reporte Mensual
Figura 06
Diagrama de Secuencia para el Proceso de Generar un Reporte Mensual
Nota. Elaboración propia(2025)

2.1.3.	Recopilación de los requerimientos 
Marbal es una empresa dedicada a la distribución de productos como los materiales de construcción, alimentos, etc. El problema de inventario de la empresa Marbal es muy ineficiente para los estándares de hoy en día lo que genera muchos problemas operativos.
Entrevista: 
Entrevistador: Equipo de proyecto
Entrevistado: Administrador general de INVERSIONES COMERCIALES MARBAL E.I.R.L.

Pregunta 1: ¿Cuál es la actividad principal de la empresa?
Respuesta:
Nosotros nos dedicamos a la venta de materiales de construcción, alimentos y bebidas al por mayor. También tenemos un área que se encarga de trabajos de acabados de construcción, sobre todo en proyectos pequeños. Atendemos a ferreterías de barrio, comerciantes minoristas y contratistas. Es decir, tenemos clientes de distintos rubros, lo que nos obliga a manejar bien los productos porque son variados y con rotación distinta.

Pregunta 2: ¿Qué problemas enfrentan en la gestión actual del inventario?
Respuesta:
 Actualmente nuestro mayor problema es que el inventario se gestiona en Excel y de forma manual. Esto hace que muchas veces no refleje lo que realmente hay en el almacén. Por ejemplo, puede aparecer que hay stock disponible, pero al momento de atender un pedido no está. También pasa que se olvidan de registrar una salida o hay errores al digitar cantidades. Eso nos genera retrasos, reclamos de clientes y, en algunos casos, pérdidas económicas porque no tenemos un control real.

Pregunta 3: ¿Qué procesos considera más críticos dentro de la empresa?
Respuesta:
Diría que son tres: el control de inventarios, porque si no está bien todo lo demás falla; las ventas mayoristas, que son nuestro ingreso principal; y la logística de entrega, porque trabajamos con productos muy distintos: desde sacos de cemento hasta cajas de gaseosa. Si no hay una buena coordinación, el cliente se molesta y puede dejar de comprarnos.

Pregunta 4: ¿Qué herramientas utilizan actualmente para llevar sus procesos?
Respuesta:
 Usamos Excel y algunos registros en papel. El problema es que no todos los trabajadores manejan la misma versión del archivo, entonces la información se dispersa. También dependemos mucho de WhatsApp y llamadas para coordinar con clientes y proveedores, pero no hay un registro formal de esas comunicaciones, así que a veces se pierde información o se confunde entre varias personas.

Pregunta 5: ¿Cómo se comunican con sus clientes y proveedores?
Respuesta:
Con los clientes usamos sobre todo WhatsApp y llamadas, porque es lo más rápido. Nos dicen qué necesitan, vemos si hay stock y coordinamos la entrega. Con los proveedores es parecido: hacemos los pedidos por teléfono o correo, pero no tenemos un historial centralizado. Si queremos revisar qué compramos el mes pasado, tenemos que buscar facturas físicas o correos antiguos, y eso quita tiempo.

Pregunta 6: ¿Qué expectativas tienen frente a la implementación de una solución tecnológica?
Respuesta:
Queremos algo que nos dé más orden y que sea fácil de usar. Lo ideal sería poder ver el inventario en tiempo real, saber cuántos productos entran y salen, y que el sistema avise cuando un producto está por acabarse. También sería útil que genere reportes de ventas y compras sin tener que hacer cálculos manuales. En resumen, buscamos ahorrar tiempo y evitar errores.

Pregunta 7: ¿Qué consideran importante en el diseño de un nuevo sistema?
Respuesta:
Lo más importante es que sea sencillo. No queremos un sistema complicado que requiera mucha capacitación. También que pueda crecer con la empresa, porque poco a poco estamos atendiendo a más clientes. Y algo clave: que proteja bien la información, porque perder datos de inventario o ventas sería un gran problema.
Observación:
La entrevista muestra que la dependencia de herramientas manuales como Excel y WhatsApp provoca errores en el control de inventario, retrasos y ausencia de información confiable. Por ello, se evidencia la necesidad de implementar un sistema integrado que automatice los procesos, ofrezca datos en tiempo real y sea sencillo de utilizar, con el fin de respaldar el crecimiento de la empresa.
2.2.	Matriz de requerimientos
RF	Nombre del Requerimiento Funcional	Caso de Uso	Actores
RF01	Registrar consumo diario de productos	Registrar Consumo Diario	Usuario / Operario
RF02	Generar alerta de stock bajo	Generar Alerta de Stock Bajo	Sistema
RF03	Clasificar producto como contratado o adicional	Clasificar Producto	Administrador
RF04	Generar orden de compra automática	Generar Orden de Compra Automática	Sistema / Encargado de Compras
RF05	Generar reportes de ventas o inventario	Generar Reporte de Ventas o Inventario	Administrador / Cliente
RF06	Validar límite diario de consumo	Validar Límite de Consumo	Sistema
RF07	Gestionar usuarios y permisos	Gestionar Usuarios y Permisos	Administrador
RF08	Editar o eliminar registros de productos	Gestionar Registros de Productos	Administrador
RF09	Centralizar información en base de datos relacional	Centralizar Información	Sistema
RF10	Permitir impresión de reportes con formato para firma	Imprimir Reportes	Administrador / Cliente

3.	FASE DE ELABORACIÓN
3.1.	Casos de Usos
 
3.2.	Especificaciones del caso de Uso

3.3.	Análisis del Sistema
3.3.1.	Paquete de Análisis

3.3.2.	Clases de Entidad
3.3.3.	Realizaciones de Caso de Uso
3.3.4.	Diagrama de comunicación
3.3.5.	Diagrama de secuencia
3.4.	Modelo conceptual o Diagrama de clase
3.5.	Modelo Lógico
3.6.	Modelo Físico
3.7.	Tarjeta CRC de cada clase
FIN DEL AVANCE 02
3.8.	Diseño del Sistema
3.8.1.	Patrón de diseño
3.8.2.	Diagrama de estructura compuesta
3.8.3.	Diagrama de tiempo
3.8.4.	Diagrama de despliegue
3.8.5.	Diagrama de componentes







Conclusiones:
o	Se determinó que la principal dificultad de la empresa Marbal E.I.R.L. está en la gestión manual del inventario mediante hojas de Excel y registros físicos, lo que provoca inconsistencias en el stock, retrasos en las operaciones, pérdida de información y reportes poco confiables.
o	La implementación de un sistema de inventario digital resulta importante para optimizar procesos clave como el registro de consumos, la generación de alertas de stock, la emisión de órdenes de compra y la elaboración de reportes. Con ello se reducirán de manera significativa los errores humanos y se incrementará la eficiencia operativa.
o	El sistema propuesto responde directamente a la visión y misión de la empresa, ya que permitirá una administración más precisa y confiable del inventario, mejorando la satisfacción del cliente y fortaleciendo la competitividad en el mercado mayorista.
Referencias bibliográficas:
EcuRed. (20 de Junio de 2018). Flujo de trabajo en el modelado de negocios. Obtenido de EcuRed: https://www.ecured.cu/Flujo_de_trabajo_en_el_modelado_de_negocios 
