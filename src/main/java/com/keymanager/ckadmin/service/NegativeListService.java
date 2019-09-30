package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.NegativeListCriteria;
import com.keymanager.ckadmin.entity.NegativeList;
import java.util.List;

public interface NegativeListService extends IService<NegativeList> {

    Page<NegativeList> searchNegativeLists(Page<NegativeList> page, NegativeListCriteria negativeListCriteria);

    void saveNegativeList(NegativeList negativeList);

    void saveNegativeLists(List<NegativeList> negativeLists , String operationType);

    List<NegativeList> getSpecifiedKeywordNegativeLists(String keyword);

    NegativeList getNegativeList(long uuid);

    void deleteNegativeList(long uuid , NegativeList negativeList);

    void deleteAll(List<Integer> uuids);

    List<NegativeList> negativeListsSynchronizeOfDelete(NegativeList negativeList);
}
