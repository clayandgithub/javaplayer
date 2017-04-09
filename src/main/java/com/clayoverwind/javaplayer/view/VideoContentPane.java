package com.clayoverwind.javaplayer.view;

import com.clayoverwind.javaplayer.Application;
import com.clayoverwind.javaplayer.action.MediaPlayerActions;
import com.clayoverwind.javaplayer.event.*;
import com.clayoverwind.javaplayer.iview.IDanMuPanel;
import com.clayoverwind.javaplayer.iview.ITimeSlider;
import com.clayoverwind.javaplayer.iview.IVideoContentPane;
import com.clayoverwind.javaplayer.presenter.DanMuPlayer;
import com.clayoverwind.javaplayer.strategy.JavaPlayerFullScreenStrategy;
import com.google.common.eventbus.Subscribe;
import com.sun.jna.platform.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

import javax.swing.*;
import java.awt.*;

public final class VideoContentPane extends BasePanel implements IVideoContentPane {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String NAME_DEFAULT = "default";

    private static final String NAME_VIDEO = "video";

    private final CardLayout cardLayout;

    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private MediaPlayerActions mediaPlayerActions;

    private ITimeSlider timeSlider;

    public VideoContentPane(Window parentWindow) {

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent(){
            @Override
            protected String[] onGetMediaPlayerFactoryExtraArgs() {
                return new String[] {"--no-osd"}; // Disables the display of the snapshot filename (amongst other things)
            }
        };
        mediaPlayerActions = new MediaPlayerActions(mediaPlayerComponent.getMediaPlayer());
        mediaPlayerComponent.getMediaPlayer().setFullScreenStrategy(new JavaPlayerFullScreenStrategy(parentWindow));
//        nativeLog = mediaPlayerComponent.getMediaPlayerFactory().newLog();
//        messagesFrame = new NativeLogFrame(nativeLog);

        // layout
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        add(new ImagePane(ImagePane.Mode.CENTER, getClass().getResource("/icons/vlcj-logo.png"), 0.3f), NAME_DEFAULT);

        addVideoPanel(NAME_VIDEO);

        // event listeners
        addEventListenerForMediaPlayer();
    }

    private void addVideoPanel(String cardName) {
        add(mediaPlayerComponent, cardName);
    }

    private void addEventListenerForMediaPlayer() {
        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                showVideo();
//                mouseMovementDetector.start();
                Application.INSTANCE.post(PlayingEvent.INSTANCE);
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
//                mouseMovementDetector.stop();
                Application.INSTANCE.post(PausedEvent.INSTANCE);
            }

            @Override
            public void muted(MediaPlayer mediaPlayer, boolean muted) {
                System.out.println("--------TRUE-------" + muted);//TODO doesn't work
                Application.INSTANCE.post(muted ? MuteEvent.TRUE : MuteEvent.FALSE);
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
//                mouseMovementDetector.stop();
                showDefault();
                Application.INSTANCE.post(StoppedEvent.INSTANCE);
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                showDefault();
//                mouseMovementDetector.stop();
                Application.INSTANCE.post(StoppedEvent.INSTANCE);
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                showDefault();
//                mouseMovementDetector.stop();
                Application.INSTANCE.post(StoppedEvent.INSTANCE);
//                JOptionPane.showMessageDialog(mainWindow, MessageFormat.format(resources().getString("error.errorEncountered"), fileChooser.getSelectedFile().toString()), resources().getString("dialog.errorEncountered"), JOptionPane.ERROR_MESSAGE);
            }

            @Override
            public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
//                        statusBar.setTitle(mediaPlayer.getMediaMeta().getTitle());
                    }
                });
            }

            @Override
            public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (timeSlider != null) {
                            timeSlider.setDuration(newDuration);
                        }

//                        statusBar.setDuration(newDuration);
                    }
                });
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (timeSlider != null) {
                            timeSlider.setTime(newTime);
                        }
//                        statusBar.setTime(newTime);
                    }
                });
            }
        });
    }

    private void showDefault() {
        cardLayout.show(this, NAME_DEFAULT);
    }

    private void showVideo() {
        cardLayout.show(this, NAME_VIDEO);
    }

    @Override
    public void playMedia(String path) {
        showVideo();
        mediaPlayerComponent.getMediaPlayer().playMedia(path);
    }

    @Override
    public void setTimeSlider(ITimeSlider timeSlider) {
        this.timeSlider = timeSlider;
    }

    @Override
    public void stop() {
        mediaPlayerComponent.getMediaPlayer().stop();
        mediaPlayerComponent.release();
    }

    @Override
    public void toggleFullScreen() {
        mediaPlayerComponent.getMediaPlayer().toggleFullScreen();
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    private void pause() {
        if (mediaPlayerComponent.getMediaPlayer().canPause()) {
            mediaPlayerComponent.getMediaPlayer().pause();
        } else {
            LOGGER.warn("Cannot pause current media!");
        }
    }

    private void play() {
        if (mediaPlayerComponent.getMediaPlayer().isPlayable()) {
            mediaPlayerComponent.getMediaPlayer().play();
        } else {
            LOGGER.warn("Cannot play current media!");
        }
    }

    private boolean isPlaying() {
        return mediaPlayerComponent.getMediaPlayer().isPlaying();
    }

    @Override
    public void setSubTitleFile(String path) {
        mediaPlayerComponent.getMediaPlayer().setSubTitleFile(path);
    }

    @Override
    public MediaPlayerActions getMediaPlayerActions() {
        return mediaPlayerActions;
    }

    @Override
    public EmbeddedMediaPlayerComponent getMediaPlayerComponent() {
        return mediaPlayerComponent;
    }

    @Override
    public float getPosition() {
        return mediaPlayerComponent.getMediaPlayer().getPosition();
    }

    @Override
    public void setPosition(float position) {
        mediaPlayerComponent.getMediaPlayer().setPosition(position);
    }
}
