package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.dao.CustomerKeywordDao;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.enums.CustomerKeywordSourceEnum;
import com.keymanager.ckadmin.enums.EntryTypeEnum;
import com.keymanager.ckadmin.enums.KeywordEffectEnum;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import com.keymanager.ckadmin.enums.CustomerKeywordSourceEnum;
import com.keymanager.ckadmin.enums.EntryTypeEnum;
import com.keymanager.ckadmin.vo.CustomerKeywordSummaryInfoVO;
import com.keymanager.ckadmin.vo.KeywordCountVO;
import com.keymanager.ckadmin.vo.OptimizationKeywordVO;
import com.keymanager.ckadmin.vo.MachineGroupQueueVO;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("customerKeywordService2")
public class CustomerKeywordServiceImpl extends ServiceImpl<CustomerKeywordDao, CustomerKeyword> implements
    CustomerKeywordService {

    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordServiceImpl.class);

    @Resource(name = "customerKeywordDao2")
    private CustomerKeywordDao customerKeywordDao;

    @Resource(name = "userRoleService2")
    private UserRoleService userRoleService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    private final static Map<String, LinkedBlockingQueue> machineGroupQueueMap = new HashMap<>();

    @Override
    public void cacheCustomerKeywords() {
        List<String> machineGroups = customerKeywordDao.getMachineGroups();
        if (CollectionUtils.isNotEmpty(machineGroups)) {
            for (String machineGroup : machineGroups) {
                String[] terminalTypeAndMachineGroups = machineGroup.split("####");
                LinkedBlockingQueue blockingQueue = machineGroupQueueMap.get(terminalTypeAndMachineGroups[0] + "####" + terminalTypeAndMachineGroups[1]);
                if (blockingQueue == null) {
                    blockingQueue = new LinkedBlockingQueue<String>(100000);
                    machineGroupQueueMap.put(terminalTypeAndMachineGroups[0] + "####" + terminalTypeAndMachineGroups[1], blockingQueue);
                }
                int machineCount = Integer.parseInt(terminalTypeAndMachineGroups[2]);
                int currentSize = blockingQueue.size();
                int offerCount = 0;
                if (currentSize < (machineCount * 4)) {
                    List<OptimizationKeywordVO> optimizationKeywordVOS = null;
                    do {
                        optimizationKeywordVOS = customerKeywordDao.fetchCustomerKeywordsForCache(terminalTypeAndMachineGroups[0], terminalTypeAndMachineGroups[1], ((machineCount * 10) > 5000 ? 5000 : (machineCount * 10)));
                        if (CollectionUtils.isNotEmpty(optimizationKeywordVOS)) {
                            List<Long> customerKeywordUuids = new ArrayList<>();
                            if (optimizationKeywordVOS.size() > machineCount) {
                                for (OptimizationKeywordVO optimizationKeywordVO : optimizationKeywordVOS) {
                                    if (blockingQueue.offer(optimizationKeywordVO)) {
                                        offerCount++;
                                        customerKeywordUuids.add(optimizationKeywordVO.getUuid());
                                    } else {
                                        break;
                                    }
                                }
                                updateOptimizationQueryTime(customerKeywordUuids);
                            } else {
                                int count = 0;
                                int repeatTimes = 0;
                                boolean hasNewElement = false;
                                Map<Long, Integer> customerKeywordUuidAndRepeatCount = new HashMap<>();
                                do {
                                    hasNewElement = false;
                                    repeatTimes++;
                                    Iterator<OptimizationKeywordVO> it = optimizationKeywordVOS.iterator();
                                    while (it.hasNext()) {
                                        OptimizationKeywordVO optimizationKeywordVO = it.next();
                                        if (optimizationKeywordVO.getQueryInterval() == 0) {
                                            optimizationKeywordVO.setQueryInterval(5000);
                                        }
                                        int maxOptimizeCount = Math.round(DateUtils.getFragmentInSeconds(Calendar.getInstance(), Calendar.DATE) / (optimizationKeywordVO.getQueryInterval()));
                                        Integer repeatCount = customerKeywordUuidAndRepeatCount.get(optimizationKeywordVO.getUuid());
                                        if (repeatCount == null) {
                                            customerKeywordUuidAndRepeatCount.put(optimizationKeywordVO.getUuid(), 0);
                                        }
                                        if (maxOptimizeCount - optimizationKeywordVO.getOptimizedCount() > 0) {
                                            offerCount++;
                                            blockingQueue.offer(optimizationKeywordVO);
                                            optimizationKeywordVO.setOptimizedCount(optimizationKeywordVO.getOptimizedCount() + 1);
                                            hasNewElement = true;
                                            customerKeywordUuidAndRepeatCount.put(optimizationKeywordVO.getUuid(), (customerKeywordUuidAndRepeatCount.get(optimizationKeywordVO.getUuid()) + 1));
                                            count++;
                                            if (count > (machineCount * 4)) {
                                                break;
                                            }
                                        } else {
                                            it.remove();
                                        }
                                    }
                                }
                                while (count < (machineCount * 4) && hasNewElement && repeatTimes < 50 && CollectionUtils.isNotEmpty(optimizationKeywordVOS));

                                if (customerKeywordUuidAndRepeatCount.size() == 0) {
                                    break;
                                }
                                while (customerKeywordUuidAndRepeatCount.size() > 0) {
                                    customerKeywordUuids.addAll(customerKeywordUuidAndRepeatCount.keySet());
                                    updateOptimizationQueryTime(customerKeywordUuids);
                                    customerKeywordUuids.clear();
                                    for (Long customerKeywordUuid : customerKeywordUuidAndRepeatCount.keySet()) {
                                        if (customerKeywordUuidAndRepeatCount.get(customerKeywordUuid) < 2) {
                                            customerKeywordUuids.add(customerKeywordUuid);
                                        } else {
                                            customerKeywordUuidAndRepeatCount.put(customerKeywordUuid, (customerKeywordUuidAndRepeatCount.get(customerKeywordUuid) - 1));
                                        }
                                    }
                                    for (Long customerKeywordUuid : customerKeywordUuids) {
                                        customerKeywordUuidAndRepeatCount.remove(customerKeywordUuid);
                                    }
                                    customerKeywordUuids.clear();
                                }
                                if (!CollectionUtils.isNotEmpty(optimizationKeywordVOS)) {
                                    break;
                                }
                            }
                        }
                    }
                    while (currentSize + offerCount < (machineCount * 10) && CollectionUtils.isNotEmpty(optimizationKeywordVOS));
                }
            }
        }
    }

    @Override
    public void updateOptimizationQueryTime(List<Long> customerKeywordUuids) {
        customerKeywordDao.updateOptimizationQueryTime(customerKeywordUuids);
    }

    @Override
    public List<MachineGroupQueueVO> getMachineGroupAndSize() {
        List<MachineGroupQueueVO> machineGroupQueueVOS = new ArrayList<>();
        for (Map.Entry<String, LinkedBlockingQueue> entry : machineGroupQueueMap.entrySet()) {
            machineGroupQueueVOS.add(new MachineGroupQueueVO(entry.getKey(), entry.getValue().size()));
        }
        return machineGroupQueueVOS;
    }

    @Override
    public List<Map> getCustomerKeywordsCount(List<Long> customerUuids, String terminalType, String entryType) {
        return customerKeywordDao.getCustomerKeywordsCount(customerUuids, terminalType, entryType);
    }

    @Override
    public void changeCustomerKeywordStatus(String terminalType, String entryType, Long customerUuid, Integer status) {
        customerKeywordDao.changeCustomerKeywordStatus(terminalType, entryType, customerUuid, status);
    }

    @Override
    public void deleteCustomerKeywords(long customerUuid) {
        logger.info("deleteCustomerKeywords:" + customerUuid);
        customerKeywordDao.deleteCustomerKeywordsByCustomerUuid(customerUuid);
    }

    @Override
    public int getCustomerKeywordCount(String terminalType, String entryType, long customerUuid) {
        return customerKeywordDao.getCustomerKeywordCount(terminalType, entryType, customerUuid);
    }

    @Override
    public void excludeCustomerKeyword(
        QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        customerKeywordDao.excludeCustomerKeyword(qzSettingExcludeCustomerKeywordsCriteria);
    }

    @Override
    public void addCustomerKeyword(List<CustomerKeyword> customerKeywords, String userName) {
        List<CustomerKeyword> addCustomerKeywords = new ArrayList<>();
        for (CustomerKeyword customerKeyword : customerKeywords) {
            CustomerKeyword tmpCustomerKeyword = checkCustomerKeyword(customerKeyword, userName);
            if (tmpCustomerKeyword != null) {
                addCustomerKeywords.add(customerKeyword);
            }
        }
        if (CollectionUtils.isNotEmpty(addCustomerKeywords)) {
            int fromIndex = 0, toIndex = 1000;
            do {
                customerKeywordDao.addCustomerKeywords(new ArrayList<>(addCustomerKeywords.subList(fromIndex, toIndex >
                    addCustomerKeywords.size() ? addCustomerKeywords.size() : toIndex)));
                fromIndex += 1000;
                toIndex += 1000;
            } while (addCustomerKeywords.size() > fromIndex);
        }
    }

    private CustomerKeyword checkCustomerKeyword(CustomerKeyword customerKeyword, String userName) {
        if (StringUtil.isNullOrEmpty(customerKeyword.getOriginalUrl())) {
            customerKeyword.setOriginalUrl(customerKeyword.getUrl());
        }
/*
        String originalUrl = customerKeyword.getOriginalUrl();
        if (!StringUtil.isNullOrEmpty(originalUrl)) {
            if (originalUrl.indexOf("www.") == 0) {
                originalUrl = originalUrl.substring(4);
            } else if (originalUrl.indexOf("m.") == 0) {
                originalUrl = originalUrl.substring(2);
            }
        } else {
            originalUrl = null;
        }
*/
        customerKeyword.setKeyword(customerKeyword.getKeyword().trim());

        if (!EntryTypeEnum.fm.name().equals(customerKeyword.getType())) {
            CustomerKeyword customerKeyword1 = customerKeywordDao
                .getOneSameCustomerKeyword(customerKeyword.getTerminalType(), customerKeyword.getCustomerUuid(), customerKeyword.getKeyword(),
                    customerKeyword.getUrl(), customerKeyword.getTitle());
            if (customerKeyword1 != null) {
                detectCustomerKeywordEffect(customerKeyword, customerKeyword1);
                return null;
            }
        }

        if (!EntryTypeEnum.fm.name().equals(customerKeyword.getType())) {
            CustomerKeyword customerKeyword1 = customerKeywordDao
                .getOneSimilarCustomerKeyword(customerKeyword.getTerminalType(), customerKeyword.getCustomerUuid(), customerKeyword.getKeyword(),
                    customerKeyword.getOriginalUrl(), customerKeyword.getTitle());
            if (customerKeyword1 != null) {
                detectCustomerKeywordEffect(customerKeyword, customerKeyword1);
                return null;
            }
        }

        customerKeyword.setKeyword(customerKeyword.getKeyword().trim());
        customerKeyword.setUrl(customerKeyword.getUrl() != null ? customerKeyword.getUrl().trim() : null);
        customerKeyword.setTitle(customerKeyword.getTitle() != null ? customerKeyword.getTitle().trim() : null);
        customerKeyword.setOriginalUrl(customerKeyword.getOriginalUrl() != null ? customerKeyword.getOriginalUrl().trim() : null);
        customerKeyword.setOrderNumber(customerKeyword.getOrderNumber() != null ? customerKeyword.getOrderNumber().trim() : null);
        customerKeyword.setOptimizeRemainingCount(customerKeyword.getOptimizePlanCount() != null ? customerKeyword.getOptimizePlanCount() : 0);
        if (customerKeyword.getStatus() == null) {
            if (StringUtils.isNotBlank(userName)) {
                boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
                if (isDepartmentManager) {
                    customerKeyword.setStatus(1);
                } else {
                    customerKeyword.setStatus(2);
                }
            } else {
                customerKeyword.setStatus(1);
            }
        }

        if ("否".equals(customerKeyword.getRunImmediate())) {
            customerKeyword.setStatus(2);
        }

        if (customerKeyword.getCurrentPosition() == null) {
            customerKeyword.setCurrentPosition(100);
        }

        if (EntryTypeEnum.qz.name().equals(customerKeyword.getType())) {
            customerKeyword.setCurrentIndexCount(-1);
        }

        if (Utils.isNullOrEmpty(customerKeyword.getMachineGroup())) {
            customerKeyword.setMachineGroup(customerKeyword.getType());
        }
        int queryInterval = 24 * 60 * 60;
        if (null != customerKeyword.getOptimizePlanCount() && customerKeyword.getOptimizePlanCount() > 0) {
            int optimizeTodayCount = (int) Math.floor(Utils
                .getRoundValue(customerKeyword.getOptimizePlanCount() * (Math.random() * 0.7 + 0.5),
                    1));
            queryInterval = queryInterval / optimizeTodayCount;
            customerKeyword.setOptimizeTodayCount(optimizeTodayCount);
        }
        customerKeyword.setQueryInterval(queryInterval);
        customerKeyword.setAutoUpdateNegativeDateTime(Utils.getCurrentTimestamp());
        customerKeyword.setCapturePositionQueryTime(Utils.addDay(Utils.getCurrentTimestamp(), -2));
        customerKeyword.setStartOptimizedTime(new Date());
        customerKeyword.setLastReachStandardDate(Utils.yesterday());
        customerKeyword.setQueryTime(new Date());
        customerKeyword.setQueryDate(new Date());
        customerKeyword.setUpdateTime(new Date());
        customerKeyword.setCreateTime(new Date());
        return customerKeyword;
    }

    @Override
    public Integer getCustomerKeywordCountByOptimizeGroupName(String groupName) {
        return customerKeywordDao.getCustomerKeywordCountByOptimizeGroupName(groupName);
    }

    @Override
    public List<CustomerKeywordSummaryInfoVO> searchCustomerKeywordSummaryInfo(String entryType,
        long customerUuid) {
        return customerKeywordDao.searchCustomerKeywordSummaryInfo(entryType, customerUuid);
    }

    private void detectCustomerKeywordEffect(CustomerKeyword customerKeyword, CustomerKeyword customerKeyword1) {
        if (!CustomerKeywordSourceEnum.Capture.name().equals(customerKeyword.getCustomerKeywordSource())) {
            String oldKeywordEffect = customerKeyword1.getKeywordEffect();
            String keywordEffect = customerKeyword.getKeywordEffect();
            if (null != oldKeywordEffect) {
                if (!oldKeywordEffect.equals(keywordEffect)) {
                    Map<String, Integer> levelMap = KeywordEffectEnum.toLevelMap();
                    String newKeywordEffect = levelMap.get(oldKeywordEffect) < levelMap.get(keywordEffect) ? oldKeywordEffect : keywordEffect;
                    customerKeyword.setKeywordEffect(newKeywordEffect);
                    customerKeyword.setUpdateTime(new Date());
                    customerKeywordDao.updateSameCustomerKeyword(customerKeyword);
                }
            } else {
                customerKeyword.setKeywordEffect(keywordEffect);
                customerKeyword.setUpdateTime(new Date());
                customerKeywordDao.updateSameCustomerKeyword(customerKeyword);
            }
        }
    }

    @Override
    public KeywordCountVO getCustomerKeywordsCountByCustomerUuid(Long customerUuid, String terminalType) {
        return customerKeywordDao.getCustomerKeywordsCountByCustomerUuid(customerUuid, terminalType);
    }

    @Override
    public void resetInvalidRefreshCount(RefreshStatisticsCriteria criteria) {
        customerKeywordDao.resetInvalidRefreshCount(criteria);
    }
}


