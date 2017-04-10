package com.clayoverwind.javaplayer.util;

import com.sun.glass.ui.Size;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Enumeration;

public class SwingUtil {
    private static AffineTransform sAffineTransform = new AffineTransform();

    private static FontRenderContext sFontRenderContext = new FontRenderContext(sAffineTransform, true,
            true);

    public static Size getStringSize(String str, Font font) {
        if (str == null || str.length() == 0 || font == null) {
            return new Size(0, 0);
        }
        return new Size((int) font.getStringBounds(str, sFontRenderContext).getWidth(), (int) font.getStringBounds(str, sFontRenderContext).getHeight());
    }

    public static void layoutAtScreenCenter(Component window) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }

    public static void addImageBackgroundPanel(final JFrame window, final String imageName) {
        if (imageName != null) {
            JPanel imgPanel = new JPanel() {
                private static final long serialVersionUID = 2580564128458591658L;

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon icon = new ImageIcon(this.getClass().getResource(imageName));
                    g.drawImage(icon.getImage(), 0, 0, window.getWidth(),
                            window.getHeight(), this);
                }
            };
            imgPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            imgPanel.setOpaque(false);
            window.setContentPane(imgPanel);
        }
    }

    public static void initGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys();
             keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
}