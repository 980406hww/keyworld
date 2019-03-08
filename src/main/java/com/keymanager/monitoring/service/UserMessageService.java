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

    public UserMessage getUserMessageByUuid(Integer uuid){
        return userMessageDao.getUserMessageByUuid(uuid);
    }

    public void saveUserMessages(UserMessageCriteria userMessageCriteria, String userName){
        Date now = new Date();
        if (userMessageCriteria.getUuid() == null){
            userMessageDao.saveUserMessages(userMessageCriteria, userName, now);
        }else {
            userMessageDao.updateUserMessages(userMessageCriteria, now);
        }
    }

    public Integer checkMessageInboxCount(String userName){
        return userMessageDao.checkMessageInboxCount(userName);
    }
}
