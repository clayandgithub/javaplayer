package com.clayoverwind.javaplayer.presenter;

import com.clayoverwind.javaplayer.model.DanMuResource;
import com.clayoverwind.javaplayer.util.FileUtil;

import java.awt.*;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */
public enum DanMuPanelPresenter {
    INSTANCE;

    public static final String DANMU_RESOURCE_DIR = "danmures";

    public static final String BILIBILI_DANMU_RESOURCE_DIR = "danmures/bilibili";

    static {
        FileUtil.createDir(DANMU_RESOURCE_DIR);
        FileUtil.createDir(BILIBILI_DANMU_RESOURCE_DIR);
    }

    public static final String BILIBILI_DANMU_FILE_PATH_PATTERN = BILIBILI_DANMU_RESOURCE_DIR + "/VIDEO_NAME.xml";

    public static final String BILIBILI_DANMU_URL_PATH_PATTERN = "http://comment.bilibili.tv/CID.xml";

    public static final int MSPF = 25;

    public static final int DANMU_BASE_SPEED = 3;

    public static final String BILIBILI_URL_SAMPLE = "例：http://www.bilibili.com/video/av2203684/";

    public static final Font DEFAULT_FONT = new Font("微软雅黑", Font.PLAIN, 14);

    private DanMuResource danMuResource = new DanMuResource();

}
