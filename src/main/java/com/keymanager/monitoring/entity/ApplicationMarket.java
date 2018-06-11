package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;
import java.util.List;


/**
 * Created by shunshikj22 on 2017/9/5.
 */
@TableName(value = "t_application_market_info")
public class ApplicationMarket{

    private static final long serialVersionUID = 3922222059082125030L;

    @TableId(value = "fUuid", type= IdType.AUTO)
    private Long uuid;

    @TableField(value = "fMarketName")
    private String marketName;

    @TableField(value = "fMarketPackageName")
    private String marketPackageName;

    @TableField(value = "fTmpPath")
    private String tmpPath;

    @TableField(value = "fApkPath")
    private String apkPath;

    @TableField(value = "fDataDBPath")
    private String dataDBPath;

    @TableField(value = "fStorageDBPath")
    private String storageDBPath;

    @TableField(value = "fFileType")
    private String fileType;

    @TableField(value = "fCreateTime")
    private Date createTime;

    @TableField(exist = false)
    private String[] fFileTypes;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketPackageName() {
        return marketPackageName;
    }

    public void setMarketPackageName(String marketPackageName) {
        this.marketPackageName = marketPackageName;
    }

    public String getTmpPath() {
        return tmpPath;
    }

    public void setTmpPath(String tmpPath) {
        this.tmpPath = tmpPath;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public String getDataDBPath() {
        return dataDBPath;
    }

    public void setDataDBPath(String dataDBPath) {
        this.dataDBPath = dataDBPath;
    }

    public String getStorageDBPath() {
        return storageDBPath;
    }

    public void setStorageDBPath(String storageDBPath) {
        this.storageDBPath = storageDBPath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String[] getfFileTypes() {
        return fFileTypes;
    }

    public void setfFileTypes(String[] fFileTypes) {
        this.fFileTypes = fFileTypes;
    }
}
