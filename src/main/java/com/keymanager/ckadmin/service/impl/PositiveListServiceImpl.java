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
    private static Logger logger = LoggerFactory.getLogger(PositiveListServiceImpl.class);

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
                PositiveListVO positiveListVO = new PositiveListVO();
                positiveListVO.setPositiveList(positiveList);
                positiveListUpdateInfoService.savePositiveListUpdateInfo(positiveListVO, userName);
            }
        }
    }

    @Override
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

    @Override
    public List<PositiveListVO> getSpecifiedKeywordPositiveLists(String keyword, String terminalType) {
        List<PositiveListVO> positiveListVOs = new ArrayList<>();
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
