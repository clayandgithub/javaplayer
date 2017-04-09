package com.clayoverwind.javaplayer.iview;

import com.clayoverwind.javaplayer.util.TimeUtil;

import java.awt.*;

/**
 * @author clayoverwind
 * @version 2017/4/8
 * @E-mail clayanddev@163.com
 */
public interface ITimeSlider {
    void refresh();

    void setTime(long time);

    void setDuration(long duration);

    Component getComponent();
}
