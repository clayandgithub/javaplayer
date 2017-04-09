package com.clayoverwind.javaplayer.iview;

import com.clayoverwind.javaplayer.presenter.DanMuPlayer;

import javax.swing.*;
import java.awt.*;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */
public interface IDanMuPanel {
    JComponent getComponent();

    DanMuPlayer getDanMuPlayer();
}
