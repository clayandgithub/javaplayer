package com.clayoverwind.javaplayer.view;

import com.clayoverwind.javaplayer.Application;
import com.clayoverwind.javaplayer.action.MediaPlayerActions;
import com.clayoverwind.javaplayer.event.MuteEvent;
import com.clayoverwind.javaplayer.event.PausedEvent;
import com.clayoverwind.javaplayer.event.PlayingEvent;
import com.clayoverwind.javaplayer.event.StoppedEvent;
import com.clayoverwind.javaplayer.iview.IDanMuPanel;
import com.clayoverwind.javaplayer.iview.IDanMuWindow;
import com.clayoverwind.javaplayer.iview.ITimeSlider;
import com.clayoverwind.javaplayer.iview.IVideoContentPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.direct.*;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@Deprecated
public final class DirectVideoContentPane extends BasePanel implements IVideoContentPane {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final DirectMediaPlayerComponent mediaPlayerComponent;

    private MediaPlayerActions mediaPlayerActions;

    private static final String NAME_DEFAULT = "default";

    private static final String NAME_VIDEO = "video";

    private final CardLayout cardLayout;

    private final BufferedImage image;

    private final JPanel videoSurface;

    private final IDanMuPanel danMuPanel;

    private ITimeSlider timeSlider;

    private Component parentComponent;

    private Dimension renderSize = Toolkit.getDefaultToolkit().getScreenSize();

    public DirectVideoContentPane(Component parentComponent) {
        this.parentComponent = parentComponent;
        image = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .createCompatibleImage(renderSize.width, renderSize.height);
        videoSurface = new VideoSurfacePanel(renderSize.width, renderSize.height);
        danMuPanel = new DanMuPanel(renderSize.width, renderSize.height);
        BufferFormatCallback bufferFormatCallback = new BufferFormatCallback() {
            @Override
            public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
                return new RV32BufferFormat(renderSize.width, renderSize.height);
            }
        };
        mediaPlayerComponent = new DirectMediaPlayerComponent(bufferFormatCallback) {
            @Override
            protected RenderCallback onGetRenderCallback() {
                return new SimpleRenderCallbackAdapter(renderSize.width, renderSize.height);
            }
        };

        mediaPlayerActions = new MediaPlayerActions(mediaPlayerComponent.getMediaPlayer());
//        mediaPlayerComponent.getMediaPlayer().setFullScreenStrategy(new JavaPlayerFullScreenStrategy(parentWindow));
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
        JPanel upPane = new JPanel();
        upPane.setLayout(new OverlayLayout(upPane));

        danMuPanel.getComponent().setAlignmentX(0);
        danMuPanel.getComponent().setAlignmentY(0);
        upPane.add(danMuPanel.getComponent());

        videoSurface.setAlignmentX(0);
        videoSurface.setAlignmentY(0);
        upPane.add(videoSurface);

        add(upPane, cardName);
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
    public void setDanMuWindow(IDanMuWindow danMuWindow) {

    }

    @Override
    public void stop() {
        mediaPlayerComponent.getMediaPlayer().stop();
        mediaPlayerComponent.release();
    }

    @Override
    public void toggleFullScreen() {
//        mediaPlayerComponent.getMediaPlayer().toggleFullScreen();
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
    public EmbeddedMediaPlayer getMediaPlayer() {
//        return mediaPlayerComponent.getMediaPlayer();
        return null;
    }
//
//    @Override
//    public float getPosition() {
//        return mediaPlayerComponent.getMediaPlayer().getPosition();
//    }
//
//    @Override
//    public void setPosition(float position) {
//        mediaPlayerComponent.getMediaPlayer().setPosition(position);
//    }

    @Override
    public float getPosition() {
        return mediaPlayerComponent.getMediaPlayer().getPosition();
    }

    @Override
    public long getTime() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public void setPosition(float v) {

    }

    @Override
    public void setTime(long time) {

    }

    @Override
    public void setDuration(long duration) {

    }

    private class VideoSurfacePanel extends JPanel {

        private VideoSurfacePanel(int width, int height) {
            setBackground(Color.black);
            setOpaque(true);
            setPreferredSize(new Dimension(width, height));
            setMinimumSize(new Dimension(width, height));
            setMaximumSize(new Dimension(width, height));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            int width = image.getWidth();
            int height = image.getHeight();
            Image newImage = image.getScaledInstance((int)(width * 0.5), (int)(height * 0.5), Image.SCALE_DEFAULT);
            g2.drawImage(newImage, 0, 0, null);
//            g2.drawImage(image, null, 0, 0);
            danMuPanel.paintCallback(g);
        }
    }

    private class SimpleRenderCallbackAdapter extends RenderCallbackAdapter {
        int width;
        int height;
        private SimpleRenderCallbackAdapter(int width, int height) {
            super(new int[width * height]);
            this.width = width;
            this.height = height;
        }

        @Override
        protected void onDisplay(DirectMediaPlayer mediaPlayer, int[] rgbBuffer) {
            // Simply copy buffer to the image and repaint
            image.setRGB(0, 0, width, height, rgbBuffer, 0, width);
            videoSurface.repaint();
        }
    }
}
