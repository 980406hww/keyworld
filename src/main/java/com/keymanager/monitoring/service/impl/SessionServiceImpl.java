package com.keymanager.monitoring.service.impl;

import com.keymanager.ckadmin.common.shiro.ShiroUser;
import com.keymanager.monitoring.entity.UserOnline;
import com.keymanager.monitoring.service.SessionService;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionDAO sessionDAO;

    @Override
    public List<UserOnline> list() {
        List<UserOnline> list = new ArrayList<>();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            UserOnline userOnline = new UserOnline();
            SimplePrincipalCollection principalCollection;
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                userOnline.setShiroUser((ShiroUser) principalCollection.getPrimaryPrincipal());
            }
            userOnline.setId((String) session.getId());
            userOnline.setHost(session.getHost());
            userOnline.setTerminalType((String) session.getAttribute("terminalType"));
            userOnline.setEntryType((String) session.getAttribute("entryType"));
            userOnline.setStartTimestamp(session.getStartTimestamp());
            userOnline.setLastAccessTime(session.getLastAccessTime());
            long timeout = session.getTimeout();
            if (timeout == 0L) {
                userOnline.setStatus("离线");
            } else {
                userOnline.setStatus("在线");
            }
            userOnline.setTimeout(timeout);
            list.add(userOnline);
        }
        return list;
    }

    @Override
    public boolean forceLogout(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        session.setTimeout(0);
        return true;
    }
}
