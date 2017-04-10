package com.clayoverwind.javaplayer.util;

import com.sun.jna.NativeLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.Info;
import uk.co.caprica.vlcj.discovery.NativeDiscoveryStrategy;
import uk.co.caprica.vlcj.discovery.linux.DefaultLinuxNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.discovery.mac.DefaultMacNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.discovery.windows.DefaultWindowsNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * @author clayoverwind
 * @version 2017/4/9
 * @E-mail clayanddev@163.com
 */

public class NativeDiscovery {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String JNA_SYSTEM_PROPERTY_NAME = "jna.library.path";
    private final NativeDiscoveryStrategy[] discoveryStrategies;

    public NativeDiscovery(NativeDiscoveryStrategy... discoveryStrategies) {
        this.discoveryStrategies = discoveryStrategies;
    }

    public NativeDiscovery() {
        this(new NativeDiscoveryStrategy[]{new DefaultLinuxNativeDiscoveryStrategy(), new DefaultWindowsNativeDiscoveryStrategy(), new DefaultMacNativeDiscoveryStrategy()});
    }

    public final boolean discover() {
        LOGGER.info("discover()");
        String jnaLibraryPath = System.getProperty(JNA_SYSTEM_PROPERTY_NAME);
        LOGGER.info("jnaLibraryPath={}", jnaLibraryPath);
        if(jnaLibraryPath == null) {
            NativeDiscoveryStrategy[] var2 = this.discoveryStrategies;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                NativeDiscoveryStrategy discoveryStrategy = var2[var4];
                LOGGER.info("discoveryStrategy={}", discoveryStrategy);
                boolean supported = discoveryStrategy.supported();
                LOGGER.info("supported={}", Boolean.valueOf(supported));
                if(supported) {
                    String path = discoveryStrategy.discover();
                    LOGGER.info("path={}", path);
                    if(path != null) {
                        LOGGER.info("Discovery found libvlc at \'{}\'", path);
                        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), path);
                        discoveryStrategy.onFound(path);
                        return true;
                    }
                }
            }

            LOGGER.warn("Discovery did not find libvlc");
        } else {
            LOGGER.info("Skipped discovery as system property \'{}\' already set to \'{}\'", "jna.library.path", jnaLibraryPath);
        }

        return false;
    }

    static {
        Info.getInstance();
    }
}