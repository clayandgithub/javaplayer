package com.clayoverwind.javaplayer.view;

import com.clayoverwind.javaplayer.Application;
import com.clayoverwind.javaplayer.event.AfterExitFullScreenEvent;
import com.clayoverwind.javaplayer.event.BeforeEnterFullScreenEvent;
import com.clayoverwind.javaplayer.event.ShutdownEvent;
import com.clayoverwind.javaplayer.iview.IDanMuPanel;
import com.clayoverwind.javaplayer.iview.IMainWindow;
import com.clayoverwind.javaplayer.iview.ITimeSlider;
import com.clayoverwind.javaplayer.iview.IVideoContentPane;
import com.clayoverwind.javaplayer.presenter.MenuBarPresenter;
import com.clayoverwind.javaplayer.presenter.VideoContentPanePresenter;
import com.google.common.eventbus.Subscribe;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.streams.NativeStreams;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author clayoverwind
 * @version 2017/4/7
 * @E-mail clayanddev@163.com
 */

public class MainWindow extends JFrame implements IMainWindow {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * native info
     */
    private static final NativeStreams nativeStreams;
    // Redirect the native output streams to files, useful since VLC can generate a lot of noisy native logs we don't care about
    // (on the other hand, if we don't look at the logs we might won't see errors)
    static {
        if (RuntimeUtil.isNix()) {
            nativeStreams = new NativeStreams("stdout.log", "stderr.log");
        }
        else {
            nativeStreams = null;
        }
    }

    /**
     * actions and key bindings
     */
    private static final String ACTION_EXIT_FULLSCREEN = "exit-fullscreen";
    private static final KeyStroke KEYSTROKE_ESCAPE = KeyStroke.getKeyStroke("ESCAPE");
    private static final KeyStroke KEYSTROKE_TOGGLE_FULLSCREEN = KeyStroke.getKeyStroke("F11");

    /**
     * sub views
     */
    private IVideoContentPane videoContentPane;

    private JPanel bottomPane;

    private MenuBar menuBar;

    private IDanMuPanel danMuPanel;

    public MainWindow(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Application.INSTANCE.subscribe(this);
        setLocation(100, 100);
        setSize(600, 400);
        setupContentPane();
        setupMenuBar();
        setAlwaysOnTop(true);
        setVisible(true);

        addActions();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                videoContentPane.stop();
                if (nativeStreams != null) {
                    nativeStreams.release();
                }
                Application.INSTANCE.post(ShutdownEvent.INSTANCE);
            }
        });
    }

    private void setupMenuBar() {
        menuBar = MenuBarPresenter.INSTANCE.createMenuBar(this);
        this.setJMenuBar(menuBar);
    }

    private void addActions() {
        getActionMap().put(ACTION_EXIT_FULLSCREEN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                videoContentPane.toggleFullScreen();
//                videoFullscreenAction.select(false);
            }
        });
    }

    private void setupContentPane() {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        addUpPanel(contentPane);
        addBottomPane(contentPane);

        setContentPane(contentPane);
    }

    private void addUpPanel(JPanel contentPane) {
//        danMuPanel = new DanMuPanel(600, 200);

        videoContentPane = VideoContentPanePresenter.INSTANCE.createVideoContentPane(this);
        contentPane.add(videoContentPane.getComponent(), BorderLayout.CENTER);

//        contentPane.setTransferHandler(new MediaTransferHandler() {
//            @Override
//            protected void onMediaDropped(String[] uris) {
//                mediaPlayerComponent.getMediaPlayer().playMedia(uris[0]);
//            }
//        });
    }

//    private void addBottomPane(JPanel contentPane) {
//        bottomPane = new JPanel();
//        bottomPane.setLayout(new OverlayLayout(bottomPane));
//
//        danMuPanel = new DanMuPanel(600, 200);
//        danMuPanel.getComponent().setAlignmentX(0);
//        danMuPanel.getComponent().setAlignmentY(0);
//        danMuPanel.getComponent().setBackground(Color.yellow);
//        bottomPane.add(danMuPanel.getComponent());
//
//        JPanel bottomControlsPane = new JPanel();
//        bottomControlsPane.setLayout(new MigLayout("fill, insets 0 n n n", "[grow]", "[]0[]"));
//        bottomControlsPane.add(new ControlsPane(videoContentPane), "grow, wrap");
//        bottomControlsPane.setAlignmentX(0);
//        bottomControlsPane.setAlignmentY(0);
//        bottomPane.add(bottomControlsPane);
//        bottomControlsPane.setBackground(Color.blue);
//
//        contentPane.add(bottomPane, BorderLayout.SOUTH);
//    }

    private void addBottomPane(JPanel contentPane) {
        bottomPane = new JPanel();
        bottomPane.setLayout(new BorderLayout());

        JPanel bottomControlsPane = new JPanel();
        bottomControlsPane.setLayout(new MigLayout("fill, insets 0 n n n", "[grow]", "[]0[]"));
        bottomControlsPane.add(new ControlsPane(videoContentPane), "grow, wrap");
        bottomPane.add(bottomControlsPane, BorderLayout.CENTER);

//        statusBar = new StatusBar();
//        bottomPane.add(statusBar, BorderLayout.SOUTH);

        contentPane.add(bottomPane, BorderLayout.SOUTH);
    }

    @Override
    public void playMedia(String path) {
        videoContentPane.playMedia(path);
    }

    @Subscribe
    public void onBeforeEnterFullScreen(BeforeEnterFullScreenEvent event) {
        menuBar.setVisible(false);
        bottomPane.setVisible(false);
        // As the menu is now hidden, the shortcut will not work, so register a temporary key-binding
        registerEscapeBinding();
    }

    @Subscribe
    public void onAfterExitFullScreen(AfterExitFullScreenEvent event) {
        deregisterEscapeBinding();
        menuBar.setVisible(true);
        bottomPane.setVisible(true);
    }

    private void registerEscapeBinding() {
        getInputMap().put(KEYSTROKE_ESCAPE, ACTION_EXIT_FULLSCREEN);
        getInputMap().put(KEYSTROKE_TOGGLE_FULLSCREEN, ACTION_EXIT_FULLSCREEN);
    }

    private void deregisterEscapeBinding() {
        getInputMap().remove(KEYSTROKE_ESCAPE);
        getInputMap().remove(KEYSTROKE_TOGGLE_FULLSCREEN);
    }

    private InputMap getInputMap() {
        JComponent c = (JComponent) getContentPane();
        return c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private ActionMap getActionMap() {
        JComponent c = (JComponent) getContentPane();
        return c.getActionMap();
    }

    public void setSubTitleFile(String subTitleFile) {
        videoContentPane.setSubTitleFile(subTitleFile);
    }

    @Override
    public void playDanMu(String path) {
        danMuPanel.getDanMuPlayer().playDanMu(path);
    }
}
