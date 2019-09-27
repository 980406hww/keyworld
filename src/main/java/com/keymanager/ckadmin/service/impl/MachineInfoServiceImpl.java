package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.dao.MachineInfoDao;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import com.keymanager.ckadmin.service.MachineInfoService;
import com.keymanager.ckadmin.vo.MachineInfoSummaryVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("machineInfoService2")
public class MachineInfoServiceImpl implements MachineInfoService {

    @Resource(name = "machineInfoDao2")
    private MachineInfoDao machineInfoDao;

    @Override
    public List<MachineGroupWorkInfo> searchMachineInfoFormMachineGroupWorkInfo(MachineGroupWorkInfoCriteria criteria) {
        return machineInfoDao.searchMachineInfoFormMachineGroupWorkInfo(criteria);
    }

    @Override
    public List<Map<String, Object>> getMachineInfos() {
        String[] strings = {"北京", "天津", "上海", "重庆", "河北", "河南", "云南", "辽宁", "黑龙江", "湖南", "安徽",
            "山东", "新疆", "江苏", "浙江", "江西", "湖北", "广西", "甘肃", "山西", "内蒙古", "陕西", "吉林", "福建", "贵州",
            "广东", "青海", "西藏", "四川", "宁夏", "海南", "台湾", "香港", "澳门", "南海诸岛"};
        List<Map<String, Object>> list = new ArrayList<>(strings.length);
        for (String str : strings) {
            list.add(getCityMap(str));
        }
        List<MachineInfoSummaryVO> machineInfos = machineInfoDao.getAllMachineInfo();
        for (MachineInfoSummaryVO machineInfo : machineInfos) {
            int index = getIndex(machineInfo.getCity(), strings);
            if (index == -1) {
                continue;
            }
            Map<String, Object> map = list.get(index);
            if (null != map.get("value")) {
                map.put("value", (int) map.get("value") + machineInfo.getCount());
            } else {
                map.put("value", machineInfo.getCount());
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> getMachineInfoBody(String cityName) {
        List<MachineInfoSummaryVO> machineInfos = machineInfoDao.getMachineInfoBody(cityName);
        int unicom = 0, telecom = 0, other = 0;
        for (MachineInfoSummaryVO machineInfo : machineInfos) {
            if (null == machineInfo.getCity()) {
                continue;
            }
            if (machineInfo.getCity().contains("联通")) {
                unicom += machineInfo.getCount();
            } else if (machineInfo.getCity().contains("电信")) {
                telecom += machineInfo.getCount();
            } else {
                other += machineInfo.getCount();
            }
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("unicom", unicom);
        map.put("telecom", telecom);
        map.put("other", other);
        Map<String, Object> data = new HashMap<>(2);
        data.put("data", machineInfos);
        data.put("total", map);
        return data;
    }

    private Map<String, Object> getCityMap(String cityName) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("name", cityName);
        return map;
    }

    private int getIndex(String city, String[] names) {
        if (null == city || "".equals(city)) {
            return -1;
        }
        int i = 0;
        for (String str : names) {
            if (city.contains(str)) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
