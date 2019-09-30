package com.keymanager.ckadmin.vo;

import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile;

public class MachineInfoUploadVO implements Serializable {

    private String machineInfoType;
    private String downloadProgramType;
    private MultipartFile file;

    public String getMachineInfoType() {
        return machineInfoType;
    }

    public void setMachineInfoType(String machineInfoType) {
        this.machineInfoType = machineInfoType;
    }

    public String getDownloadProgramType() {
        return downloadProgramType;
    }

    public void setDownloadProgramType(String downloadProgramType) {
        this.downloadProgramType = downloadProgramType;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
