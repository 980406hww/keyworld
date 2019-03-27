package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ScreenedWebsiteDao;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.ScreenedWebsite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Service
public class ScreenedWebsiteService extends ServiceImpl<ScreenedWebsiteDao, ScreenedWebsite> {

    @Autowired
    private ScreenedWebsiteDao screenedWebsiteDao;

    public ModelAndView constructSearchScreenedWebsiteListsModelAndView(int currentPageNumber, int pageSize, ScreenedWebsite screenedWebsite) {
        ModelAndView modelAndView = new ModelAndView("/screenedWebsite/screenedWebsite");
        Page<ScreenedWebsite> page = new Page<>();
        page.setRecords(screenedWebsiteDao.searchCustomerKeywordListsPage(new Page<CustomerKeyword>(currentPageNumber, pageSize), screenedWebsite));
        modelAndView.addObject("screenedWebsite", screenedWebsite);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    public void saveScreenedWebsite(ScreenedWebsite screenedWebsite) {
        if (null != screenedWebsite.getUuid()) {
            screenedWebsite.setUpdateTime(new Date());
            screenedWebsiteDao.updateById(screenedWebsite);
        } else {
            screenedWebsiteDao.insert(screenedWebsite);
        }
    }

    public void deleteBatchScreenedWebsite(List<String> uuids) {
        screenedWebsiteDao.deleteBatchIds(uuids);
    }

    public ScreenedWebsite getScreenedWebsite(Long uuid){
        return screenedWebsiteDao.selectById(uuid);
    }

    public void delScreenedWebsite(Long uuid){
        screenedWebsiteDao.deleteById(uuid);
    }
}
