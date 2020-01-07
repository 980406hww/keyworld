package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_computer_name")
public class ComputerName extends BaseEntity{

    @TableField(value = "fComputerNamePrefix")
    private String computerNamePrefix;

    @TableField(value = "fSequence")
    private int sequence;

    public String getComputerNamePrefix() {
        return computerNamePrefix;
    }

    public void setComputerNamePrefix(String computerNamePrefix) {
        this.computerNamePrefix = computerNamePrefix;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}