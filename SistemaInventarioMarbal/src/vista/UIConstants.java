package vista;

import java.awt.*;

public class UIConstants {
    // Colores
    // Color primario actualizado a un tono más llamativo (naranja vibrante)
    public static final Color PRIMARY = new Color(255,140,0); // naranja vibrante
    public static final Color PRIMARY_DARK = new Color(230,115,0);
    public static final Color HEADER = new Color(200,80,0);
    public static final Color BACKGROUND = new Color(245,247,250);
    public static final Color PANEL_BG = Color.WHITE;
    public static final Color TEXT_PRIMARY = new Color(34,34,34);
    public static final Color TEXT_SECONDARY = new Color(107,114,128);
    public static final Color SECONDARY_BUTTON = new Color(220,220,220);
    public static final Color TABLE_HEADER_BG = new Color(240,240,240);
    public static final Color HEADER_SUB = new Color(200,220,240);

    // Tipografías
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font SECTION_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TABLE_CELL_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    // Tamaños / medidas
    public static final int BUTTON_RADIUS = 14;
    public static final Dimension MENU_BUTTON_SIZE = new Dimension(250, 40);
    public static final Dimension ACTION_BUTTON_SIZE = new Dimension(140, 34);

    private UIConstants() { /* util */ }
}
