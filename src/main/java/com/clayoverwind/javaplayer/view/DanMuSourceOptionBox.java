package com.clayoverwind.javaplayer.view;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by clay on 2016/9/10.
 */
public class DanMuSourceOptionBox extends JComboBox{

    public DanMuSourceOptionBox(String[] danmuSourceOptions, final ChooseDanMuFileDialog parent) {
        super(danmuSourceOptions);

        final DanMuSourceOptionBox box = this;
        this.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    parent.onChangeDanMuResource(box.getSelectedIndex());
                    parent.repaint();
                } else if (event.getStateChange() == ItemEvent.DESELECTED){
                }
            }
        });
    }
}
