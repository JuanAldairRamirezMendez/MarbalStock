package vista;

import javax.swing.*;
import java.awt.*;

public class UIFactory {
    public static JButton createRoundedButton(String text, Color bg, Color fg, int width, int height) {
        JButton boton = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        // Pintamos el fondo redondeado en paintComponent pero mantenemos el botón opaco
        // para que no se muestren componentes subyacentes al hacer hover.
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        if (width > 0 && height > 0) boton.setPreferredSize(new Dimension(width, height));
        boton.setFont(UIConstants.BUTTON_FONT);
        boton.setBackground(bg == null ? UIConstants.PRIMARY : bg);
        boton.setForeground(fg == null ? Color.WHITE : fg);
        boton.setFocusPainted(false);
        boton.setBorder(new RoundedBorder(UIConstants.BUTTON_RADIUS, UIConstants.PRIMARY_DARK));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMargin(new Insets(6, 12, 6, 12));
        // No hover/pressed effects: dejar botón estable para evitar mostrar elementos subyacentes.
        // Se mantiene el pintado personalizado en paintComponent, pero no cambiaremos el fondo
        // dinámicamente al pasar el cursor.

        return boton;
    }
}
