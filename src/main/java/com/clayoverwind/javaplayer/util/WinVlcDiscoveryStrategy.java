package com.clayoverwind.javaplayer.util;

import uk.co.caprica.vlcj.binding.LibC;
import uk.co.caprica.vlcj.discovery.StandardNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */
public class WinVlcDiscoveryStrategy extends StandardNativeDiscoveryStrategy {
    private static final Pattern[] FILENAME_PATTERNS = new Pattern[]{Pattern.compile("libvlc\\.dll"), Pattern.compile("libvlccore\\.dll")};

    public WinVlcDiscoveryStrategy() {}

    @Override
    protected Pattern[] getFilenamePatterns() {
        return FILENAME_PATTERNS;
    }

    @Override
    public final boolean supported() {
        return RuntimeUtil.isWindows();
    }

    @Override
    protected void onGetDirectoryNames(List<String> directoryNames) {
        String installDir = WindowsRuntimeUtil.getVlcInstallDir();
        if (installDir == null) {
//            installDir = PathUtil.transferToCurrentSystemPath(Thread.currentThread().getContextClassLoader().getResource("nativelib").getPath());
            installDir = JarToolUtil.getJarDir() + System.getProperty("file.separator") + "vlclib";
        }
        System.out.printf("--------------%s\n", installDir);

        System.out.println("installDir = ------------" + installDir);
        if(installDir != null) {
            directoryNames.add(0, installDir);
        }

    }

    @Override
    public void onFound(String path) {
        if(LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_220) && System.getenv("VLC_PLUGIN_PATH") == null) {
            String pluginPath = String.format("%s\\%s", new Object[]{path, "plugins"});
            LibC.INSTANCE._putenv(String.format("%s=%s", new Object[]{"VLC_PLUGIN_PATH", pluginPath}));
        }

    }
}
