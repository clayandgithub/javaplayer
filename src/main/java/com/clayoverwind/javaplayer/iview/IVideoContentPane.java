package com.clayoverwind.javaplayer.iview;

import com.clayoverwind.javaplayer.action.MediaPlayerActions;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.Component;

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

    EmbeddedMediaPlayerComponent getMediaPlayerComponent();

    void playMedia(String path);

    void setTimeSlider(ITimeSlider timeSlider);

    void setSubTitleFile(String subTitleFile);
}
