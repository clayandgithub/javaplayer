package com.clayoverwind.javaplayer.view;

import com.clayoverwind.javaplayer.util.SwingUtil;
import com.sun.glass.ui.Size;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by clay on 2016/9/11.
 */
public class SingleDanMuLabel extends JLabel {

    public static final int DANMU_BASE_SPEED = 3;

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private int mSpeed = DANMU_BASE_SPEED;

    private static final Font DAN_MU_FONT = new Font("华文细黑", Font.BOLD, 24);

    // TODO read colorHexStr and lineNo from file
    public SingleDanMuLabel(String text, String colorHexStr, int lineNo) {
        super(text);
        setFont(DAN_MU_FONT);

        Size size = SwingUtil.getStringSize(text, DAN_MU_FONT);
        setSize(size.width, size.height);

//        int colorR = Integer.parseInt(colorHexStr.substring(0, 2), 16);
//        int colorG = Integer.parseInt(colorHexStr.substring(2, 4), 16);
//        int colorB = Integer.parseInt(colorHexStr.substring(4, 6), 16);
//        int colorA = Integer.parseInt(colorHexStr.substring(6, 8), 16);
        setForeground(new Color(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256)));
//        setForeground(new Color(colorR, colorG, colorB));

        mSpeed = Math.round(DANMU_BASE_SPEED * (RANDOM.nextFloat() * 2 + 1));
    }

    public int getSpeed() {
        return mSpeed;
    }
}
