package com.clayoverwind.javaplayer.strategy;

import com.clayoverwind.javaplayer.Application;
import com.clayoverwind.javaplayer.event.AfterExitFullScreenEvent;
import com.clayoverwind.javaplayer.event.BeforeEnterFullScreenEvent;
import uk.co.caprica.vlcj.player.embedded.DefaultAdaptiveRuntimeFullScreenStrategy;

import java.awt.*;

public final class JavaPlayerFullScreenStrategy extends DefaultAdaptiveRuntimeFullScreenStrategy {

    public JavaPlayerFullScreenStrategy(Window window) {
        super(window);
    }

    @Override
    protected void beforeEnterFullScreen() {
        Application.INSTANCE.post(BeforeEnterFullScreenEvent.INSTANCE);
    }

    @Override
    protected  void afterExitFullScreen() {
        Application.INSTANCE.post(AfterExitFullScreenEvent.INSTANCE);
    }
}
