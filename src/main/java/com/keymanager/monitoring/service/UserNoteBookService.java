package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.UserNoteBookDao;
import com.keymanager.monitoring.entity.UserNoteBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/4/16 14:01
 **/
@Service
public class UserNoteBookService extends ServiceImpl<UserNoteBookDao, UserNoteBook> {

    @Autowired
    private UserNoteBookDao userNoteBookDao;

    public List<UserNoteBook> findUserNoteBooksByCustomerUuid (Long customerUuid) {
        return userNoteBookDao.findUserNoteBooksByCustomerUuid(customerUuid);
    }
}
