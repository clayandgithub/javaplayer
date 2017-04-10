package com.clayoverwind.javaplayer.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextfieldHintListener implements FocusListener {
    private String mHindText;
    private JTextField mTextField;

    public JTextfieldHintListener(String hintText, JTextField textField) {
        this.mHindText = hintText;
        this.mTextField = textField;
        textField.setForeground(Color.GRAY);
    }
    @Override
    public void focusGained(FocusEvent e) {
        String temp = mTextField.getText();
        if(temp.equals(mHindText)){
            mTextField.setText("");
            mTextField.setForeground(Color.BLACK);
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        String temp = mTextField.getText();
        if(temp.equals("")){
            mTextField.setForeground(Color.GRAY);
            mTextField.setText(mHindText);
        }
    }
}