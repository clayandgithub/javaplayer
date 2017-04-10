package com.clayoverwind.javaplayer.presenter;

import com.clayoverwind.javaplayer.iview.IDanMuWindow;
import com.clayoverwind.javaplayer.iview.IVideoContentPane;
import com.clayoverwind.javaplayer.view.EmbeddedVideoContentPane;

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
            videoContentPane = new EmbeddedVideoContentPane(parentWindow);
//            videoContentPane = new DirectVideoContentPane(parentComponent);
        }
        return videoContentPane;
    }

    public IVideoContentPane getVideoContentPane() {
        return videoContentPane;
    }

    public void setDanMuWindow(IDanMuWindow danMuWindow) {
        this.videoContentPane.setDanMuWindow(danMuWindow);
    }

}
