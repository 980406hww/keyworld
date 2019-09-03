package com.keymanager.ckadmin.common.result;

public class ResultBean {
    private Integer code;
    private String msg;
    private String entryType;
    private Integer count;
    private Object data;

    public ResultBean(Integer code, String msg, Integer count, Object data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public ResultBean(Integer code, String msg, String entryType) {
        this.code = code;
        this.msg = msg;
        this.entryType = entryType;
    }

    public ResultBean(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultBean() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
