package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.ProductkeywordCriteria;
import com.keymanager.ckadmin.entity.ProductKeyword;
import java.util.List;

/**
 * <p>
 * 新关键字表 服务类
 * </p>
 *
 * @author lhc
 * @since 2019-09-05
 */
public interface ProductKeywordService extends IService<ProductKeyword> {

    Page<ProductKeyword> searchProductKeywords(Page<ProductKeyword> page, ProductkeywordCriteria productkeywordCriteria);

    void deleteAll(List<Integer> uuids);

    void deleteOne(Long uuid);
}
