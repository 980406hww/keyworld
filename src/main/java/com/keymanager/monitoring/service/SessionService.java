package com.keymanager.monitoring.service;

import com.keymanager.monitoring.entity.UserOnline;

import java.util.List;

public interface SessionService {

    List<UserOnline> list();

    boolean forceLogout(String sessionId);
}
