package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.NegativeListCriteria;
import com.keymanager.ckadmin.entity.NegativeList;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("negativeListDao2")
public interface NegativeListDao extends BaseMapper<NegativeList> {

    List<NegativeList> searchNegativeLists(Page<NegativeList> page, @Param("negativeListCriteria") NegativeListCriteria negativeListCriteria);

    List<NegativeList> searchNegativeListsFullMatching(@Param("negativeListCriteria") NegativeListCriteria negativeListCriteria);

    List<NegativeList> getSpecifiedKeywordNegativeLists(@Param("keyword") String keyword);

    int selectLastId();

    List<NegativeList> negativeListsSynchronizeOfDelete(@Param("negativeList") NegativeList negativeList);

}
