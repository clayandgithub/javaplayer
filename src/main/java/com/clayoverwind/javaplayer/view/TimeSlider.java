/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2015 Caprica Software Limited.
 */

package com.clayoverwind.javaplayer.view;

import com.clayoverwind.javaplayer.Application;
import com.clayoverwind.javaplayer.event.TickEvent;
import com.clayoverwind.javaplayer.iview.ITimeSlider;
import com.clayoverwind.javaplayer.iview.ITimeSliderListener;
import com.clayoverwind.javaplayer.iview.ITimeSliderSource;
import com.clayoverwind.javaplayer.util.TimeUtil;
import com.google.common.eventbus.Subscribe;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.player.MediaPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

final class TimeSlider extends JPanel implements ITimeSlider {

    private final JSlider slider;
    private final JLabel timeLabel;
    private final JLabel durationLabel;

    private long time;

    private final ITimeSliderSource timeSliderSource;

    private final ITimeSliderListener timeSliderListener;

    private final AtomicBoolean sliderChanging = new AtomicBoolean();

    private final AtomicBoolean positionChanging = new AtomicBoolean();

    TimeSlider(ITimeSliderSource source, ITimeSliderListener listener) {
        this.timeSliderSource = source;
        this.timeSliderListener = listener;

        timeLabel = new StandardLabel("9:99:99");

        UIManager.put("Slider.paintValue", false); // FIXME how to do this for a single component?
        slider = new JSlider();
        slider.setMinimum(0);
        slider.setMaximum(1000);
        slider.setValue(0);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!positionChanging.get()) {
                    JSlider source = (JSlider) e.getSource();
                    if (source.getValueIsAdjusting()) {
                        sliderChanging.set(true);
                    } else {
                        sliderChanging.set(false);
                    }
                    timeSliderListener.setPosition(source.getValue() / 1000.0f);
                }
            }
        });

        durationLabel = new StandardLabel("9:99:99");

        setLayout(new MigLayout("fill, insets 0 0 0 0", "[grow][][][]", "[]"));

        add(slider, "grow");
        add(timeLabel, "shrink");
        add(new JLabel("/"), "shrink");
        add(durationLabel, "shrink");

        timeLabel.setText("-:--:--");

        durationLabel.setText("-:--:--");

        Application.INSTANCE.subscribe(this);
    }

    @Override
    public void refresh() {
        timeLabel.setText(TimeUtil.formatTime(time));

        if (!sliderChanging.get()) {
            int value = (int) (timeSliderSource.getPosition() * 1000.0f);
            positionChanging.set(true);
            slider.setValue(value);
            positionChanging.set(false);
        }
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public void setDuration(long duration) {
        durationLabel.setText(TimeUtil.formatTime(duration));
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Subscribe
    public void onTick(TickEvent tick) {
        refresh();
    }
}
