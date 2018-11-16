package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.PositiveListCriteria;
import com.keymanager.monitoring.dao.PositiveListDao;
import com.keymanager.monitoring.entity.PositiveList;
import com.keymanager.monitoring.entity.PositiveListUpdateInfo;
import com.keymanager.monitoring.vo.PositiveListVO;
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

    public void savePositiveLists(List<PositiveListVO> positiveListVOS, String operationType, String btnType, String userName) {
        if (CollectionUtils.isNotEmpty(positiveListVOS)) {
            for (PositiveListVO positiveListVO : positiveListVOS) {
                PositiveListCriteria positiveListCriteria = new PositiveListCriteria();
                positiveListCriteria.setKeyword(positiveListVO.getPositiveList().getKeyword());
                positiveListCriteria.setTitle(positiveListVO.getPositiveList().getTitle());
                positiveListCriteria.setTerminalType(positiveListVO.getPositiveList().getTerminalType());
                String[] strings = positiveListVO.getPositiveList().getNewsSource().split(",");
                if (strings.length > 0){
                    if (strings.length > 1){
                        positiveListCriteria.setPosition(Integer.valueOf(strings[1]));
                    }
                    if (strings[0].equals("null")) {
                        positiveListVO.getPositiveList().setNewsSource(null);
                    } else {
                        positiveListVO.getPositiveList().setNewsSource(strings[0]);
                    }
                } else {
                    positiveListVO.getPositiveList().setNewsSource(null);
                }
                List<PositiveList> existingPositiveLists = positiveListDao.searchPositiveListsFullMatching(positiveListCriteria);
                if(operationType.equals("update")){
                    for (PositiveList existingPositiveList : existingPositiveLists) {
                        if (btnType.equals("1")) {
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
                            if (btnType.equals("1") || CollectionUtils.isNotEmpty(positiveListVO.getPositiveListUpdateInfoList())) {
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
        List<PositiveListVO> positiveListVOS = new ArrayList<PositiveListVO>();
        List<PositiveList> positiveLists = positiveListDao.getSpecifiedKeywordPositiveLists(keyword, terminalType);
        if (CollectionUtils.isNotEmpty(positiveLists)) {
            for (PositiveList positiveList : positiveLists) {
                PositiveListVO positiveListVO = new PositiveListVO();
                List<PositiveListUpdateInfo> positiveListUpdateInfoList = positiveListUpdateInfoService.findPositiveListUpdateInfos(positiveList.getUuid());
                positiveListVO.setPositiveList(positiveList);
                positiveListVO.setPositiveListUpdateInfoList(positiveListUpdateInfoList);
                positiveListVOS.add(positiveListVO);
            }
        }
        return positiveListVOS;
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
