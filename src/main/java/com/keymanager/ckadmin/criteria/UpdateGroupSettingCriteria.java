package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.entity.GroupSetting;

public class UpdateGroupSettingCriteria {
    private GroupSetting gs;
    private String searchEngine;

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    private GroupSetting groupSetting;

    public GroupSetting getGs () {
        return gs;
    }

    public void setGs (GroupSetting gs) {
        this.gs = gs;
    }

    public GroupSetting getGroupSetting () {
        return groupSetting;
    }

    public void setGroupSetting (GroupSetting groupSetting) {
        this.groupSetting = groupSetting;
    }

}
