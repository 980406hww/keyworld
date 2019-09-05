package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.ProductkeywordCriteria;
import com.keymanager.ckadmin.entity.ProductKeyword;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 新关键字表 Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-09-05
 */
@Repository("productKeywordDao2")
public interface ProductKeywordDao extends BaseMapper<ProductKeyword> {

    List<ProductKeyword> searchProductKeywords(Page<ProductKeyword> page, @Param("productkeywordCriteria") ProductkeywordCriteria productkeywordCriteria);
}
