package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.UserMessageCriteria;
import com.keymanager.monitoring.entity.UserMessage;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/2/28 16:56
 **/
public interface UserMessageDao extends BaseMapper<UserMessage> {

    List<UserMessage> getUserMessageLists(Page<UserMessage> page, @Param("userMessageCriteria") UserMessageCriteria userMessageCriteria);

    UserMessage getUserMessageByUuid(@Param("uuid")Integer uuid);

    void saveUserMessages(@Param("userMessageCriteria") UserMessageCriteria userMessageCriteria, @Param("userName") String userName, @Param("now") Date now);

    void updateUserMessages(@Param("userMessageCriteria") UserMessageCriteria userMessageCriteria, @Param("now") Date now);

    Integer checkMessageInboxCount(@Param("userName") String userName);
}
