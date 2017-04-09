package com.clayoverwind.javaplayer.presenter;

import com.clayoverwind.javaplayer.iview.IMainWindow;
import com.clayoverwind.javaplayer.service.TickService;
import com.clayoverwind.javaplayer.util.NativeDiscovery;
import com.clayoverwind.javaplayer.util.WinVlcDiscoveryStrategy;
import com.clayoverwind.javaplayer.view.MainWindow;
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
        SwingUtilities.invokeLater(() -> {
            mainWindow = new MainWindow("Java Player");
//            mainWindow.playMedia("D:\\Movies\\The.Big.Bang.Theory.S10E16.1080p.HDTV.X264-DIMENSION.mkv");
            mainWindow.playMedia("D:\\a.mkv");
            TickService.INSTANCE.start();
        });
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
