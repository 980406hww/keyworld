package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.entity.QZChargeRule;
import java.util.List;
import java.util.Map;

/**
 * @Author zhoukai
 * @Date 2019/5/28 15:41
 **/
public class QZChargeRuleStandardResultVO {

    private String standardType;
    private Map<String, List<QZChargeRule>> qzChargeRuleMap;

    public String getStandardType () {
        return standardType;
    }

    public void setStandardType (String standardType) {
        this.standardType = standardType;
    }

    public Map<String, List<QZChargeRule>> getQzChargeRuleMap () {
        return qzChargeRuleMap;
    }

    public void setQzChargeRuleMap (Map<String, List<QZChargeRule>> qzChargeRuleMap) {
        this.qzChargeRuleMap = qzChargeRuleMap;
    }
}
