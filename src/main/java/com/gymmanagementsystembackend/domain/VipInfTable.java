package com.gymmanagementsystembackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.xml.crypto.Data;
import java.util.Date;

@TableName("vip_inf")
public class VipInfTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "vip_id")
    private String vipId;
    @TableField(value = "type")
    private String type;
    @TableField(value = "join_date")
    private String joinDate;
    @TableField(value = "expiration_date")
    private String expirationDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "VipInfTable{" +
                "id=" + id +
                ", vipId='" + vipId + '\'' +
                ", type='" + type + '\'' +
                ", joinDate='" + joinDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                '}';
    }
}
