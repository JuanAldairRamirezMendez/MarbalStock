import conexion.ConexionBD;
import java.sql.Connection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vista.LoginFrame;

/**
 * Main - Punto de entrada principal del Sistema de Inventario Marbal
 * 
 * SISTEMA DE INVENTARIO MARBAL - Inversiones Comerciales Marbal E.I.R.L.
 * Proyecto académico - Análisis y Diseño de Sistemas de Información
 * 
 * RESPONSABLE: Equipo completo (Coordinado por Diego García)
 * 
 * DESCRIPCIÓN:
 * Esta clase es el punto de entrada principal del sistema. Realiza las
 * siguientes tareas antes de lanzar la interfaz de usuario:
 * 
 * 1. VERIFICACIÓN DE CONEXIÓN A BD:
 *    - Prueba la conexión a MySQL/MariaDB usando ConexionBD (Juan Ramírez)
 *    - Valida que el esquema 'marbal_inventario' esté accesible
 *    - Muestra mensaje de error si la conexión falla
 * 
 * 2. CONFIGURACIÓN DE LOOK AND FEEL:
 *    - Aplica el tema nativo del sistema operativo
 *    - Mejora la apariencia visual de la interfaz Swing
 * 
 * 3. INICIALIZACIÓN DE LA APLICACIÓN:
 *    - Lanza LoginFrame (Rufo Ferrel) como primera ventana
 *    - Permite al usuario autenticarse antes de acceder al sistema
 * 
 * FLUJO DE EJECUCIÓN:
 * Main → Verificar BD → LoginFrame → MenuPrincipal → Módulos del sistema
 * 
 * ARQUITECTURA MVC:
 * - Main verifica la infraestructura (BD)
 * - LoginFrame (Vista) captura credenciales
 * - UsuarioController (Controlador) valida autenticación
 * - MenuPrincipal (Vista) muestra opciones según rol
 * 
 * INTEGRANTES DEL PROYECTO:
 * - Diego García (U23247615) - Integración MVC y pruebas
 * - Keila Mateo (U23262823) - Modelos/Entidades
 * - Juan Ramírez (U20201597) - Base de datos y conexión
 * - Erick Estrada (U22302925) - Controladores y seguridad
 * - Rufo Ferrel (U23231492) - Interfaces gráficas y reportes
 * 
 * REQUISITOS PREVIOS:
 * 1. MySQL/MariaDB en ejecución (XAMPP o MySQL Server)
 * 2. Base de datos 'marbal_inventario' creada con el script_bd.sql
 * 3. Driver JDBC de MySQL en el classpath
 * 4. Configuración correcta en ConexionBD.java (URL, usuario, contraseña)
 * 
 * CASOS DE USO:
 * - Punto de entrada único para toda la aplicación
 * - Validación de infraestructura antes de iniciar
 * - Experiencia de usuario consistente desde el inicio
 * 
 * FECHA: Octubre 2025
 * 
 * @author Equipo Marbal
 * @version 1.0
 */
public class Main {
    
    /**
     * Método principal que inicia el Sistema de Inventario Marbal.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("SISTEMA DE INVENTARIO MARBAL");
        System.out.println("Inversiones Comerciales Marbal E.I.R.L.");
        System.out.println("========================================\n");
        
        // 1. VERIFICAR CONEXIÓN A BASE DE DATOS
        System.out.println("1. Verificando conexión a base de datos...");
        if (!verificarConexionBD()) {
            System.err.println("\n❌ ERROR CRÍTICO: No se pudo conectar a la base de datos.");
            System.err.println("   Verifique que:");
            System.err.println("   - MySQL/MariaDB esté ejecutándose (XAMPP o MySQL Server)");
            System.err.println("   - El esquema 'marbal_inventario' esté creado");
            System.err.println("   - Las credenciales en ConexionBD.java sean correctas");
            System.err.println("   - El driver JDBC esté en el classpath\n");
            
            // Mostrar diálogo de error en modo gráfico
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "No se pudo conectar a la base de datos.\n\n" +
                "Verifique que MySQL/MariaDB esté en ejecución\n" +
                "y que el esquema 'marbal_inventario' esté creado.\n\n" +
                "Consulte la consola para más detalles.",
                "Error de Conexión - Sistema Marbal",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            
            System.exit(1);
        }
        
        System.out.println("   ✓ Conexión a base de datos exitosa\n");
        
        // 2. CONFIGURAR LOOK AND FEEL
        System.out.println("2. Configurando interfaz gráfica...");
        configurarLookAndFeel();
        System.out.println("   ✓ Look and Feel configurado\n");
        
        // 3. LANZAR APLICACIÓN
        System.out.println("3. Iniciando interfaz de usuario...");
        SwingUtilities.invokeLater(() -> {
            try {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                System.out.println("   ✓ Sistema iniciado correctamente\n");
                System.out.println("========================================");
                System.out.println("Sistema listo. Esperando autenticación...");
                System.out.println("========================================\n");
            } catch (Exception e) {
                System.err.println("❌ Error al iniciar la interfaz de usuario:");
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
    
    /**
     * Verifica la conexión a la base de datos antes de iniciar la aplicación.
     * 
     * @return true si la conexión es exitosa, false en caso contrario
     */
    private static boolean verificarConexionBD() {
        ConexionBD conexionBD = new ConexionBD();
        Connection conn = null;
        
        try {
            conn = conexionBD.abrirConexion();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("   → Conectado a: " + conn.getMetaData().getURL());
                System.out.println("   → Base de datos: " + conn.getCatalog());
                System.out.println("   → Usuario: " + conn.getMetaData().getUserName());
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("   ❌ Error al conectar: " + e.getMessage());
            e.printStackTrace();
            return false;
            
        } finally {
            if (conexionBD != null) {
                try {
                    conexionBD.cerrarConexion();
                } catch (Exception e) {
                    // Ignorar errores al cerrar
                }
            }
        }
    }
    
    /**
     * Configura el Look and Feel de la aplicación para usar el tema nativo
     * del sistema operativo, mejorando la apariencia visual.
     */
    private static void configurarLookAndFeel() {
        try {
            // Usar el Look and Feel nativo del sistema operativo
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (Exception e) {
            System.err.println("   ⚠ No se pudo aplicar el Look and Feel del sistema.");
            System.err.println("     Se usará el Look and Feel por defecto de Java.");
            // No es crítico, continuar con el Look and Feel por defecto
        }
    }
}
