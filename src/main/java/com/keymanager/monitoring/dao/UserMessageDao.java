package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.UserMessageCriteria;
import com.keymanager.monitoring.entity.UserMessage;
import com.keymanager.monitoring.vo.UserMessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/2/28 16:56
 **/
public interface UserMessageDao extends BaseMapper<UserMessage> {

    List<UserMessageVO> getUserMessages(Page<UserMessageVO> page, @Param("userMessageCriteria") UserMessageCriteria userMessageCriteria);

    UserMessage getUserMessage(@Param("userMessageCriteria")UserMessageCriteria userMessageCriteria, @Param("type") boolean type);

    void saveUserMessages(@Param("userMessageCriteria") UserMessageCriteria userMessageCriteria, @Param("now") Date now);

    void updateUserMessages(@Param("userMessageCriteria") UserMessageCriteria userMessageCriteria, @Param("now") Date now);

    Integer checkMessageInboxCount(@Param("userName") String userName);

    List<UserMessage> getHistoryUserMessages (@Param("userName") String userName, @Param("customerUuid") long customerUuid, @Param("type") String type);
}
