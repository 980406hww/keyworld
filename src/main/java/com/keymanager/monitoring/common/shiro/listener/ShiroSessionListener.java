package com.keymanager.monitoring.common.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class ShiroSessionListener implements SessionListener {

    private static final Logger log = LoggerFactory.getLogger(ShiroSessionListener.class);
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    @Override
    public void onStart(Session session) {
//        log.info("====创建session====");
        sessionCount.incrementAndGet();
    }

    @Override
    public void onStop(Session session) {
//        log.info("====中途销毁session====");
        sessionCount.decrementAndGet();
    }

    @Override
    public void onExpiration(Session session) {
//        log.info("====过期销毁session====");
        sessionCount.decrementAndGet();
    }
}
