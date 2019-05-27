package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.dao.CustomerKeywordDao;
import com.keymanager.monitoring.dao.CustomerKeywordRefreshStatInfoDao;
import com.keymanager.monitoring.dao.CustomerKeywordTerminalRefreshStatRecordDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.CustomerKeywordTerminalRefreshStatRecord;
import com.keymanager.monitoring.vo.PositionVO;
import com.keymanager.util.Constants;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Created by shunshikj08 on 2017/9/12.
 */
@Service
public class CustomerKeywordRefreshStatInfoService extends ServiceImpl<CustomerKeywordRefreshStatInfoDao, CustomerKeywordTerminalRefreshStatRecord> {

    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordRefreshStatInfoService.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    private MachineInfoService machineInfoService;

    @Autowired
    private CustomerKeywordDao customerKeywordDao;

    @Autowired
    private CustomerKeywordRefreshStatInfoDao customerKeywordRefreshStatInfoDao;

    @Autowired
    private CustomerKeywordTerminalRefreshStatRecordDao customerKeywordTerminalRefreshStatRecordDao;

    public List<CustomerKeywordTerminalRefreshStatRecord> generateCustomerKeywordStatInfo(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        List<CustomerKeywordTerminalRefreshStatRecord> customerKeywordTerminalRefreshStatRecords = getCustomerKeywordStatInfoRecords(customerKeywordRefreshStatInfoCriteria);
        Map<String, CustomerKeywordTerminalRefreshStatRecord> customerKeywordRefreshStatInfoRecordMap = new HashMap<String, CustomerKeywordTerminalRefreshStatRecord>();
        for (CustomerKeywordTerminalRefreshStatRecord customerKeywordTerminalRefreshStatRecord : customerKeywordTerminalRefreshStatRecords) {
            customerKeywordRefreshStatInfoRecordMap.put(customerKeywordTerminalRefreshStatRecord.getGroup(), customerKeywordTerminalRefreshStatRecord);
        }

        List<CustomerKeywordTerminalRefreshStatRecord> csCustomerKeywordTerminalRefreshStatRecords = machineInfoService.searchMachineInfoForRefreshStat(customerKeywordRefreshStatInfoCriteria);
        for (CustomerKeywordTerminalRefreshStatRecord csCustomerKeywordTerminalRefreshStatRecord : csCustomerKeywordTerminalRefreshStatRecords) {
            CustomerKeywordTerminalRefreshStatRecord customerKeywordTerminalRefreshStatRecord = customerKeywordRefreshStatInfoRecordMap.get(csCustomerKeywordTerminalRefreshStatRecord.getGroup());
            if (null != customerKeywordTerminalRefreshStatRecord) {
                customerKeywordTerminalRefreshStatRecord.setIdleTotalMinutes(csCustomerKeywordTerminalRefreshStatRecord.getIdleTotalMinutes());
                customerKeywordTerminalRefreshStatRecord.setTotalMachineCount(csCustomerKeywordTerminalRefreshStatRecord.getTotalMachineCount());
                customerKeywordTerminalRefreshStatRecord.setUnworkMachineCount(csCustomerKeywordTerminalRefreshStatRecord.getUnworkMachineCount());
            }
        }
        this.SetCountCustomerKeywordRefreshStatInfo(customerKeywordTerminalRefreshStatRecords);
        return customerKeywordTerminalRefreshStatRecords;
    }

    public void SetCountCustomerKeywordRefreshStatInfo(List<CustomerKeywordTerminalRefreshStatRecord> customerKeywordTerminalRefreshStatRecords){
        CustomerKeywordTerminalRefreshStatRecord total = new CustomerKeywordTerminalRefreshStatRecord();
        total.setGroup("总计");
        for (CustomerKeywordTerminalRefreshStatRecord customerKeywordTerminalRefreshStatRecord : customerKeywordTerminalRefreshStatRecords) {
            total.setInvalidKeywordCount(total.getInvalidKeywordCount() + customerKeywordTerminalRefreshStatRecord.getInvalidKeywordCount());
            total.setNeedOptimizeCount(total.getNeedOptimizeCount() + customerKeywordTerminalRefreshStatRecord.getNeedOptimizeCount());
            total.setNeedOptimizeKeywordCount(total.getNeedOptimizeKeywordCount() + customerKeywordTerminalRefreshStatRecord.getNeedOptimizeKeywordCount());
            total.setQueryCount(total.getQueryCount() + customerKeywordTerminalRefreshStatRecord.getQueryCount());
            total.setTotalKeywordCount(total.getTotalKeywordCount() + customerKeywordTerminalRefreshStatRecord.getTotalKeywordCount());
            total.setTotalMachineCount(total.getTotalMachineCount() + customerKeywordTerminalRefreshStatRecord.getTotalMachineCount());
            total.setTotalOptimizeCount(total.getTotalOptimizeCount() + customerKeywordTerminalRefreshStatRecord.getTotalOptimizeCount());
            total.setTotalOptimizedCount(total.getTotalOptimizedCount() + customerKeywordTerminalRefreshStatRecord.getTotalOptimizedCount());
            total.setUnworkMachineCount(total.getUnworkMachineCount() + customerKeywordTerminalRefreshStatRecord.getUnworkMachineCount());
            total.setZeroOptimizedCount(total.getZeroOptimizedCount() + customerKeywordTerminalRefreshStatRecord.getZeroOptimizedCount());
            total.setReachStandardKeywordCount(total.getReachStandardKeywordCount() + customerKeywordTerminalRefreshStatRecord.getReachStandardKeywordCount());
            total.setTodaySubTotal(total.getTodaySubTotal() + customerKeywordTerminalRefreshStatRecord.getTodaySubTotal());
            total.setMaxInvalidCount(customerKeywordTerminalRefreshStatRecord.getMaxInvalidCount());
            total.setIdleTotalMinutes(total.getIdleTotalMinutes() + customerKeywordTerminalRefreshStatRecord.getIdleTotalMinutes());
        }
        customerKeywordTerminalRefreshStatRecords.add(0, total);
    }

    private List<CustomerKeywordTerminalRefreshStatRecord> getCustomerKeywordStatInfoRecords(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        List<CustomerKeywordTerminalRefreshStatRecord> customerKeywordTerminalRefreshStatRecords = customerKeywordRefreshStatInfoDao.searchCustomerKeywordStatInfos(customerKeywordRefreshStatInfoCriteria);
        return customerKeywordTerminalRefreshStatRecords;
    }

    public List<String> searchKeywordUrlByGroup(String terminalType, String entryType, List<String> groups) throws Exception {
        List<String> keywordUrls = new ArrayList<String>();
        for (String group : groups) {
            List<String> keywordUrlList = customerKeywordDao.searchKeywordUrlByGroup(terminalType, entryType, group);
            if(CollectionUtils.isNotEmpty(keywordUrlList)) {
                keywordUrls.addAll(keywordUrlList);
            }
        }
        return keywordUrls;
    }

    public void downloadTxtFile(List<String> keywordUrls) throws Exception {
        FileOutputStream o = null;
        String path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "keywordUrl.txt";
        o = new FileOutputStream(path);
        for (String keywordUrl : keywordUrls) {
            o.write(keywordUrl.getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        }
        o.close();

        InputStream input = new FileInputStream(new File(path));
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(Utils.getWebRootPath() + "keywordUrl.txt")));
        int temp = 0;
        while ((temp = input.read()) != -1) {
            outputStream.write(temp);
        }
        input.close();
        outputStream.close();
    }

    public void uploadCSVFile(String terminalType, String entryType, String searchEngine, int reachStandardPosition, File targetFile) {
        FileUtil.readTxtFile(targetFile,"UTF-8");
        List<String> contents = FileUtil.readTxtFile(targetFile,"GBK");
        List<PositionVO> newContents = new ArrayList<PositionVO>();
        if(contents.size() > 0) {
            contents.remove(0);
            for (String content : contents) {
                PositionVO positionVO = new PositionVO();
                String[] positionInfo = content.split(",");
                if(positionInfo[2].contains("名外") || positionInfo[2].contains("--")) {
                    positionInfo[2] = "0";
                }
                positionVO.setUrl(positionInfo[0].replaceAll("\"",""));
                positionVO.setKeyword(positionInfo[1].replaceAll("\"",""));
                positionVO.setPosition(Integer.parseInt(positionInfo[2]));
                newContents.add(positionVO);
            }
        }
        while(newContents.size() > 0) {
            List<PositionVO> subContents = newContents.subList(0, (newContents.size() > 500) ? 500 : newContents.size());
            customerKeywordDao.batchUpdatePosition(terminalType, entryType, searchEngine, reachStandardPosition, subContents);
            newContents.removeAll(subContents);
        }
    }

    public void updateCustomerKeywordStatInfo (){
        List<Long> uuids = customerKeywordTerminalRefreshStatRecordDao.findMostDistantCustomerKeywordTerminalRefreshStatRecord();
        if (CollectionUtils.isNotEmpty(uuids)) {
            customerKeywordTerminalRefreshStatRecordDao.deleteBatchIds(uuids);
        }
        List<CustomerKeywordTerminalRefreshStatRecord> refreshStatInfoRecords = generateAllCustomerKeywordStatInfo(new CustomerKeywordRefreshStatInfoCriteria());
        for (CustomerKeywordTerminalRefreshStatRecord refreshStatInfoRecord : refreshStatInfoRecords) {
            refreshStatInfoRecord.setCreateDate(new Date());
            customerKeywordTerminalRefreshStatRecordDao.insert(refreshStatInfoRecord);
        }
    }

    private List<CustomerKeywordTerminalRefreshStatRecord> generateAllCustomerKeywordStatInfo(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        List<CustomerKeywordTerminalRefreshStatRecord> customerKeywordStatInfoRecords = getCustomerKeywordStatInfoRecords(customerKeywordRefreshStatInfoCriteria);
        Map<String, Map<String, Map<String, CustomerKeywordTerminalRefreshStatRecord>>> refreshStatInfoRecordGroupMap = new HashMap<String, Map<String, Map<String, CustomerKeywordTerminalRefreshStatRecord>>>();
        for (CustomerKeywordTerminalRefreshStatRecord customerKeywordTerminalRefreshStatRecord : customerKeywordStatInfoRecords) {
            Map<String, Map<String, CustomerKeywordTerminalRefreshStatRecord>> refreshStatInfoRecordTerminalTypeMap = refreshStatInfoRecordGroupMap.get(customerKeywordTerminalRefreshStatRecord.getGroup());
            if (null == refreshStatInfoRecordTerminalTypeMap) {
                refreshStatInfoRecordTerminalTypeMap = new HashMap<String, Map<String, CustomerKeywordTerminalRefreshStatRecord>>();
            }
            Map<String, CustomerKeywordTerminalRefreshStatRecord> refreshStatInfoRecordTypeMap = refreshStatInfoRecordTerminalTypeMap.get(customerKeywordTerminalRefreshStatRecord.getTerminalType());
            if (null == refreshStatInfoRecordTypeMap) {
                refreshStatInfoRecordTypeMap = new HashMap<String, CustomerKeywordTerminalRefreshStatRecord>();
            }
            refreshStatInfoRecordTypeMap.put(customerKeywordTerminalRefreshStatRecord.getType(), customerKeywordTerminalRefreshStatRecord);
            refreshStatInfoRecordTerminalTypeMap.put(customerKeywordTerminalRefreshStatRecord.getTerminalType(), refreshStatInfoRecordTypeMap);
            refreshStatInfoRecordGroupMap.put(customerKeywordTerminalRefreshStatRecord.getGroup(), refreshStatInfoRecordTerminalTypeMap);
        }

        List<CustomerKeywordTerminalRefreshStatRecord> searchClientStatusForRefreshStatRecords = machineInfoService.searchMachineInfoForRefreshStat(customerKeywordRefreshStatInfoCriteria);
        for (CustomerKeywordTerminalRefreshStatRecord csCustomerKeywordTerminalRefreshStatRecord : searchClientStatusForRefreshStatRecords) {
            Map<String, Map<String, CustomerKeywordTerminalRefreshStatRecord>> csCustomerKeywordRefreshStatInfoRecordTerminalTypeMap = refreshStatInfoRecordGroupMap.get(csCustomerKeywordTerminalRefreshStatRecord.getGroup());
            if (null != csCustomerKeywordRefreshStatInfoRecordTerminalTypeMap) {
                Map<String, CustomerKeywordTerminalRefreshStatRecord> csCustomerKeywordRefreshStatInfoRecordTypeMap = csCustomerKeywordRefreshStatInfoRecordTerminalTypeMap.get(csCustomerKeywordTerminalRefreshStatRecord.getTerminalType());
                if (null != csCustomerKeywordRefreshStatInfoRecordTypeMap) {
                    for (CustomerKeywordTerminalRefreshStatRecord customerKeywordTerminalRefreshStatRecord : csCustomerKeywordRefreshStatInfoRecordTypeMap.values()) {
                        customerKeywordTerminalRefreshStatRecord.setIdleTotalMinutes(csCustomerKeywordTerminalRefreshStatRecord.getIdleTotalMinutes());
                        customerKeywordTerminalRefreshStatRecord.setTotalMachineCount(csCustomerKeywordTerminalRefreshStatRecord.getTotalMachineCount());
                        customerKeywordTerminalRefreshStatRecord.setUnworkMachineCount(csCustomerKeywordTerminalRefreshStatRecord.getUnworkMachineCount());
                    }
                }
            }
        }
        return customerKeywordStatInfoRecords;
    }
}
