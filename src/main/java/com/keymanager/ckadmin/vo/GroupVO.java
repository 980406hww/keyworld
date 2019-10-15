package com.keymanager.ckadmin.vo;


public class GroupVO {

    private Long groupUuid;
    private String groupName;

    public Long getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(Long groupUuid) {
        this.groupUuid = groupUuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GroupVO other = (GroupVO) obj;
        return groupName.equals(other.groupName);
    }
}
