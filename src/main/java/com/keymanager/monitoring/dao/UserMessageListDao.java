package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.UserMessageListCriteria;
import com.keymanager.monitoring.entity.UserMessageList;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/2/28 16:56
 **/
public interface UserMessageListDao extends BaseMapper<UserMessageList> {

    List<UserMessageList> getUserMessageLists(Page<UserMessageList> page, @Param("userMessageListCriteria") UserMessageListCriteria userMessageListCriteria);

    UserMessageList getUserMessageByUuid(@Param("uuid")Integer uuid);

    void saveUserMessages( @Param("userMessageListCriteria") UserMessageListCriteria userMessageListCriteria, @Param("userName") String userName, @Param("now") Date now);

    void updateUserMessages( @Param("userMessageListCriteria") UserMessageListCriteria userMessageListCriteria, @Param("now") Date now);

    Integer checkMessageInboxCount(@Param("userName") String userName);
}
