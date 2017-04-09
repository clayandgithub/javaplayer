package com.clayoverwind.javaplayer.presenter;

import com.clayoverwind.javaplayer.iview.IMainWindow;
import com.clayoverwind.javaplayer.iview.IVideoContentPane;
import com.clayoverwind.javaplayer.view.MainWindow;
import com.clayoverwind.javaplayer.view.VideoContentPane;
import com.sun.jna.platform.unix.X11;

import java.awt.*;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */
public enum VideoContentPanePresenter {
    INSTANCE;

    private IVideoContentPane videoContentPane;

    public IVideoContentPane createVideoContentPane(Window parentWindow) {
        if (videoContentPane == null) {
            videoContentPane = new VideoContentPane(parentWindow);
        }
        return videoContentPane;
    }

    public IVideoContentPane getVideoContentPane() {
        return videoContentPane;
    }

}
