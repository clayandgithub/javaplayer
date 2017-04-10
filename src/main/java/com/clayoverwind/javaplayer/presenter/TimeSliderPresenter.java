package com.clayoverwind.javaplayer.presenter;

import com.clayoverwind.javaplayer.iview.IMainWindow;
import com.clayoverwind.javaplayer.iview.ITimeSlider;
import com.clayoverwind.javaplayer.iview.ITimeSliderListener;
import com.clayoverwind.javaplayer.iview.ITimeSliderSource;
import com.clayoverwind.javaplayer.view.MenuBar;
import com.clayoverwind.javaplayer.view.TimeSlider;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */
public enum TimeSliderPresenter {
    INSTANCE;
    private ITimeSlider timeSlider;

    public ITimeSlider createTimeSlider(ITimeSliderSource source) {
        if (timeSlider == null) {
            timeSlider = new TimeSlider(source);
        }
        return timeSlider;
    }

    public void addTimeSliderListener(ITimeSliderListener listener) {
        if (timeSlider != null) {
            timeSlider.addTimeSliderListener(listener);
        }
    }


}
