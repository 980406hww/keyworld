package com.keymanager.monitoring.common.result;

import java.util.List;

/**
 * @ClassName Menu
 * @Description TODO
 * @Author lhc
 * @Date 2019/8/30 11:08
 * @Version 1.0
 */
public class Menu {
    private String title;
    private String icon;
    private Boolean spread;
    private String href;
    private List<Menu> children;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getSpread() {
        return spread;
    }

    public void setSpread(Boolean spread) {
        this.spread = spread;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}
