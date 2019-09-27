package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.PositiveListCriteria;
import com.keymanager.ckadmin.entity.PositiveList;
import com.keymanager.ckadmin.vo.PositiveListVO;
import java.util.List;

public interface PositiveListService extends IService<PositiveList> {
    Page<PositiveList> searchPositiveLists(Page<PositiveList> page, PositiveListCriteria positiveListCriteria);

    void savePositiveList(PositiveList positiveList, String userName);

    void savePositiveLists(List<PositiveListVO> positiveListVOs, String operationType, String btnType, String userName);

    List<PositiveListVO> getSpecifiedKeywordPositiveLists(String keyword, String terminalType);

    PositiveList getPositiveList(long uuid);

    void deletePositiveList(long uuid);

    void updatePositiveList(PositiveList positiveList);

    void deletePositiveLists(List<Integer> uuids);
}
