package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Classification;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/12/4.
 */
public interface ClassificationRestDao extends BaseMapper<Classification> {
    List<String> getClassificationGroup();

    Classification getClassification();

    Classification getClassificationgroupNotAll(@Param("groupInfo") String group);

    void updatefCaptured(@Param("uuid")Integer uuid);
}
