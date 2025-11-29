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
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        if (width > 0 && height > 0) boton.setPreferredSize(new Dimension(width, height));
        boton.setFont(UIConstants.BUTTON_FONT);
        boton.setBackground(bg == null ? UIConstants.PRIMARY : bg);
        boton.setForeground(fg == null ? Color.WHITE : fg);
        boton.setFocusPainted(false);
        boton.setBorder(new RoundedBorder(UIConstants.BUTTON_RADIUS, UIConstants.PRIMARY_DARK));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMargin(new Insets(6, 12, 6, 12));
        // efecto hover y pressed
        Color base = boton.getBackground();
        Color hover = base.brighter();
        Color pressed = base.darker();
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(hover);
                boton.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(base);
                boton.repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                boton.setBackground(pressed);
                boton.repaint();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                // restaurar hover o base según posición
                if (boton.contains(e.getPoint())) boton.setBackground(hover);
                else boton.setBackground(base);
                boton.repaint();
            }
        });

        return boton;
    }
}
