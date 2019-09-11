package com.keymanager.ckadmin.common;

import java.util.List;

/**
 * @ClassName UserTree
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/11 9:39
 * @Version 1.0
 */
public class UserTree {

    private Long id;
    private String name;
    private String alias;
    private Boolean spread;
    private Long parentTId;
    private List<UserTree> children;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Boolean getSpread() {
        return spread;
    }

    public void setSpread(Boolean spread) {
        this.spread = spread;
    }

    public List<UserTree> getChildren() {
        return children;
    }

    public void setChildren(List<UserTree> children) {
        this.children = children;
    }

    public Long getParentTId() {
        return parentTId;
    }

    public void setParentTId(Long parentTId) {
        this.parentTId = parentTId;
    }
}
