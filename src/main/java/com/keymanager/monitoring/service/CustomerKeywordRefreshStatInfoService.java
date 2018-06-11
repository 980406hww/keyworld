package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.dao.CustomerKeywordDao;
import com.keymanager.monitoring.dao.CustomerKeywordRefreshStatInfoDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.vo.CustomerKeywordRefreshStatInfoVO;
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
public class CustomerKeywordRefreshStatInfoService extends ServiceImpl<CustomerKeywordRefreshStatInfoDao, CustomerKeywordRefreshStatInfoVO> {

    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordRefreshStatInfoService.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    private ClientStatusService clientStatusService;

    @Autowired
    private CustomerKeywordDao customerKeywordDao;

    @Autowired
    private CustomerKeywordRefreshStatInfoDao customerKeywordRefreshStatInfoDao;

    public List<CustomerKeywordRefreshStatInfoVO> generateCustomerKeywordStatInfo(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        List<CustomerKeywordRefreshStatInfoVO> customerKeywordRefreshStatInfoVOs = getCustomerKeywordStatInfoVOs(customerKeywordRefreshStatInfoCriteria);
        Map<String, CustomerKeywordRefreshStatInfoVO> customerKeywordRefreshStatInfoVOMap = new HashMap<String, CustomerKeywordRefreshStatInfoVO>();
        for (CustomerKeywordRefreshStatInfoVO customerKeywordRefreshStatInfoVO : customerKeywordRefreshStatInfoVOs) {
            customerKeywordRefreshStatInfoVOMap.put(customerKeywordRefreshStatInfoVO.getGroup(), customerKeywordRefreshStatInfoVO);
        }

        List<ClientStatus> clientStatuses = clientStatusService.searchClientStatusForRefreshStat(customerKeywordRefreshStatInfoCriteria);
        for (ClientStatus clientStatus : clientStatuses) {
            CustomerKeywordRefreshStatInfoVO customerKeywordRefreshStatInfoVO = customerKeywordRefreshStatInfoVOMap.get(clientStatus.getGroup());
            if (customerKeywordRefreshStatInfoVO != null) {
                customerKeywordRefreshStatInfoVO.setTotalMachineCount(customerKeywordRefreshStatInfoVO.getTotalMachineCount() + 1);
                if (clientStatus.getRed()) {
                    customerKeywordRefreshStatInfoVO.setUnworkMachineCount(customerKeywordRefreshStatInfoVO.getUnworkMachineCount() + 1);
                }
            }
        }
        CustomerKeywordRefreshStatInfoVO total = new CustomerKeywordRefreshStatInfoVO();
        total.setGroup("总计");
        for (CustomerKeywordRefreshStatInfoVO customerKeywordRefreshStatInfoVO : customerKeywordRefreshStatInfoVOs) {
            total.setInvalidKeywordCount(total.getInvalidKeywordCount() + customerKeywordRefreshStatInfoVO.getInvalidKeywordCount());
            total.setNeedOptimizeCount(total.getNeedOptimizeCount() + customerKeywordRefreshStatInfoVO.getNeedOptimizeCount());
            total.setNeedOptimizeKeywordCount(total.getNeedOptimizeKeywordCount() + customerKeywordRefreshStatInfoVO.getNeedOptimizeKeywordCount());
            total.setQueryCount(total.getQueryCount() + customerKeywordRefreshStatInfoVO.getQueryCount());
            total.setTotalKeywordCount(total.getTotalKeywordCount() + customerKeywordRefreshStatInfoVO.getTotalKeywordCount());
            total.setTotalMachineCount(total.getTotalMachineCount() + customerKeywordRefreshStatInfoVO.getTotalMachineCount());
            total.setTotalOptimizeCount(total.getTotalOptimizeCount() + customerKeywordRefreshStatInfoVO.getTotalOptimizeCount());
            total.setTotalOptimizedCount(total.getTotalOptimizedCount() + customerKeywordRefreshStatInfoVO.getTotalOptimizedCount());
            total.setUnworkMachineCount(total.getUnworkMachineCount() + customerKeywordRefreshStatInfoVO.getUnworkMachineCount());
            total.setZeroOptimizedCount(total.getZeroOptimizedCount() + customerKeywordRefreshStatInfoVO.getZeroOptimizedCount());
            total.setReachStandardKeywordCount(total.getReachStandardKeywordCount() + customerKeywordRefreshStatInfoVO.getReachStandardKeywordCount());
            total.setTodaySubTotal(total.getTodaySubTotal() + customerKeywordRefreshStatInfoVO.getTodaySubTotal());
            total.setMaxInvalidCount(customerKeywordRefreshStatInfoVO.getMaxInvalidCount());
        }
        customerKeywordRefreshStatInfoVOs.add(0, total);
        return customerKeywordRefreshStatInfoVOs;
    }

    private List<CustomerKeywordRefreshStatInfoVO> getCustomerKeywordStatInfoVOs(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        Config config = configService.getConfig(Constants.CONFIG_KEY_MAX_INVALID_COUNT, customerKeywordRefreshStatInfoCriteria.getEntryType());
        customerKeywordRefreshStatInfoCriteria.setConfigValue(config.getValue());
        List<CustomerKeywordRefreshStatInfoVO> customerKeywordRefreshStatInfoVOs = customerKeywordRefreshStatInfoDao.searchCustomerKeywordStatInfoVOs(customerKeywordRefreshStatInfoCriteria);
        List<CustomerKeywordRefreshStatInfoVO> customerKeywordRefreshStatInfoVOList = new ArrayList<CustomerKeywordRefreshStatInfoVO>();
        for(CustomerKeywordRefreshStatInfoVO customerKeywordRefreshStatInfoVO : customerKeywordRefreshStatInfoVOs) {
            CustomerKeywordRefreshStatInfoVO refreshStatInfoVO = new CustomerKeywordRefreshStatInfoVO();
            refreshStatInfoVO.setGroup(customerKeywordRefreshStatInfoVO.getGroup());
            refreshStatInfoVO.setTotalKeywordCount(customerKeywordRefreshStatInfoVO.getTotalKeywordCount());
            refreshStatInfoVO.setNeedOptimizeKeywordCount(customerKeywordRefreshStatInfoVO.getNeedOptimizeKeywordCount());
            refreshStatInfoVO.setInvalidKeywordCount(customerKeywordRefreshStatInfoVO.getInvalidKeywordCount());
            refreshStatInfoVO.setZeroOptimizedCount(customerKeywordRefreshStatInfoVO.getZeroOptimizedCount());
            refreshStatInfoVO.setReachStandardKeywordCount(customerKeywordRefreshStatInfoVO.getReachStandardKeywordCount());
            refreshStatInfoVO.setTodaySubTotal(customerKeywordRefreshStatInfoVO.getTodaySubTotal());
            refreshStatInfoVO.setTotalOptimizeCount(customerKeywordRefreshStatInfoVO.getTotalOptimizeCount());
            refreshStatInfoVO.setTotalOptimizedCount(customerKeywordRefreshStatInfoVO.getTotalOptimizedCount());
            refreshStatInfoVO.setNeedOptimizeCount(customerKeywordRefreshStatInfoVO.getNeedOptimizeCount());
            refreshStatInfoVO.setQueryCount(customerKeywordRefreshStatInfoVO.getQueryCount());
            refreshStatInfoVO.setMaxInvalidCount(Integer.parseInt(config.getValue()));
            customerKeywordRefreshStatInfoVOList.add(refreshStatInfoVO);
        }
        return customerKeywordRefreshStatInfoVOList;
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
}
