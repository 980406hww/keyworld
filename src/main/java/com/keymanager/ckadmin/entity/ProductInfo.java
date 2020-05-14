package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName(value = "t_product_info")
public class ProductInfo {

    @TableId(value = "fUuid", type = IdType.AUTO)
    private int uuid;

    @TableField(value = "fProductName")
    private String productName;

    @TableField(value = "fProductPrice")
    private double productPrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    @TableField(value = "fCreateDate")
    private Date createDate;

    @TableField(value = "fAlterDate")
    private Date alterDate;

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getAlterDate() {
        return alterDate;
    }

    public void setAlterDate(Date alterDate) {
        this.alterDate = alterDate;
    }
}
