package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.ProductkeywordCriteria;
import com.keymanager.ckadmin.dao.ProductKeywordDao;
import com.keymanager.ckadmin.entity.ProductKeyword;
import com.keymanager.ckadmin.service.ProductKeywordService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 新关键字表 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-09-05
 */
@Service("productKeywordService2")
public class ProductKeywordServiceImpl extends
    ServiceImpl<ProductKeywordDao, ProductKeyword> implements ProductKeywordService {

    @Resource(name = "productKeywordDao2")
    private ProductKeywordDao productKeywordDao;

    @Override
    public Page<ProductKeyword> searchProductKeywords(Page<ProductKeyword> page,
        ProductkeywordCriteria productkeywordCriteria) {
        List<ProductKeyword> productKeywords = productKeywordDao
            .searchProductKeywords(page, productkeywordCriteria);
        page.setRecords(productKeywords);
        return page;
    }

    @Override
    public void deleteAll(List<Integer> uuids) {
        productKeywordDao.deleteProductKeywordsByUuids(uuids);
    }

    @Override
    public void deleteOne(Long uuid) {
        this.deleteById(uuid);
    }
}
