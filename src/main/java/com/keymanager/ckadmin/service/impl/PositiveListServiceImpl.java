package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.keymanager.ckadmin.criteria.PositiveListCriteria;
import com.keymanager.ckadmin.dao.PositiveListDao;
import com.keymanager.ckadmin.entity.PositiveList;
import com.keymanager.ckadmin.entity.PositiveListUpdateInfo;
import com.keymanager.ckadmin.service.PositiveListService;
import com.keymanager.ckadmin.service.PositiveListUpdateInfoService;
import com.keymanager.ckadmin.vo.PositiveListVO;
import com.keymanager.util.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "positiveListService2")
public class PositiveListServiceImpl extends ServiceImpl<PositiveListDao, PositiveList> implements PositiveListService {

    @Resource(name = "positiveListDao2")
    private PositiveListDao positiveListDao;

    @Resource(name = "positiveListUpdateInfoService2")
    private PositiveListUpdateInfoService positiveListUpdateInfoService;

    @Override
    public Page<PositiveList> searchPositiveLists(Page<PositiveList> page, PositiveListCriteria positiveListCriteria) {
        page.setRecords(positiveListDao.searchPositiveLists(page, positiveListCriteria));
        return page;
    }

    @Override
    public void savePositiveList(PositiveList positiveList, String userName) {
        if (null != positiveList.getUuid()) {
            positiveList.setUpdateTime(new Date());
            updatePositiveList(positiveList);
        } else {
            positiveList.setCreateTime(new Date());
            positiveListDao.insert(positiveList);
            if (null != positiveList.getOptimizeMethod()) {
                PositiveListVO positiveListVo = new PositiveListVO();
                positiveListVo.setPositiveList(positiveList);
                positiveListUpdateInfoService.savePositiveListUpdateInfo(positiveListVo, userName);
            }
        }
    }

    @Override
    public void savePositiveLists(List<PositiveListVO> positiveListVos, String operationType, String btnType, String userName) {
        if (CollectionUtils.isNotEmpty(positiveListVos)) {
            for (PositiveListVO positiveListVo : positiveListVos) {
                PositiveListCriteria positiveListCriteria = new PositiveListCriteria();
                positiveListCriteria.setKeyword(positiveListVo.getPositiveList().getKeyword());
                positiveListCriteria.setTitle(positiveListVo.getPositiveList().getTitle());
                positiveListCriteria.setTerminalType(positiveListVo.getPositiveList().getTerminalType());
                positiveListCriteria.setPosition(positiveListVo.getPositiveList().getOriginalPosition());
                List<PositiveList> existingPositiveLists = positiveListDao.searchPositiveListsFullMatching(positiveListCriteria);
                if("update".equals(operationType)){
                    for (PositiveList existingPositiveList : existingPositiveLists) {
                        if (btnType.equals(Constants.POSITIVELIST_OPERATION_TYPE)) {
                            existingPositiveList.setOptimizeMethod(positiveListVo.getPositiveList().getOptimizeMethod());
                            existingPositiveList.setNewsSource(positiveListVo.getPositiveList().getNewsSource());
                            existingPositiveList.setUpdateTime(new Date());
                            updatePositiveList(existingPositiveList);
                            positiveListVo.setPositiveList(existingPositiveList);
                            positiveListUpdateInfoService.savePositiveListUpdateInfo(positiveListVo, userName);
                        } else {
                            deletePositiveList(existingPositiveList.getUuid());
                        }
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(existingPositiveLists)) {
                        if (null != positiveListCriteria.getPosition()) {
                            PositiveList existingPositiveList = existingPositiveLists.get(0);
                            positiveListVo.getPositiveList().setUuid(existingPositiveList.getUuid());
                            positiveListVo.getPositiveList().setCreateTime(existingPositiveList.getCreateTime());
                            if (btnType.equals(Constants.POSITIVELIST_OPERATION_TYPE) || positiveListVo.isHasUpdateInfo()) {
                                positiveListUpdateInfoService.savePositiveListUpdateInfo(positiveListVo, userName);
                            }
                        }
                    }
                    this.savePositiveList(positiveListVo.getPositiveList(), userName);
                }
            }
        }
    }

    @Override
    public List<PositiveListVO> getSpecifiedKeywordPositiveLists(String keyword, String terminalType) {
        List<PositiveListVO> positiveListVos = new ArrayList<>();
        List<PositiveList> positiveLists = positiveListDao.getSpecifiedKeywordPositiveLists(keyword, terminalType);
        if (CollectionUtils.isNotEmpty(positiveLists)) {
            for (PositiveList positiveList : positiveLists) {
                PositiveListVO positiveListVo = new PositiveListVO();
                List<PositiveListUpdateInfo> positiveListUpdateInfoList = positiveListUpdateInfoService.findPositiveListUpdateInfos(positiveList.getUuid());
                positiveListVo.setPositiveList(positiveList);
                positiveListVo.setPositiveListUpdateInfoList(positiveListUpdateInfoList);
                positiveListVos.add(positiveListVo);
            }
        }
        return positiveListVos;
    }

    @Override
    public PositiveList getPositiveList(long uuid) {
        return positiveListDao.selectById(uuid);
    }

    @Override
    public void deletePositiveList(long uuid) {
        positiveListDao.deleteById(uuid);
        positiveListUpdateInfoService.deleteByPid(uuid);
    }

    @Override
    public void updatePositiveList(PositiveList positiveList){
        positiveListDao.updateById(positiveList);
    }

    @Override
    public void deletePositiveLists(List<Integer> uuids) {
        for (Integer uuid : uuids) {
            deletePositiveList(Long.valueOf(uuid));
        }
    }

}
