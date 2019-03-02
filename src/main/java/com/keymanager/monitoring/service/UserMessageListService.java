package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.UserMessageListCriteria;
import com.keymanager.monitoring.dao.UserMessageListDao;
import com.keymanager.monitoring.entity.UserMessageList;
import com.keymanager.monitoring.vo.UserMessageListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author zhoukai
 * @Date 2019/2/28 16:58
 **/
@Service
public class UserMessageListService  extends ServiceImpl<UserMessageListDao, UserMessageList> {

    @Autowired
    private UserMessageListDao userMessageListDao;

    public UserMessageListVO getUserMessageList(UserMessageListCriteria userMessageListCriteria) {
        UserMessageListVO userMessageListVo = new UserMessageListVO();
        userMessageListVo.setMessageStatus(userMessageListCriteria.getMessageStatus());
        userMessageListVo.setPageNumber(userMessageListCriteria.getPageNumber());
        userMessageListVo.setStatus(userMessageListCriteria.getStatus());
        userMessageListVo.setTargetUserName(userMessageListCriteria.getTargetUserName());
        Page<UserMessageList> page = new Page<UserMessageList>(userMessageListCriteria.getPageNumber(), 10);
        page.setRecords(userMessageListDao.getUserMessageLists(page, userMessageListCriteria));
        userMessageListVo.setPageTotalNumber(page.getPages());
        userMessageListVo.setUserMessageLists(page.getRecords());
        return userMessageListVo;
    }

    public UserMessageList getUserMessageByUuid(Integer uuid){
        return userMessageListDao.getUserMessageByUuid(uuid);
    }

    public void saveUserMessages(UserMessageListCriteria userMessageListCriteria, String userName){
        Date now = new Date();
        if (userMessageListCriteria.getUuid() == null){
            userMessageListDao.saveUserMessages(userMessageListCriteria, userName, now);
        }else {
            userMessageListDao.updateUserMessages(userMessageListCriteria, now);
        }
    }

    public Integer checkMessageInboxCount(String userName){
        return userMessageListDao.checkMessageInboxCount(userName);
    }
}
