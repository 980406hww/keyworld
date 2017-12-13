package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Classification;
import com.keymanager.monitoring.entity.ClassificationWebsitInfo;
import com.keymanager.monitoring.vo.ClassificationWebSiteInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/12/4.
 */
public interface ClassificationWebsiteInfoRestDao extends BaseMapper<ClassificationWebsitInfo> {
    ClassificationWebsitInfo getfetchKeywordClassificationEmail(@Param("classificationUuid") int classificationUuid);
}
