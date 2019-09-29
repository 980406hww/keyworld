package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.WarnListCriteria;
import com.keymanager.ckadmin.entity.WarnList;
import java.util.List;

public interface WarnListService extends IService<WarnList> {

    Page<WarnList> searchWarnLists(Page<WarnList> page, WarnListCriteria warnListCriteria);

    void saveWarnList(WarnList warnList);

    void saveWarnLists(List<WarnList> warnLists , String operationType);

    List<WarnList> getSpecifiedKeywordWarnLists(String keyword);

    WarnList getWarnList(long uuid);

    void deleteWarnList(long uuid);

    void deleteWarnLists(List<Integer> uuids);
}
