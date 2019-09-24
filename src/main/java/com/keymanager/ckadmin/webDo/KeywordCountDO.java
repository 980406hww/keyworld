package com.keymanager.ckadmin.webDo;

import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName KeywordCountVO
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/17 14:03
 * @Version 1.0
 */
public class KeywordCountDO {

    private Integer customerUuid;
    private String excelType;
    private String entryType;
    private String terminalType;
    private MultipartFile file;

    public Integer getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Integer customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getExcelType() {
        return excelType;
    }

    public void setExcelType(String excelType) {
        this.excelType = excelType;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
