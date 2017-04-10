package com.clayoverwind.javaplayer.iview;

import javax.swing.*;
import java.awt.*;

/**
 * @author clayoverwind
 * @version 2017/4/8
 * @E-mail clayanddev@163.com
 */
public interface IMainWindow {
    void playMedia(String path);

    Component getComponent();

    JFrame getWindow();

    void setSubTitleFile(String filePath);

    Component getVideoContentPane();
}
