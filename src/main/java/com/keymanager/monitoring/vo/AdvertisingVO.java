package com.keymanager.monitoring.vo;

import java.util.Date;

public class AdvertisingVO {

    private int aid;
    private int clsid;
    private int typeid;
    private String tagname;
    private String adname;
    private int timeset;
    private Date starttime;
    private Date endtime;
    private String normbody;
    private String expbody;
    private String typename;
    private String r_typename;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
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

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getR_typename() {
        return r_typename;
    }

    public void setR_typename(String r_typename) {
        this.r_typename = r_typename;
    }
}
