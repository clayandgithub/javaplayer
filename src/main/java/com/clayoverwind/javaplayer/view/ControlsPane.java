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
import com.clayoverwind.javaplayer.action.MediaPlayerActions;
import com.clayoverwind.javaplayer.event.*;
import com.clayoverwind.javaplayer.iview.ITimeSlider;
import com.clayoverwind.javaplayer.iview.IVideoContentPane;
import com.clayoverwind.javaplayer.presenter.TimeSliderPresenter;
import com.google.common.eventbus.Subscribe;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class ControlsPane extends BasePanel {

    private final Icon playIcon = newIcon("play");

    private final Icon pauseIcon = newIcon("pause");

    private final Icon previousIcon = newIcon("previous");

    private final Icon nextIcon = newIcon("next");

    private final Icon fullscreenIcon = newIcon("fullscreen");

    private final Icon extendedIcon = newIcon("extended");

    private final Icon snapshotIcon = newIcon("snapshot");

    private final Icon volumeHighIcon = newIcon("volume-high");

    private final Icon volumeMutedIcon = newIcon("volume-muted");

    private final JButton playPauseButton;

    private final JButton previousButton;

    private final JButton stopButton;

    private final JButton nextButton;

    private final JButton fullscreenButton;

    private final JButton extendedButton;

    private final JButton snapshotButton;

    private final JButton muteButton;

    private final JSlider volumeSlider;

    private ITimeSlider timeSlider;

    public ControlsPane(IVideoContentPane videoContentPane) {
        MediaPlayerActions mediaPlayerActions = videoContentPane.getMediaPlayerActions();
        EmbeddedMediaPlayer mediaPlayer = videoContentPane.getMediaPlayer();
        playPauseButton = new BigButton();
        playPauseButton.setAction(mediaPlayerActions.playbackPlayAction());
        previousButton = new StandardButton();
        previousButton.setIcon(previousIcon);
        stopButton = new StandardButton();
        stopButton.setAction(mediaPlayerActions.playbackStopAction());
        nextButton = new StandardButton();
        nextButton.setIcon(nextIcon);
        fullscreenButton = new StandardButton();
        fullscreenButton.setIcon(fullscreenIcon);
        extendedButton = new StandardButton();
        extendedButton.setIcon(extendedIcon);
        snapshotButton = new StandardButton();
        snapshotButton.setAction(mediaPlayerActions.videoSnapshotAction());
        muteButton = new StandardButton();
        muteButton.setIcon(volumeHighIcon);
        volumeSlider = new JSlider();
        volumeSlider.setMinimum(LibVlcConst.MIN_VOLUME);
        volumeSlider.setMaximum(LibVlcConst.MAX_VOLUME);

        setLayout(new MigLayout("fill, insets 0 0 0 0", "[]12[grow]8[]0[]0[]0[]0[]", "[center]"));

        add(playPauseButton);

        timeSlider = TimeSliderPresenter.INSTANCE.createTimeSlider(videoContentPane);
        timeSlider.addTimeSliderListener(videoContentPane);
        videoContentPane.setTimeSlider(timeSlider);
        add(timeSlider.getComponent(), "growx");

        add(stopButton, "sg 1");

        add(fullscreenButton, "sg 1");

        add(muteButton, "sg 1");

        add(volumeSlider, "wmax 100");

//        add(extendedButton, "sg 1");
//        add(snapshotButton, "sg 1");

        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                mediaPlayer.setVolume(volumeSlider.getValue());
            }
        });

        // FIXME really these should share common action

        muteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.mute();
            }
        });

        fullscreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.toggleFullScreen();
            }
        });

        extendedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Application.INSTANCE.post(ShowEffectsEvent.INSTANCE);
            }
        });
    }

    @Subscribe
    public void onPlaying(PlayingEvent event) {
        playPauseButton.setIcon(pauseIcon);
    }

    @Subscribe
    public void onPaused(PausedEvent event) {
        playPauseButton.setIcon(playIcon);
    }

    @Subscribe
    public void onStopped(StoppedEvent event) {
        playPauseButton.setIcon(playIcon);
    }

    @Subscribe
    public void onMuted(MuteEvent event) {
        switch (event) {
            case TRUE:
                muteButton.setIcon(volumeMutedIcon);
                break;
            default:
                muteButton.setIcon(volumeHighIcon);
        }
    }

    private class BigButton extends JButton {

        private BigButton() {
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setHideActionText(true);
        }
    }

    private class StandardButton extends JButton {

        private StandardButton() {
            setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            setHideActionText(true);
        }
    }

    private Icon newIcon(String name) {
        return new ImageIcon(getClass().getResource("/icons/buttons/" + name + ".png"));
    }
}
