package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.ProductCriteria;
import com.keymanager.ckadmin.dao.ProductInfoDao;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.entity.MachineInfo;
import com.keymanager.ckadmin.entity.ProductInfo;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.ProductInfoService;
import com.keymanager.ckadmin.util.StringUtil;
import com.keymanager.ckadmin.vo.ProductStatisticsVO;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

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
    public Page<ProductInfo> getProducts(Page<ProductInfo> page, ProductCriteria criteria) {
        List<ProductInfo> productInfos= productInfoDao.getProducts(page, criteria);
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
                List<MachineInfo> infoList = machineInfoHashMap.get(info.getHost());
                if (null == infoList) {
                    infoList = new ArrayList<>();
                    machineInfoHashMap.put(info.getHost(), infoList);
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
                    ProductStatisticsVO vo = new ProductStatisticsVO();
                    int productTotalTimes = 0;
                    int productMachineCount = 0;
                    String[] key = entry.getKey().split("###");
                    vo.setProductId(Long.parseLong(key[1]));
                    vo.setProductName(key[0]);
                    for (Map.Entry<String, List<MachineInfo>> listEntry : entry.getValue().entrySet()) {
                        int totalTimes = 0;
                        List<MachineInfo> infos = listEntry.getValue();
                        for (MachineInfo info : infos) {
                            int days;
                            Date openDate = info.getOpenDate() == null ? Utils.getCurrentTimestamp(): info.getOpenDate();
                            // 计算运行天数
                            if (cleanDate.compareTo(openDate) > 0) {
                                days = Utils.getTwoDateIntervalDays(cleanDate, new Date());
                            } else {
                                days = Utils.getTwoDateIntervalDays(openDate, new Date());
                            }
                            // 计算性价比
                            int timesForOneRMB = 0;
                            if (info.getPrice() > 0.0) {
                                timesForOneRMB = (int) (info.getOptimizationSucceedCount() / (info.getPrice() / 30 * days));
                            }
                            totalTimes += timesForOneRMB;
                        }

                        String vncA = "<a href=\"javascript:void(0);\" style=\"color: #0c7df5;\" onclick=\"toMachineInfoForVncHost('" + vo.getProductId() + "', '" + vo.getProductName() + "')\">" + listEntry.getKey() + "</a>";
                        String vncUrl = "<span class=\"left-span\">" + vncA + "</span><span class=\"right-span\">" + totalTimes / infos.size() +
                                "</span>";
                        if (StringUtil.isNullOrEmpty(vo.getVncUrl())) {
                            vo.setVncUrl(vncUrl);
                        } else {
                            vo.setVncUrl(vo.getVncUrl() + "<br><hr>" + vncUrl);
                        }

                        productTotalTimes += totalTimes;
                        productMachineCount += infos.size();
                    }
                    vo.setProductAvgTimes(productTotalTimes / productMachineCount);
                    productStatisticsVos.add(vo);
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

    @Override
    public List<ProductStatisticsVO> getProductStatisticsForTerminalType(ProductCriteria productCriteria) throws ParseException {
        // 获取所有机器的产品和vnc信息。
        List<MachineInfo> machineInfos = productInfoDao.getProductStatisticsForTerminalType(productCriteria);
        if (CollectionUtils.isNotEmpty(machineInfos)) {
            HashMap<String, HashMap<String, List<MachineInfo>>> machineMap = new HashMap<>(16);
            for (MachineInfo info : machineInfos) {
                // 产品名###产品ID###产品供应商为KEY，获取MAP。
                HashMap<String, List<MachineInfo>> machineInfoHashMap = machineMap.get(info.getProductName() + "###" + info.getProductId() + "###" + info.getProductSuppliers());
                // MAP为空的话获取创建一个新的MAP。
                if (null == machineInfoHashMap) {
                    machineInfoHashMap = new HashMap<>(16);
                    machineMap.put(info.getProductName() + "###" + info.getProductId() + "###" + info.getProductSuppliers(), machineInfoHashMap);
                }
                // host为key，进行分类。
                List<MachineInfo> infoList = machineInfoHashMap.get(info.getHost());
                if (null == infoList) {
                    infoList = new ArrayList<>();
                    machineInfoHashMap.put(info.getHost(), infoList);
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
                    ProductStatisticsVO vo = new ProductStatisticsVO();
                    int productTotalTimes = 0;
                    int productMachineCount = 0;
                    String[] key = entry.getKey().split("###");
                    vo.setProductId(Long.parseLong(key[1]));
                    vo.setProductName(key[0]);
                    vo.setProductSuppliers(key[2]);
                    for (Map.Entry<String, List<MachineInfo>> listEntry : entry.getValue().entrySet()) {
                        int totalTimes = 0;
                        List<MachineInfo> infos = listEntry.getValue();
                        for (MachineInfo info : infos) {
                            int days;
                            Date openDate = info.getOpenDate() == null ? Utils.getCurrentTimestamp(): info.getOpenDate();
                            // 计算运行天数
                            if (cleanDate.compareTo(openDate) > 0) {
                                days = Utils.getTwoDateIntervalDays(cleanDate, new Date());
                            } else {
                                days = Utils.getTwoDateIntervalDays(openDate, new Date());
                            }
                            // 计算性价比
                            int timesForOneRMB = 0;
                            if (info.getPrice() > 0.0) {
                                timesForOneRMB = (int) (info.getOptimizationSucceedCount() / (info.getPrice() / 30 * days));
                            }
                            totalTimes += timesForOneRMB;
                        }

                        String vncA = "<a href=\"javascript:void(0);\" style=\"color: #0c7df5;\" onclick=\"toMachineInfoForVncHost('" + vo.getProductId() + "', '" + vo.getProductName() + "', '" + listEntry.getKey() +"')\">" + listEntry.getKey() + "</a>";
                        String vncUrl = "<span class=\"left-span\">" + vncA + "</span><span class=\"right-span\">" + totalTimes / infos.size() + "</span>";
                        if (StringUtil.isNullOrEmpty(vo.getVncUrl())) {
                            vo.setVncUrl(vncUrl);
                        } else {
                            vo.setVncUrl(vo.getVncUrl() + "<br><hr>" + vncUrl);
                        }

                        productTotalTimes += totalTimes;
                        productMachineCount += infos.size();
                    }
                    vo.setProductAvgTimes(productTotalTimes / productMachineCount);
                    vo.setMachineCount(productMachineCount);
                    productStatisticsVos.add(vo);
                }
            }
            return productStatisticsVos;
        }
        return null;
    }
}
