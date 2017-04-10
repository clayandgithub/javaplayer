package com.clayoverwind.javaplayer.presenter;

import com.clayoverwind.javaplayer.iview.IDanMuWindow;
import com.clayoverwind.javaplayer.iview.IMainWindow;
import com.clayoverwind.javaplayer.iview.ITimeSlider;
import com.clayoverwind.javaplayer.service.TickService;
import com.clayoverwind.javaplayer.util.NativeDiscovery;
import com.clayoverwind.javaplayer.util.WinVlcDiscoveryStrategy;
import com.clayoverwind.javaplayer.view.DanMuWindow;
import com.clayoverwind.javaplayer.view.MainWindow;
import com.clayoverwind.javaplayer.view.NewMainWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;

/**
 * @author clayoverwind
 * @version 2017/4/8
 * @E-mail clayanddev@163.com
 */
public enum  MainWindowPresenter {
    INSTANCE;

    private IMainWindow mainWindow;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void start() {
        new NativeDiscovery(new WinVlcDiscoveryStrategy()).discover();
        setLookAndFeel();
        if (SwingUtilities.isEventDispatchThread()) {
            initAndStart();
        } else {
            SwingUtilities.invokeLater(() -> initAndStart());
        }
    }

    private void initAndStart() {
        mainWindow = new MainWindow("Java Player");
        TickService.INSTANCE.start();
        mainWindow.playMedia("D:\\Movies\\The.Big.Bang.Theory.S10E13.1080p.HDTV.X264-DIMENSION.mkv");
    }

    private void setLookAndFeel() {
        String lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
        if (RuntimeUtil.isNix()) {
            lookAndFeelClassName = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        }
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        } catch (Exception e) {
            LOGGER.error("This error doesn't matter", e);
        }
    }
}
