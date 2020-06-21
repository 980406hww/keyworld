package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.dao.ProductInfoDao;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.entity.MachineInfo;
import com.keymanager.ckadmin.entity.ProductInfo;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.ProductInfoService;
import com.keymanager.ckadmin.vo.ProductStatisticsVO;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

@Service(value = "productInfoService")
public class ProductInfoServiceImpl implements ProductInfoService {

    @Resource
    private ProductInfoDao productInfoDao;

    @Resource(name = "configService2")
    private ConfigService configService;

    @Override
    public ProductInfo getProductInfo(long id) {
        return productInfoDao.selectById(id);
    }

    @Override
    public Long getProductIdByName(String name) {
        return productInfoDao.getProductIdByName(name);
    }

    @Override
    public Long getProductId(ProductInfo productInfo){
        return productInfoDao.getProductId(productInfo);
    }

    @Override
    public Page<ProductInfo> getProductsByName(Page<ProductInfo> page,String name) {
        List<ProductInfo> productInfos= productInfoDao.getProductsByName(page,name);
        return page.setRecords(productInfos);
    }

    @Override
    public void deleteProduct(long uuid) {
        productInfoDao.deleteById(uuid);
    }

    @Override
    public void updateProduct(ProductInfo productInfo) {
        productInfoDao.updateAllColumnById(productInfo);
    }

    @Override
    public void addProduct(ProductInfo productInfo) {
        productInfoDao.insert(productInfo);
    }

    @Override
    public List<ProductInfo> getAllProduct() {
        return productInfoDao.getProductsByName("");
    }

    @Override
    public void updateProductPriceForUuids(List<Long> uuids, String productPrice){
        productInfoDao.updateProductPriceForUuids(uuids, productPrice);
    }

    @Override
    public List<ProductStatisticsVO> getAllProductStatistics(Long productId) throws ParseException {
        List<MachineInfo> machineInfos = productInfoDao.getAllProductStatistics(productId);
        if (CollectionUtils.isNotEmpty(machineInfos)) {
            HashMap<String, HashMap<String, List<MachineInfo>>> machineMap = new HashMap<>(16);
            for (MachineInfo info : machineInfos) {
                HashMap<String, List<MachineInfo>> machineInfoHashMap = machineMap.get(info.getProductName() + "###" + info.getProductId());
                if (null == machineInfoHashMap) {
                    machineInfoHashMap = new HashMap<>(16);
                    machineMap.put(info.getProductName() + "###" + info.getProductId(), machineInfoHashMap);
                }
                List<MachineInfo> infoList = machineInfoHashMap.get(info.getHost() + ":" + info.getPort());
                if (null == infoList) {
                    infoList = new ArrayList<>();
                    machineInfoHashMap.put(info.getHost() + ":" + info.getPort(), infoList);
                }
                infoList.add(info);
            }

            List<ProductStatisticsVO> productStatisticsVos = new ArrayList<>();
            // 获取清空机器操作次数和成功次数的日期
            Config config = configService.getConfig(Constants.CONFIG_TYPE_CLEAN_TIME, Constants.CONFIG_KEY_CLEAN_NAME);
            if (null != config) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String configValue = config.getValue();
                Date cleanDate = sdf.parse(configValue);
                for (Map.Entry<String, HashMap<String, List<MachineInfo>>> entry : machineMap.entrySet()) {
                    for (Map.Entry<String, List<MachineInfo>> listEntry : entry.getValue().entrySet()) {
                        int totalTimes = 0;
                        List<MachineInfo> infos = listEntry.getValue();
                        for (MachineInfo info : infos) {
                            int days;
                            Timestamp startUpTime = info.getStartUpTime() == null ? Utils.getCurrentTimestamp(): info.getStartUpTime();
                            // 计算运行天数
                            if (cleanDate.compareTo(startUpTime) > 0) {
                                days = Utils.getIntervalDays(startUpTime, new Date());
                            } else {
                                days = Utils.getIntervalDays(cleanDate, new Date());
                            }
                            // 计算性价比
                            int timesForOneRMB = 0;
                            if (info.getPrice() > 0.0) {
                                timesForOneRMB = (int) (info.getOptimizationSucceedCount() / (info.getPrice() / 30 * days));
                            }
                            totalTimes += timesForOneRMB;
                        }

                        ProductStatisticsVO vo = new ProductStatisticsVO();
                        String[] key = entry.getKey().split("###");
                        vo.setProductId(Long.parseLong(key[1]));
                        vo.setProductName(key[0]);
                        vo.setVncUrl(listEntry.getKey());
                        vo.setAvgTimesForOneRMB(totalTimes / infos.size());
                        vo.setCount(entry.getValue().size());
                        productStatisticsVos.add(vo);
                    }
                }
            }

            return productStatisticsVos;
        }
        return null;
    }

    @Override
    public List<ProductInfo> getSupperProduct() {
        return productInfoDao.getSupperProduct();
    }
}
