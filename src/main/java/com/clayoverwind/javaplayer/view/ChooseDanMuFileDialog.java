package com.clayoverwind.javaplayer.view;

import com.clayoverwind.javaplayer.presenter.DanMuWindowPresenter;
import com.clayoverwind.javaplayer.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author clayoverwind
 * @version 2017/4/10
 * @E-mail clayanddev@163.com
 */
public class ChooseDanMuFileDialog extends JDialog {

    public static final String DANMU_RESOURCE_DIR = "danmures";

    public static final String BILIBILI_DANMU_RESOURCE_DIR = "danmures/bilibili";

    static {
        FileUtil.createDir(DANMU_RESOURCE_DIR);
        FileUtil.createDir(BILIBILI_DANMU_RESOURCE_DIR);
    }

    public static final String[] DANMU_RESOURCE_OPTIONS = {"本地文件", "bilibili"};
    public static final int LOCAL_FILE_OPTION_INDEX = 0;
    public static final int BILIBILI_OPTION_INDEX = 1;

    public static final String BILIBILI_URL_SAMPLE = "例：http://www.bilibili.com/video/av2203684/";


    public static final String BILIBILI_DANMU_FILE_PATH_PATTERN = BILIBILI_DANMU_RESOURCE_DIR + "/VIDEO_NAME.xml";

    public static final String BILIBILI_DANMU_URL_PATH_PATTERN = "http://comment.bilibili.tv/CID.xml";

    private DanMuSourceOptionBox sourceOptionBox;
    private JTextField urlInput;
    private JButton initDanMuButton;

    public ChooseDanMuFileDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        setTitle("导入弹幕");
        setSize(500, 200);
        SwingUtil.layoutAtScreenCenter(this);
        setResizable(false);
        addComponents();
    }

    private void addComponents() {
        final GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5,5,5,5);

        // add mDanMuSourceOptionBox
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        sourceOptionBox = new DanMuSourceOptionBox(DANMU_RESOURCE_OPTIONS, this);
        this.add(sourceOptionBox, constraints);

        // add mURLInput
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        urlInput = new JTextField(15);
        urlInput.setEnabled(false);
        urlInput.addFocusListener(new JTextfieldHintListener(BILIBILI_URL_SAMPLE, urlInput));
        this.add(urlInput, constraints);

        // add mInitDanMuButton
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        initDanMuButton = new JButton("导入");
        initDanMuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initDanMuButtonClicked();
            }
        });
        this.add(initDanMuButton, constraints);
    }

    public void initDanMuButtonClicked() {
        String filePath = null;
        switch (sourceOptionBox.getSelectedIndex()) {
            case LOCAL_FILE_OPTION_INDEX:
                filePath = chooseDanMuFileToPlay();
                if (filePath != null) {
                    urlInput.setText(filePath);
                    //TODO
                    DanMuWindowPresenter.INSTANCE.loadDanMuResourceFromFile(filePath, true);
                    dispose();
                }
                break;
            case BILIBILI_OPTION_INDEX:
                String url = urlInput.getText();
                if (validateBilibiliURL(url)) {
                    urlInput.setText(url);
                    filePath = ClawUtil.getBilibiliDanmuFileByUrl(url);
                    DanMuWindowPresenter.INSTANCE.loadDanMuResourceFromFile(filePath, true);
                    dispose();
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(this, "请输入正确的网址！（" + BILIBILI_URL_SAMPLE + "）", "错误", JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
        }
    }

    public void onChangeDanMuResource(int selectedIndex) {
        switch (selectedIndex) {
            case LOCAL_FILE_OPTION_INDEX:
                urlInput.setText(null);
                urlInput.setEnabled(false);
                break;
            case BILIBILI_OPTION_INDEX:
                urlInput.setText(BILIBILI_URL_SAMPLE);
                urlInput.setEnabled(true);
                break;
            default:
        }
    }

    public String chooseDanMuFileToPlay() {
        JFileChooser jfc = new JFileChooser(JarToolUtil.getJarDir() + "/" + DANMU_RESOURCE_DIR);
        jfc.setDialogTitle("请选择弹幕文件:");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = jfc.showOpenDialog(this);
        if(JFileChooser.APPROVE_OPTION == result) {
            File file = jfc.getSelectedFile();
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    private boolean validateBilibiliURL(String url) {
        return url.matches("(http://)?(www.bilibili.com/video/av([0-9])*)(/)?");
    }
}
