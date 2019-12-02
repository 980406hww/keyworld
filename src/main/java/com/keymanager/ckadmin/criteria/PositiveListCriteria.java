package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;
import com.keymanager.ckadmin.vo.PositiveListVO;
import java.util.List;

public class PositiveListCriteria extends BaseCriteria {
    private String terminalType;
    private String keyword;
    private String title;
    private String url;
    private String desc;
    private Integer position;
    private String operationType;
    private String originalUrl;
    private String btnType;
    private String userName;
    private String password;
    private List<PositiveListVO> positiveListVos;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getBtnType () {
        return btnType;
    }

    public void setBtnType (String btnType) {
        this.btnType = btnType;
    }

    public List<PositiveListVO> getPositiveListVos() {
        return positiveListVos;
    }

    public void setPositiveListVos(List<PositiveListVO> positiveListVos) {
        this.positiveListVos = positiveListVos;
    }
}
