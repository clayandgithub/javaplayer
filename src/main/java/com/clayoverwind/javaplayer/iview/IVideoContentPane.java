package com.clayoverwind.javaplayer.iview;

import com.clayoverwind.javaplayer.action.MediaPlayerActions;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.*;

/**
 * @author clayoverwind
 * @version 2017/4/8
 * @E-mail clayanddev@163.com
 */
public interface IVideoContentPane extends ITimeSliderSource, ITimeSliderListener{
    void stop();

    void toggleFullScreen();

    JComponent getComponent();

    MediaPlayerActions getMediaPlayerActions();

    EmbeddedMediaPlayer getMediaPlayer();

    void playMedia(String path);

    void setTimeSlider(ITimeSlider timeSlider);

    void setDanMuWindow(IDanMuWindow danMuWindow);

    void setSubTitleFile(String subTitleFile);
}
