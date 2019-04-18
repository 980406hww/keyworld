package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.UserNoteBook;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/4/16 14:03
 **/

public interface UserNoteBookDao extends BaseMapper<UserNoteBook> {

    List<UserNoteBook> findUserNoteBooksByCustomerUuid (@Param("customerUuid") Long customerUuid, @Param("terminalType") String terminalType, @Param("searchAll") Integer searchAll);
}
