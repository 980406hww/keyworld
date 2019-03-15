package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.UserMessageCriteria;
import com.keymanager.monitoring.dao.UserMessageDao;
import com.keymanager.monitoring.entity.UserMessage;
import com.keymanager.monitoring.vo.UserMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/2/28 16:58
 **/
@Service
public class UserMessageService extends ServiceImpl<UserMessageDao, UserMessage> {

    @Autowired
    private UserMessageDao userMessageDao;

    public UserMessageVO getUserMessages(UserMessageCriteria userMessageCriteria) {
        UserMessageVO userMessageVo = new UserMessageVO();
        userMessageVo.setMessageStatus(userMessageCriteria.getMessageStatus());
        Page<UserMessage> page = new Page<UserMessage>(userMessageCriteria.getPageNumber(), 10);
        page.setRecords(userMessageDao.getUserMessages(page, userMessageCriteria));
        userMessageVo.setPage(page);
        return userMessageVo;
    }

    public UserMessage getUserMessage(UserMessageCriteria userMessageCriteria){
        return userMessageDao.getUserMessage(userMessageCriteria);
    }

    public void saveUserMessages(UserMessageCriteria userMessageCriteria){
        Date now = new Date();
        if (null == userMessageCriteria.getUuid()) {
            userMessageDao.saveUserMessages(userMessageCriteria, now);
        } else {
            if (userMessageCriteria.isUpdateStatus()) {
                userMessageDao.updateUserMessages(userMessageCriteria, now);
            } else {
                UserMessage userMessage = userMessageDao.getUserMessage(userMessageCriteria);
                if (null == userMessage) {
                    userMessageDao.saveUserMessages(userMessageCriteria, now);
                }
            }
        }
    }

    public Integer checkMessageInboxCount(String userName){
        return userMessageDao.checkMessageInboxCount(userName);
    }

    public List<UserMessage> getHistoryUserMessages (long customerUuid, String type) {
        return userMessageDao.getHistoryUserMessages(customerUuid, type);
    }
}
