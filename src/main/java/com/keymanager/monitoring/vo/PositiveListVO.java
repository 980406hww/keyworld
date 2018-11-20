package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.entity.PositiveList;
import com.keymanager.monitoring.entity.PositiveListUpdateInfo;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/11/16 14:32
 **/
public class PositiveListVO {
    private PositiveList positiveList;
    private List<PositiveListUpdateInfo> positiveListUpdateInfoList;
    private boolean hasUpdateInfo;

    public boolean isHasUpdateInfo () {
        return hasUpdateInfo;
    }

    public void setHasUpdateInfo (boolean hasUpdateInfo) {
        this.hasUpdateInfo = hasUpdateInfo;
    }

    public void setPositiveListUpdateInfoList (List<PositiveListUpdateInfo> positiveListUpdateInfoList) {
        this.positiveListUpdateInfoList = positiveListUpdateInfoList;
    }

    public void setPositiveList (PositiveList positiveList) {
        this.positiveList = positiveList;
    }

    public PositiveList getPositiveList () {
        return positiveList;
    }

    public List<PositiveListUpdateInfo> getPositiveListUpdateInfoList () {
        return positiveListUpdateInfoList;
    }
}
