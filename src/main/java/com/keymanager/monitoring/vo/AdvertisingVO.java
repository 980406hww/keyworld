package com.keymanager.monitoring.vo;

import java.util.Date;

public class AdvertisingVO {

    private int id;
    private int clsid;
    private int typeid;
    private String adname;
    private int timeset;
    private Date starttime;
    private Date endtime;
    private String normbody;
    private String expbody;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClsid() {
        return clsid;
    }

    public void setClsid(int clsid) {
        this.clsid = clsid;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getAdname() {
        return adname;
    }

    public void setAdname(String adname) {
        this.adname = adname;
    }

    public int getTimeset() {
        return timeset;
    }

    public void setTimeset(int timeset) {
        this.timeset = timeset;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getNormbody() {
        return normbody;
    }

    public void setNormbody(String normbody) {
        this.normbody = normbody;
    }

    public String getExpbody() {
        return expbody;
    }

    public void setExpbody(String expbody) {
        this.expbody = expbody;
    }
}
