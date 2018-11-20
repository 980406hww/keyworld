package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.PositiveListCriteria;
import com.keymanager.monitoring.dao.PositiveListDao;
import com.keymanager.monitoring.entity.PositiveList;
import com.keymanager.monitoring.entity.PositiveListUpdateInfo;
import com.keymanager.monitoring.vo.PositiveListVO;
import com.keymanager.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PositiveListService extends ServiceImpl<PositiveListDao, PositiveList> {

    private static Logger logger = LoggerFactory.getLogger(PositiveListService.class);

    @Autowired
    private PositiveListDao positiveListDao;

    @Autowired
    private PositiveListUpdateInfoService positiveListUpdateInfoService;

    public Page<PositiveList> searchPositiveLists(Page<PositiveList> page, PositiveListCriteria positiveListCriteria) {
        page.setRecords(positiveListDao.searchPositiveLists(page, positiveListCriteria));
        return page;
    }

    public void savePositiveList(PositiveList positiveList, String userName) {
        if (null != positiveList.getUuid()) {
            positiveList.setUpdateTime(new Date());
            updatePositiveList(positiveList);
        } else {
            positiveList.setCreateTime(new Date());
            positiveListDao.insert(positiveList);
            if (null != positiveList.getOptimizeMethod()) {
                PositiveListVO positiveListVO = new PositiveListVO();
                positiveListVO.setPositiveList(positiveList);
                positiveListUpdateInfoService.savePositiveListUpdateInfo(positiveListVO, userName);
            }
        }
    }

    public void savePositiveLists(List<PositiveListVO> positiveListVOs, String operationType, String btnType, String userName) {
        if (CollectionUtils.isNotEmpty(positiveListVOs)) {
            for (PositiveListVO positiveListVO : positiveListVOs) {
                PositiveListCriteria positiveListCriteria = new PositiveListCriteria();
                positiveListCriteria.setKeyword(positiveListVO.getPositiveList().getKeyword());
                positiveListCriteria.setTitle(positiveListVO.getPositiveList().getTitle());
                positiveListCriteria.setTerminalType(positiveListVO.getPositiveList().getTerminalType());
                positiveListCriteria.setPosition(positiveListVO.getPositiveList().getOriginalPosition());
                List<PositiveList> existingPositiveLists = positiveListDao.searchPositiveListsFullMatching(positiveListCriteria);
                if(operationType.equals("update")){
                    for (PositiveList existingPositiveList : existingPositiveLists) {
                        if (btnType.equals(Constants.POSITIVELIST_OPERATION_TYPE)) {
                            existingPositiveList.setOptimizeMethod(positiveListVO.getPositiveList().getOptimizeMethod());
                            existingPositiveList.setNewsSource(positiveListVO.getPositiveList().getNewsSource());
                            existingPositiveList.setUpdateTime(new Date());
                            updatePositiveList(existingPositiveList);
                            positiveListVO.setPositiveList(existingPositiveList);
                            positiveListUpdateInfoService.savePositiveListUpdateInfo(positiveListVO, userName);
                        } else {
                            deletePositiveList(existingPositiveList.getUuid());
                        }
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(existingPositiveLists)) {
                        if (null != positiveListCriteria.getPosition()) {
                            PositiveList existingPositiveList = existingPositiveLists.get(0);
                            positiveListVO.getPositiveList().setUuid(existingPositiveList.getUuid());
                            positiveListVO.getPositiveList().setCreateTime(existingPositiveList.getCreateTime());
                            if (btnType.equals(Constants.POSITIVELIST_OPERATION_TYPE) || positiveListVO.isHasUpdateInfo()) {
                                positiveListUpdateInfoService.savePositiveListUpdateInfo(positiveListVO, userName);
                            }
                        }
                    }
                    this.savePositiveList(positiveListVO.getPositiveList(), userName);
                }
            }
        }
    }

    public List<PositiveListVO> getSpecifiedKeywordPositiveLists(String keyword, String terminalType) {
        List<PositiveListVO> positiveListVOs = new ArrayList<PositiveListVO>();
        List<PositiveList> positiveLists = positiveListDao.getSpecifiedKeywordPositiveLists(keyword, terminalType);
        if (CollectionUtils.isNotEmpty(positiveLists)) {
            for (PositiveList positiveList : positiveLists) {
                PositiveListVO positiveListVO = new PositiveListVO();
                List<PositiveListUpdateInfo> positiveListUpdateInfoList = positiveListUpdateInfoService.findPositiveListUpdateInfos(positiveList.getUuid());
                positiveListVO.setPositiveList(positiveList);
                positiveListVO.setPositiveListUpdateInfoList(positiveListUpdateInfoList);
                positiveListVOs.add(positiveListVO);
            }
        }
        return positiveListVOs;
    }

    public PositiveList getPositiveList(long uuid) {
        return positiveListDao.selectById(uuid);
    }

    public void deletePositiveList(long uuid) {
        positiveListDao.deleteById(uuid);
        positiveListUpdateInfoService.deleteByPid(uuid);
    }

    public void updatePositiveList(PositiveList positiveList){
        positiveListDao.updateById(positiveList);
    }

    public void deletePositiveLists(List<String> uuids) {
        for (String uuid : uuids) {
            deletePositiveList(Long.valueOf(uuid));
        }
    }

}
