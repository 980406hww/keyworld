package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.KeywordManagerSessionCriteria;
import com.keymanager.monitoring.dao.KeywordManagerSessionDao;
import com.keymanager.monitoring.entity.KeywordManagerSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Enumeration;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoukai
 * @since 2019-08-09
 */
@Service
public class KeywordManagerSessionService extends ServiceImpl<KeywordManagerSessionDao, KeywordManagerSession> {

    @Autowired
    private KeywordManagerSessionDao keywordManagerSessionDao;

    public void saveKeywordManagerSessions(HttpSession session) {
        String sessionId = session.getId();
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            KeywordManagerSession existingKeywordManagerSession = keywordManagerSessionDao.findExistingKeywordManagerSession(sessionId, attributeName);
            if (null == existingKeywordManagerSession) {
                existingKeywordManagerSession = new KeywordManagerSession();
                existingKeywordManagerSession.setSessionID(sessionId);
                existingKeywordManagerSession.setAttributeName(attributeName);
                existingKeywordManagerSession.setContent((String) session.getServletContext().getAttribute(attributeName));
                existingKeywordManagerSession.setStatus("已创建");
                existingKeywordManagerSession.setCreateTime(new Date());
                keywordManagerSessionDao.insert(existingKeywordManagerSession);
            } else {
                existingKeywordManagerSession.setUpdateTime(new Date());
                keywordManagerSessionDao.updateById(existingKeywordManagerSession);
            }
        }
    }

    public Page<KeywordManagerSession> searchKeywordManagerSession(Page<KeywordManagerSession> page, KeywordManagerSessionCriteria criteria) {
        page.setRecords(keywordManagerSessionDao.searchKeywordManagerSessions(page, criteria));
        return page;
    }
}
