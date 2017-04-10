package com.clayoverwind.javaplayer.iview;

import com.clayoverwind.javaplayer.event.PausedEvent;
import com.clayoverwind.javaplayer.event.PlayingEvent;
import com.clayoverwind.javaplayer.event.StoppedEvent;
import com.clayoverwind.javaplayer.presenter.DanMuPlayer;

import javax.swing.*;
import java.awt.*;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */
public interface IDanMuPanel extends ITimeSliderListener {
    JComponent getComponent();

    DanMuPlayer getDanMuPlayer();

    void paintCallback(Graphics g);

    void loadDanMuResourceFromFile(String filePath, boolean startDirectly);

    void onPlaying(PlayingEvent event);

    void onPaused(PausedEvent event);

    void onStopped(StoppedEvent event);
}
