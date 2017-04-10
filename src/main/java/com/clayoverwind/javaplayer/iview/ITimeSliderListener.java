package com.clayoverwind.javaplayer.iview;

/**
 * @author clayoverwind
 * @version 2017/4/8
 * @E-mail clayanddev@163.com
 */
public interface ITimeSliderListener {
    void setPosition(float v);

    void setTime(long time);

    void setDuration(long duration);
}
