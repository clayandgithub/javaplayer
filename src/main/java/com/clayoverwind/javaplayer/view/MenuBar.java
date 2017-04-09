package com.clayoverwind.javaplayer.view;

import com.clayoverwind.javaplayer.util.JarToolUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */
public class MenuBar extends JMenuBar {
    private MainWindow parentWindow;

    public MenuBar(MainWindow parentWindow) {
        super();
        this.parentWindow = parentWindow;
        addFileMenu();
        addHelpMenu();
    }

    private void addFileMenu() {
        JMenu menu = new JMenu("文件");
        add(menu);
        JMenuItem item1 = new JMenuItem("导入视频");
        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importMedia();
            }
        });

        JMenuItem item2 = new JMenuItem("导入字幕");
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importSubtitle();
            }
        });

        JMenuItem item3 = new JMenuItem("导入弹幕");
        item3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importDanmu();
            }
        });

        menu.add(item1);
        menu.addSeparator();
        menu.add(item2);
        menu.addSeparator();
        menu.add(item3);
    }

    private void addHelpMenu() {
        JMenu menu = new JMenu("帮助");
        add(menu);
        JMenuItem item1 = new JMenuItem("软件说明");
        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showIntroductionDialog();
            }
        });

        JMenuItem item2 = new JMenuItem("项目地址");
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCodeAddressDialog();
            }
        });

        JMenuItem item3 = new JMenuItem("关于作者");
        item3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAuthorDialog();
            }
        });

        menu.add(item1);
        menu.addSeparator();
        menu.add(item2);
        menu.addSeparator();
        menu.add(item3);
    }

    private void showIntroductionDialog() {
        JOptionPane.showMessageDialog(parentWindow, "详见https://github.com/clayandgithub/javaplayer", "软件说明", JOptionPane.PLAIN_MESSAGE);
    }

    private void showCodeAddressDialog() {
        JOptionPane.showMessageDialog(parentWindow, "https://github.com/clayandgithub/javaplayer", "项目地址", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAuthorDialog() {
        JOptionPane.showMessageDialog(parentWindow, "详见http://clayandgithub.github.io/", "关于作者", JOptionPane.INFORMATION_MESSAGE);
    }

    private void importMedia() {
        String filePath = chooseFileToImport("请选择视频文件:");
        if (filePath != null) {
            parentWindow.playMedia(filePath);
        }
    }

    private void importSubtitle() {
        String filePath = chooseFileToImport("请选择字幕文件:");
        if (filePath != null) {
            parentWindow.setSubTitleFile(filePath);
        }
    }

    private void importDanmu() {
        JOptionPane.showMessageDialog(parentWindow, "暂不支持", "抱歉", JOptionPane.INFORMATION_MESSAGE);
    }

    public String chooseFileToImport(String dialogTitle) {
        JFileChooser jfc = new JFileChooser(JarToolUtil.getJarFilePath());
        jfc.setDialogTitle(dialogTitle);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = jfc.showOpenDialog(parentWindow);
        if(JFileChooser.APPROVE_OPTION == result) {
            File file = jfc.getSelectedFile();
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }
}
