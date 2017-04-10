package com.clayoverwind.javaplayer;

import com.clayoverwind.javaplayer.presenter.DanMuWindowPresenter;
import com.clayoverwind.javaplayer.presenter.MainWindowPresenter;
import com.google.common.eventbus.EventBus;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * @author clayoverwind
 * @version 2017/4/7
 * @E-mail clayanddev@163.com
 */
public enum Application {
    INSTANCE;
    private final EventBus eventBus = new EventBus();

    public static void main(String[] args) {
        MainWindowPresenter.INSTANCE.start();
    }

    public void subscribe(Object subscriber) {
        eventBus.register(subscriber);
    }

    public void post(Object event) {
        // Events are always posted and processed on the Swing Event Dispatch thread
        if (SwingUtilities.isEventDispatchThread()) {
            eventBus.post(event);
        } else {
            SwingUtilities.invokeLater(() -> eventBus.post(event));
        }
    }

    private static final String RESOURCE_BUNDLE_BASE_NAME = "strings/vlcj-player";
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME);

    public static ResourceBundle resources() {
        return resourceBundle;
    }
}
