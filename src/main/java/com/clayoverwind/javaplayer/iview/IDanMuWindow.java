package com.clayoverwind.javaplayer.iview;

import javax.swing.*;
import java.awt.*;

/**
 * @author clayoverwind
 * @version 2017/4/8
 * @E-mail clayanddev@163.com
 */
public interface IDanMuWindow extends ITimeSliderListener {
    void playDanMu(String path);

    Component getComponent();

    JFrame getJFrame();

    void setDanMuFile(String filePath);

    void setPosition (float position);

    float getPosition();

    void setOverlayComponent(Component overlayComponent);

    void setFocusWindow(JFrame focusWindow);

    void showWindow();

    void hideWindow();

    void loadDanMuResourceFromFile(String filePath, boolean startDirectly);
}
