package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.UserNoteBook;
import java.util.List;

public interface UserNoteBookService extends IService<UserNoteBook> {

    List<UserNoteBook> findUserNoteBooks(Long qzUuid, String terminalType, Integer searchAll);

    int saveUserNoteBook(UserNoteBook userNoteBook);

    void deleteUserNoteBook(Long qzUuid);
}
