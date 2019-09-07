package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.dao.QZSettingDao;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.enums.TerminalTypeEnum;
import com.keymanager.ckadmin.service.QZSettingService;

import com.keymanager.ckadmin.vo.QZSearchEngineVO;
import com.keymanager.ckadmin.vo.QZSettingVO;
import com.keymanager.util.Constants;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
@Service("QZSettingService2")
public class QZSettingServiceImpl extends
    ServiceImpl<QZSettingDao, QZSetting> implements QZSettingService {

    @Resource(name = "QZSettingDao2")
    private QZSettingDao qzSettingDao;

    @Override
    public Page<QZSetting> searchQZSetting(Page<QZSetting> page, QZSettingCriteria qzSettingCriteria){
        page.setRecords(qzSettingDao.searchQZSettings(page, qzSettingCriteria));
        return page;
    }

    @Override
    public List<QZSearchEngineVO> searchQZSettingSearchEngineMap(QZSettingCriteria criteria, Integer record) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(Constants.ALL_SEARCH_ENGINE, TerminalTypeEnum.PC.name());
        map.put(Constants.ALL_SEARCH_ENGINE + TerminalTypeEnum.Phone.name(), TerminalTypeEnum.Phone.name());
        boolean displayExistTabFlag = (!"".equals(criteria.getDomain()) || !"".equals(criteria.getCustomerUuid())) && record == 1;
        if (displayExistTabFlag) {
            List<QZSettingVO> qzSettingVos = qzSettingDao.searchQZSettingSearchEngines(criteria.getCustomerUuid(), criteria.getDomain());
            for (QZSettingVO qzSettingVo : qzSettingVos) {
                if (null != qzSettingVo.getPcGroup()) {
                    map.put(qzSettingVo.getSearchEngine(), TerminalTypeEnum.PC.name());
                }
                if (null != qzSettingVo.getPhoneGroup()){
                    map.put(qzSettingVo.getSearchEngine() + TerminalTypeEnum.Phone.name(), TerminalTypeEnum.Phone.name());
                }
            }
        } else {
            map.putAll(Constants.SEARCH_ENGINE_MAP);
        }
        if (!map.containsKey(criteria.getSearchEngine()) && !map.containsKey(criteria.getSearchEngine() + criteria.getTerminalType())) {
            Map.Entry<String, String> next = map.entrySet().iterator().next();
            if ("".equals(criteria.getSearchEngine()) && TerminalTypeEnum.Phone.name().equals(criteria.getTerminalType())) {
                criteria.setSearchEngine(Constants.ALL_SEARCH_ENGINE);
                criteria.setTerminalType(TerminalTypeEnum.Phone.name());
            } else {
                criteria.setSearchEngine(next.getKey().substring(0, next.getKey().contains("P") ? next.getKey().indexOf('P') : next.getKey().length()));
                criteria.setTerminalType(next.getValue());
            }
        }
        List<QZSearchEngineVO> qzSearchEngineVOS= new LinkedList<>();
        for(Map.Entry<String, String> entry : map.entrySet()) {
            QZSearchEngineVO qzSearchEngineVO = new QZSearchEngineVO();
            qzSearchEngineVO.setSearchEngineName(entry.getKey());
            qzSearchEngineVO.setTerminalType(entry.getValue());
            qzSearchEngineVOS.add(qzSearchEngineVO);
        }
        return qzSearchEngineVOS;
    }

}
