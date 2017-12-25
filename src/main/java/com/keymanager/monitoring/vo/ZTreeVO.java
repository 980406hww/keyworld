package com.keymanager.monitoring.vo;

/**
 * Created by shunshikj08 on 2017/12/19.
 */
public class ZTreeVO {
    private Long id;
    private Long pId;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZTreeVO(Long id, Long pId, String name) {
        this.id = id;
        this.pId = pId;
        this.name = name;
    }
}
