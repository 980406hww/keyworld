package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.UserNoteBook;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("userNoteBookDao2")
public interface UserNoteBookDao extends BaseMapper<UserNoteBook> {

    List<UserNoteBook> findUserNoteBooks(@Param("qzUuid") Long qzUuid, @Param("terminalType") String terminalType, @Param("searchAll") Integer searchAll);

    void deleteUserNoteBook(@Param("qzUuid") Long qzUuid);
}
