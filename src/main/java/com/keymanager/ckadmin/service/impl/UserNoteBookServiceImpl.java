package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.UserNoteBookDao;
import com.keymanager.ckadmin.entity.UserNoteBook;
import com.keymanager.ckadmin.service.UserNoteBookService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("userNoteBookService2")
public class UserNoteBookServiceImpl extends ServiceImpl<UserNoteBookDao, UserNoteBook> implements
    UserNoteBookService {

    @Resource(name = "userNoteBookDao2")
    private UserNoteBookDao userNoteBookDao;

    @Override
    public List<UserNoteBook> findUserNoteBooks(Long customerUuid, String terminalType,
        Integer searchAll) {
        return userNoteBookDao.findUserNoteBooks(customerUuid, terminalType, searchAll);
    }
}
