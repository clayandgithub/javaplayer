package com.clayoverwind.javaplayer.presenter;

import com.clayoverwind.javaplayer.view.MainWindow;
import com.clayoverwind.javaplayer.view.MenuBar;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */
public enum MenuBarPresenter {
    INSTANCE;

    public MenuBar createMenuBar(MainWindow mainWindow) {
        return new MenuBar(mainWindow);
    }
}
