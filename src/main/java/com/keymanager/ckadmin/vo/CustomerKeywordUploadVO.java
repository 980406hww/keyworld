package com.keymanager.ckadmin.vo;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author lhc
 * @Date 2019/9/17 14:03
 */
public class CustomerKeywordUploadVO {

    private Long qzUuid;
    private Long customerUuid;
    private String excelType;
    private String entryType;
    private String terminalType;
    private MultipartFile file;

    public Long getQzUuid() {
        return qzUuid;
    }

    public void setQzUuid(Long qzUuid) {
        this.qzUuid = qzUuid;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
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
