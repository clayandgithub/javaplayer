package com.clayoverwind.javaplayer.presenter;

import com.clayoverwind.javaplayer.iview.IDanMuWindow;
import com.clayoverwind.javaplayer.model.DanMuResource;
import com.clayoverwind.javaplayer.util.FileUtil;
import com.clayoverwind.javaplayer.view.ChooseDanMuFileDialog;
import com.clayoverwind.javaplayer.view.DanMuWindow;
import com.clayoverwind.javaplayer.view.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */
public enum DanMuWindowPresenter {
    INSTANCE;

    private IDanMuWindow danMuWindow;

    public static final int MSPF = 25;

    public static final int DANMU_BASE_SPEED = 3;

    public static final Font DEFAULT_FONT = new Font("微软雅黑", Font.PLAIN, 14);

    private DanMuResource danMuResource = new DanMuResource();

    public void attachOverlayComponentAndFocusWindow(JFrame focusWindow, Component overlayComponent) {
        danMuWindow.setOverlayComponent(overlayComponent);
        danMuWindow.setFocusWindow(focusWindow);
    }

    public void showDanMuWindow() {
        if (danMuWindow == null) {
            if (SwingUtilities.isEventDispatchThread()) {
                danMuWindow = new DanMuWindow("弹幕窗口", 600, 400);
            } else {
                SwingUtilities.invokeLater(()->danMuWindow = new DanMuWindow("弹幕窗口", 600, 400));
            }
            VideoContentPanePresenter.INSTANCE.setDanMuWindow(danMuWindow);
        } else {
            danMuWindow.showWindow();
        }
    }

    public void hideDanMuWindow() {
        if (danMuWindow != null) {
            danMuWindow.hideWindow();
        }
    }

    public void importDanMuFile() {
        ChooseDanMuFileDialog dialog = new ChooseDanMuFileDialog(danMuWindow.getJFrame(), true);
        dialog.setVisible(true);
    }

    public void loadDanMuResourceFromFile(String filePath, boolean startDirectly) {
        danMuWindow.loadDanMuResourceFromFile(filePath, startDirectly);
    }

    public void showOrHideDanMu() {
        if (danMuWindow != null) {
            if (danMuWindow.getComponent().isVisible()) {
                danMuWindow.hideWindow();
            } else {
                danMuWindow.showWindow();
            }
        }
    }
}
