package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component("customerKeywordDao2")
public interface
CustomerKeywordDao2 extends BaseMapper<com.keymanager.ckadmin.entity.CustomerKeyword> {

    List<Map> getCustomerKeywordsCount(@Param("customerUuids") List<Long> customerUuids,
            @Param("terminalType") String terminalType, @Param
            ("entryType") String entryType);

    void changeCustomerKeywordStatus(@Param("terminalType") String terminalType, @Param("entryType") String entryType,
            @Param("customerUuid") Long customerUuid, @Param("status") Integer status);

    void deleteCustomerKeywordsByCustomerUuid(long customerUuid);
}