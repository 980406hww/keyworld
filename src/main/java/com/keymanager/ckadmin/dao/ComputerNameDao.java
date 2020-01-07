package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.ComputerName;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("computerNameDao")
public interface ComputerNameDao  extends BaseMapper<ComputerName> {
    ComputerName findComputerName(@Param("computerNamePrefix") String namePrefix);

    void addComputerName(@Param("computerNamePrefix") String namePrefix, @Param("sequence") int sequence);

    void updateComputerNameSequence(@Param("computerNamePrefix") String namePrefix, @Param("sequence") Integer sequence);
}
