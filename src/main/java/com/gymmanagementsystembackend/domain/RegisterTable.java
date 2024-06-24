package com.gymmanagementsystembackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("register")
public class RegisterTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "user_id")
    private String userId;
    @TableField(value = "account")
    private String account;
    @TableField(value = "password")
    private String password;
    @TableField(value = "mode")
    private String mode;
    @TableField(value = "join_date")
    private String joinDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String toString() {
        return "RegisterTable{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", mode='" + mode + '\'' +
                ", joinDate='" + joinDate + '\'' +
                '}';
    }
}
