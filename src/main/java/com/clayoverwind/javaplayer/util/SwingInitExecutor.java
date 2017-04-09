package com.clayoverwind.javaplayer.util;

import javax.swing.*;

/**
 * @author clayoverwind
 * @version 2017/4/7
 * @E-mail clayanddev@163.com
 */
public class SwingInitExecutor {
    public static void execute(final SwingInitable target) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!target.swingInit()) {
                    System.out.println("Init failed!");
                }
            }
        });
    }
}
