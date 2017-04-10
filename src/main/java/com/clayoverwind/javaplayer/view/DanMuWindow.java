package com.clayoverwind.javaplayer.view;

import com.clayoverwind.javaplayer.Application;
import com.clayoverwind.javaplayer.event.TickEvent;
import com.clayoverwind.javaplayer.iview.IDanMuPanel;
import com.clayoverwind.javaplayer.iview.IDanMuWindow;
import com.clayoverwind.javaplayer.util.SwingUtil;
import com.google.common.eventbus.Subscribe;
import com.sun.awt.AWTUtilities;
import com.sun.jna.platform.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author clayoverwind
 * @version 2017/4/10
 * @E-mail clayanddev@163.com
 */
public class DanMuWindow extends TransparentWindow implements IDanMuWindow {
    private IDanMuPanel danMuPanel;

    private Component overlayComponent;

    private JFrame focusWindow;

    public DanMuWindow(String title, int width, int height) {
        super(title, width, height);
        SwingUtil.layoutAtScreenCenter(this);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setupContentPanel();
        setAlwaysOnTop(true);
        setVisible(true);

        addListeners();

        Application.INSTANCE.subscribe(this);
    }

    private void addListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                hideWindow();
            }
        });
    }

    private void setupContentPanel() {
        danMuPanel = new DanMuPanel(this.getWidth(), this.getHeight());
        setLayout(new BorderLayout());
        this.add(danMuPanel.getComponent(), BorderLayout.CENTER);
    }

    public void playDanMu(String path) {
        danMuPanel.getDanMuPlayer().playDanMu(path);
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public JFrame getJFrame() {
        return this;
    }

    @Override
    public void setDanMuFile(String filePath) {

    }

    @Override
    public void setPosition(float position) {

    }

    @Override
    public float getPosition() {
        return 0;
    }

    @Override
    public void setOverlayComponent(final Component overlayComponent) {
        this.overlayComponent = overlayComponent;
        if (overlayComponent != null) {
            overlayComponent.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    refreshPosition();
                }

                @Override
                public void componentMoved(ComponentEvent e) {
                    refreshPosition();
                }
            });
        }
    }

    public void refreshPosition() {
        if (overlayComponent != null) {
            Point srcPosition = overlayComponent.getLocationOnScreen();
            Dimension srcSize = overlayComponent.getSize();
            setLocation(srcPosition.x, srcPosition.y);
            setSize(srcSize);
        }
    }

    @Override
    public void setFocusWindow(JFrame focusWindow) {
        this.focusWindow = focusWindow;
        focusWindow.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                refreshPosition();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                refreshPosition();
            }
        });
//        if (focusWindow != null) {
//            focusWindow.addWindowFocusListener(new WindowFocusListener() {
//                @Override
//                public void windowGainedFocus(WindowEvent e) {
//                    System.out.println("-------setFocusWindow--------");
//                    requestFocus();
//                }
//
//                @Override
//                public void windowLostFocus(WindowEvent e) {
//                    System.out.println("-------lost focus--------");
//                }
//            });
//        }
    }

    @Override
    public void showWindow() {
        setVisible(true);
        danMuPanel.onPlaying(null);
    }

    @Override
    public void hideWindow() {
        setVisible(false);
        danMuPanel.onPaused(null);
    }

    @Override
    public void loadDanMuResourceFromFile(String filePath, boolean startDirectly) {
        danMuPanel.loadDanMuResourceFromFile(filePath, startDirectly);
    }

    @Override
    public void setTime(long time) {
        danMuPanel.setTime(time);
    }

    @Override
    public void setDuration(long duration) {
        danMuPanel.setDuration(duration);
    }
}