package com.clayoverwind.javaplayer.service;

import com.clayoverwind.javaplayer.Application;
import com.clayoverwind.javaplayer.event.FrameTickEvent;
import com.clayoverwind.javaplayer.event.TickEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author clayoverwind
 * @version 2017/4/8
 * @E-mail clayanddev@163.com
 */
public enum TickService {
    INSTANCE;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private AtomicBoolean isTicking = new AtomicBoolean(false);

    public void start() {
        if (!isTicking.getAndSet(true)) {
            executorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    Application.INSTANCE.post(TickEvent.INSTANCE);
                }
            }, 0, 1000, TimeUnit.MILLISECONDS);
        }
    }

    public void stop() {
        if (isTicking.getAndSet(false)) {
            executorService.shutdown();
        }
    }
}
