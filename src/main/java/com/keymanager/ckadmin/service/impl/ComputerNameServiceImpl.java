package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.dao.ComputerNameDao;
import com.keymanager.ckadmin.entity.ComputerName;
import com.keymanager.ckadmin.service.ComputerNameService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("computerNameService")
public class ComputerNameServiceImpl implements ComputerNameService {

    @Resource(name = "computerNameDao")
    private ComputerNameDao computerNameDao;

    @Override
    public synchronized int getSequence(String namePrefix) {
        ComputerName computerName = computerNameDao.findComputerName(namePrefix);
        if(computerName == null){
            computerNameDao.addComputerName(namePrefix, 1);
            return 1;
        }else{
            computerNameDao.updateComputerNameSequence(namePrefix, computerName.getSequence() + 1);
            return computerName.getSequence() + 1;
        }
    }
}
