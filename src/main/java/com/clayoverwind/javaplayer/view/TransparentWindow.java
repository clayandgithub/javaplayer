/**
 * TransparentWindow.java
 * Copyright 2016 escenter@zju.edu.cn, all rights reserved.
 * any form of usage is subject to approval.
 */
package com.clayoverwind.javaplayer.view;

import com.sun.awt.AWTUtilities;

import javax.swing.*;

/**
 * @author wangweiwei
 *
 */
public class TransparentWindow extends JFrame {

    private static final long serialVersionUID = 5816079399320136426L;
    
    public TransparentWindow(String title, int width, int height) {
        super(title);
        this.setSize(width, height);
        this.setUndecorated(true);
        AWTUtilities.setWindowOpaque(this, false);
    }

}
